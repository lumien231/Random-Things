package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenPlants implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		// Beans
		if (random.nextBoolean())
		{
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);

			BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));
			if (world.isAirBlock(target) && (!world.provider.getHasNoSky() || target.getY() < 255) && ModBlocks.beanSprout.canBlockStay(world, target, ModBlocks.beanSprout.getDefaultState()))
			{
				world.setBlockState(target, ModBlocks.beanSprout.getDefaultState(), 2);
			}
		}

		// Pitcher Plants
		if (random.nextInt(10) == 0)
		{
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);

			BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));
			BiomeGenBase biome = world.getBiomeGenForCoords(target);

			if (world.isAirBlock(target) && biome.getFloatTemperature(target) >= 0.8F && biome.getFloatRainfall() >= 0.8F && ModBlocks.pitcherPlant.canBlockStay(world, target, ModBlocks.pitcherPlant.getDefaultState()))
			{
				world.setBlockState(target, ModBlocks.pitcherPlant.getDefaultState(), 2);
			}
		}
	}
}