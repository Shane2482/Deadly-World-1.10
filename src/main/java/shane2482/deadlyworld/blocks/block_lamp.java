package shane2482.deadlyworld.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shane2482.deadlyworld.blocks.base.blockbase;

public class block_lamp extends blockbase  {

	public block_lamp(Material material, String name, String Regname, float hardness, float resistance, String tool, int level) {
		super(material, name, Regname, hardness, resistance, tool, level);
		setLightLevel(10);
		setSoundType(SoundType.GLASS);
		
	}

	
}
