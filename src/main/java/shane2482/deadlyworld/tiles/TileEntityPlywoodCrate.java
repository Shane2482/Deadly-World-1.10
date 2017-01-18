package shane2482.deadlyworld.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import shane2482.deadlyworld.blocks.storage.block_plywood_crate;
import shane2482.deadlyworld.blocks.storage.container.ContainerPlywoodCrate;


public class TileEntityPlywoodCrate extends TileEntityLockableLoot implements IInventory {

	private ItemStack[] chestContents = new ItemStack[72];
	private String customName;
	private int numPlayersUsing;

	public TileEntityPlywoodCrate() {
	}

	// Size
	@Override
	public int getSizeInventory() {
		return 72;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	// Name
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.plywood_crate";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomName(String name) {
		this.customName = name;
	}

	// Slots
	@Override
	public ItemStack getStackInSlot(int index) {
		return chestContents[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		ItemStack itemstack = ItemStackHelper.getAndSplit(this.chestContents, index, count);

		if (itemstack != null) {
			this.markDirty();
		}

		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {

		return ItemStackHelper.getAndRemove(this.chestContents, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		this.chestContents[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	// NBT
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.chestContents = new ItemStack[this.getSizeInventory()];

		if (nbt.hasKey("CustomName", 8)) {
			this.customName = nbt.getString("CustomName");
		}

		if (!this.checkLootAndRead(nbt)) {
			NBTTagList list = nbt.getTagList("Items", 10);

			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound compound = list.getCompoundTagAt(i);
				int j = compound.getByte("Slot") & 255;

				if (j >= 0 && j < this.chestContents.length) {
					this.chestContents[j] = ItemStack.loadItemStackFromNBT(compound);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		if (!this.checkLootAndWrite(nbt)) {
			NBTTagList list = new NBTTagList();

			for (int i = 0; i < this.chestContents.length; ++i) {
				if (this.chestContents[i] != null) {
					NBTTagCompound compound = new NBTTagCompound();
					compound.setByte("Slot", (byte) i);
					this.chestContents[i].writeToNBT(compound);
					list.appendTag(compound);
				}
			}

			nbt.setTag("Items", list);
		}

		if (this.hasCustomName()) {
			nbt.setString("CustomName", this.customName);
		}

		return nbt;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

	// Usability
	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
						(double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);

		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator() && this.getBlockType() instanceof block_plywood_crate) {
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);

		}
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	// GUI/Container
	@Override
	public String getGuiID() {
		return "Plywood_Crate";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {

		return new ContainerPlywoodCrate(playerInventory, this);
	}

	// Miss

	@Override
	public void markDirty() {
		super.markDirty();

	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();

	}

	@Override
	public void clear() {

		for (int i = 0; i < this.chestContents.length; ++i) {
			this.chestContents[i] = null;
		}
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	public static void registerFixesChest(DataFixer fixer) {
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists("Chest", new String[] { "Items" }));
	}

	
}
