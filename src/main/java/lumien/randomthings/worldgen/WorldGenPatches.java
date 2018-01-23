package lumien.randomthings.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenPatches extends WorldGenerator
{
	private final Block block;

	public WorldGenPatches(Block blockIn)
	{
		this.block = blockIn;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < worldIn.getHeight() - 1) && this.block.canPlaceBlockAt(worldIn, blockpos) && !worldIn.canSeeSky(blockpos))
			{
				worldIn.setBlockState(blockpos, this.block.getDefaultState(), 2);
			}
		}

		return true;
	}
}