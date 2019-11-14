package lumien.randomthings.worldgen;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class BloodRoseFeature extends FlowersFeature
{

	public BloodRoseFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49876_1_)
	{
		super(p_i49876_1_);
	}

	@Override
	public BlockState getRandomFlower(Random random, BlockPos pos)
	{
		return ModBlocks.BLOOD_ROSE.getDefaultState();
	}

	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		BlockState blockstate = this.getRandomFlower(rand, pos);
		int i = 0;

		for (int j = 0; j < 8; ++j)
		{
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
			if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 255 && blockstate.isValidPosition(worldIn, blockpos))
			{
				worldIn.setBlockState(blockpos, blockstate, 2);
				++i;
			}
		}

		return i > 0;
	}

}
