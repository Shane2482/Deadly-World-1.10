package shane2482.deadlyworld.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class itemblocks extends ItemBlock {

	public itemblocks(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(false);

	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		return super.getUnlocalizedName();
	}

}
