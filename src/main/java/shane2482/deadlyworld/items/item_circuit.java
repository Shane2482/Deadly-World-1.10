package shane2482.deadlyworld.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shane2482.deadlyworld.DeadlyWorld;
import shane2482.deadlyworld.library.Reference;

public class item_circuit extends itembase

{
	public static final String Bug_Item = Reference.MOD_ID + ".this is a bug";

	public item_circuit(String Name, String RegName) {
		super(Name, RegName);

	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return stack.getItemDamage() >= CircuitTypes.values().length ? Bug_Item
				: this.getUnlocalizedName() + "_" + CircuitTypes.values()[stack.getItemDamage()].name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
		for (int i = 0; i < CircuitTypes.values().length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	protected void registerRendering() {
		for (int i = 0; i < CircuitTypes.values().length; i++) {
			String name = this.getRegistryName() + "_" + CircuitTypes.values()[i].name();
			DeadlyWorld.proxy.addRenderRegister(new ItemStack(this, 1, i), new ResourceLocation(name), "inventory");
		}

	}

	private enum CircuitTypes implements IStringSerializable {

		Iron(0, "iron"), 
		Gold(1, "gold"), 
		Diamond(2, "diamond");

		/////////////////////////////

		private static final item_circuit.CircuitTypes[] METADATA_LOOKUP = new item_circuit.CircuitTypes[values().length];
		private final int metadata;
		private final String name;

		private CircuitTypes(int meta, String name) {
			this.metadata = meta;
			this.name = name;

		}

		public int getMetadata() {
			return this.metadata;
		}

		public String toString() {
			return this.name;
		}

		public static item_circuit.CircuitTypes byMetadata(int meta) {
			if (meta < 0 || meta >= METADATA_LOOKUP.length) {
				meta = 0;
			}

			return METADATA_LOOKUP[meta];
		}

		public String getName() {
			return this.name;
		}

		static {
			for (item_circuit.CircuitTypes itemcircuit$CircuitTypes : values()) {
				METADATA_LOOKUP[itemcircuit$CircuitTypes.getMetadata()] = itemcircuit$CircuitTypes;
			}
		}
	}

}
