package shane2482.deadlyworld.tiles;

import java.awt.Container;



import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import shane2482.deadlyworld.blocks.storage.block_plywood_chest;
import shane2482.deadlyworld.init.ModBlocks;

public class TileEntityPlywoodChest extends TileEntityLockableLoot implements ITickable, IInventory {

	private ItemStack[] chestContents = new ItemStack[36];
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    private String customName;
    
    public TileEntityPlywoodChest()
    {
    }
    
    @Override
    public int getSizeInventory()
    {
        return 36;
    }
    
   
    @Override
    public ItemStack getStackInSlot(int index)
    {
        return chestContents[index];
    }
    @Override
    public void markDirty()
    {
        super.markDirty();
        
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.chestContents, index, count);

        if (itemstack != null)
        {
            this.markDirty();
        }

        return itemstack;
    }
    
   
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
       
        return ItemStackHelper.getAndRemove(this.chestContents, index);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
       
        this.chestContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }
    
    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.plywood_chest";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    public static void registerFixesChest(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists("Chest", new String[] {"Items"}));
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        if (!this.checkLootAndRead(compound))
        {
            NBTTagList nbttaglist = compound.getTagList("Items", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;

                if (j >= 0 && j < this.chestContents.length)
                {
                    this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 0; i < this.chestContents.length; ++i)
            {
                if (this.chestContents[i] != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte)i);
                    this.chestContents[i].writeToNBT(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
                }
            }

            compound.setTag("Items", nbttaglist);
        }

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }
    
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }
    
   public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        
    }
    
    @Override
    public void update()
    {
        
    	 if (++this.ticksSinceSync % 20 * 4 == 0)
         {
             this.worldObj.addBlockEvent(this.pos, ModBlocks.Plywood_Chest, 1, this.numPlayersUsing);
         }

         this.prevLidAngle = this.lidAngle;
         int i = this.pos.getX();
         int j = this.pos.getY();
         int k = this.pos.getZ();
         float f = 0.1F;

         if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
         {
             double d0 = (double)i + 0.5D;
             double d1 = (double)k + 0.5D;
             this.worldObj.playSound((EntityPlayer)null, d0, (double)j + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
         }

         if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
         {
             float f2 = this.lidAngle;

             if (this.numPlayersUsing > 0)
             {
                 this.lidAngle += 0.1F;
             }
             else
             {
                 this.lidAngle -= 0.1F;
             }

             if (this.lidAngle > 1.0F)
             {
                 this.lidAngle = 1.0F;
             }

             float f1 = 0.5F;

             if (this.lidAngle < 0.5F && f2 >= 0.5F)
             {
                 double d3 = (double)i + 0.5D;
                 double d2 = (double)k + 0.5D;
                 this.worldObj.playSound((EntityPlayer)null, d3, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
             }

             if (this.lidAngle < 0.0F)
             {
                 this.lidAngle = 0.0F;
             }
         }
     }
    
    
    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }
    
    
    @Override
    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            //this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            //this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }
    
    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator() && this.getBlockType() instanceof block_plywood_chest)
        {
            --this.numPlayersUsing;
            this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            //this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
            //this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
        }
    }
    
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
    
    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
        
    }
    
    @Override
    public String getGuiID()
    {
        return "Plywood_Chest";
    }
    
    @Override
    public ContainerChest createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            this.chestContents[i] = null;
        }
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
    public void handleUpdateTag(NBTTagCompound tag) {
    	this.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getTileData() {
    	NBTTagCompound nbt = new NBTTagCompound();
    	this.writeToNBT(nbt);
    	return nbt;
    }
}
