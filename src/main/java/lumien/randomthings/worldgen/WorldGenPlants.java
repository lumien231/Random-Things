package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenPlants implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.getWorldType() != WorldType.DEBUG_WORLD)
		{
			// Beans
			if (Worldgen.beans && random.nextBoolean())
			{
				int x = chunkX * 16 + 8 + random.nextInt(16);
				int z = chunkZ * 16 + 8 + random.nextInt(16);

				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() > 0 && world.isAirBlock(target) && (!world.provider.hasNoSky() || target.getY() < 255) && ModBlocks.beanSprout.canBlockStay(world, target, ModBlocks.beanSprout.getDefaultState()))
				{
					world.setBlockState(target, ModBlocks.beanSprout.getDefaultState(), 2);
				}
			}

			// Pitcher Plants
			if (Worldgen.pitcherPlants && random.nextInt(10) == 0)
			{
				int x = chunkX * 16 + 8 + random.nextInt(16);
				int z = chunkZ * 16 + 8 + random.nextInt(16);

				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() >= 0)
				{
					Biome biome = world.getBiome(target);

					if (world.isAirBlock(target) && biome.getFloatTemperature(target) >= 0.8F && ModBlocks.pitcherPlant.canBlockStay(world, target, ModBlocks.pitcherPlant.getDefaultState()))
					{
						world.setBlockState(target, ModBlocks.pitcherPlant.getDefaultState(), 2);
					}
				}
			}
		}
	}
}