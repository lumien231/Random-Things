package lumien.randomthings.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

/**
 * SuperLubricentStoneBlock
 */
public class SuperLubricentStoneBlock extends Block
{

	public SuperLubricentStoneBlock()
	{
		super(Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F).slipperiness(1F / 0.91F));
	}


}