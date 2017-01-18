package shane2482.deadlyworld.blocks.storage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import shane2482.deadlyworld.DeadlyWorld;
import shane2482.deadlyworld.blocks.base.itemblocks;
import shane2482.deadlyworld.library.GuiHandler;
import shane2482.deadlyworld.library.Reference;
import shane2482.deadlyworld.tiles.TileEntityPlywoodCrate;

public class block_plywood_crate extends Block implements ITileEntityProvider{

	public block_plywood_crate(Material material, String name, String Regname, float hardness, float resistance, String tool, int level) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(Regname);
		setHarvestLevel(tool, level);
		setHardness(hardness);
		setResistance(resistance);
		this.setCreativeTab(DeadlyWorld.block);
		this.register();
	}

	
	private void register() {
		registerBlock(this, this.getItemBlocks());

		this.registerRendering();
	}

	public static void registerBlock(Block block, itemblocks itemBlock) {

		GameRegistry.register(block);
		itemBlock.setRegistryName(block.getRegistryName());
		GameRegistry.register(itemBlock);
	}

	

	protected itemblocks getItemBlocks() {
		return new itemblocks(this);
	}

	protected void registerRendering() {
		DeadlyWorld.proxy.addRenderRegister(new ItemStack(this), this.getRegistryName(), "inventory");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPlywoodCrate();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityPlywoodCrate Te = (TileEntityPlywoodCrate)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, Te);
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		 if (stack.hasDisplayName())
	        {
	            TileEntity Te = world.getTileEntity(pos);

	            if (Te instanceof TileEntityPlywoodCrate)
	            {
	                ((TileEntityPlywoodCrate)Te).setCustomName(stack.getDisplayName());
	            }
	        }
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && !player.isSneaking()){
			player.openGui(DeadlyWorld.instance, GuiHandler.Plywood_Crate, world, pos.getX(), pos.getY(), pos.getZ());		}
		return true;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

}
