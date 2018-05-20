package lumien.randomthings.block;

import lumien.randomthings.lib.ISuperLubricent;
import net.minecraft.block.material.Material;

public class BlockSuperLubricentStone extends BlockBase implements ISuperLubricent
{

	protected BlockSuperLubricentStone()
	{
		super("superlubricentstone", Material.ROCK);

		this.setHardness(1.5F);
	}

}
