package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.BlockLotus;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class WorldGenPlants extends WorldGenerator
{
	@Override
	public boolean generate(World world, Random random, BlockPos position)
	{
		if (world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
		{
			// Beans
			if (Worldgen.beans && random.nextBoolean())
			{
				int x = position.getX() + 8 + random.nextInt(16);
				int z = position.getZ() + 8 + random.nextInt(16);

				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() > 0 && world.isAirBlock(target) && (target.getY() < 255) && ModBlocks.beanSprout.canBlockStay(world, target, ModBlocks.beanSprout.getDefaultState()))
				{
					world.setBlockState(target, ModBlocks.beanSprout.getDefaultState(), 2);
				}
			}

			// Pitcher Plants
			if (Worldgen.pitcherPlants && random.nextInt(10) == 0)
			{
				int x = position.getX() + 8 + random.nextInt(16);
				int z = position.getZ() + 8 + random.nextInt(16);

				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() >= 0)
				{
					Biome biome = world.getBiome(target);

					if (world.isAirBlock(target) && biome.getTemperature(target) >= 0.8F && ModBlocks.pitcherPlant.canBlockStay(world, target, ModBlocks.pitcherPlant.getDefaultState()))
					{
						world.setBlockState(target, ModBlocks.pitcherPlant.getDefaultState(), 2);
					}
				}
			}

			if (Worldgen.LOTUS && random.nextInt(10) == 0)
			{
				int x = position.getX() + 8 + random.nextInt(16);
				int z = position.getZ() + 8 + random.nextInt(16);

				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() >= 0)
				{
					Biome biome = world.getBiome(target);
					IBlockState state = world.getBlockState(target);

					IBlockState placeState = ModBlocks.lotus.getDefaultState().withProperty(BlockLotus.AGE, random.nextInt(4));

					if ((state.getBlock().isAir(state, world, target) || state.getBlock().isReplaceable(world, target)) && BiomeDictionary.hasType(biome, Type.SNOWY) && ModBlocks.lotus.canBlockStay(world, target, placeState))
					{
						world.setBlockState(target, placeState, 2);
					}
				}
			}
		}

		return true;
	}
}