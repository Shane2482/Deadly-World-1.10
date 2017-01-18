package shane2482.deadlyworld.init;

import net.minecraft.item.Item;
import shane2482.deadlyworld.items.itembase;
import shane2482.deadlyworld.items.item_circuit;

public class ModItems {

	/////////////////////////////

	public static Item Circuits;
	public static Item Wood_Dust;
	public static Item Dirt_Clump;

	/////////////////////////////

	public static void init() {
		Circuits = new item_circuit("circuit", "item_circuit");
		Wood_Dust = new itembase("wood_dust", "item_wood_dust");
		Dirt_Clump = new itembase("dirt_clump", "item_dirt_clump");
	}

}