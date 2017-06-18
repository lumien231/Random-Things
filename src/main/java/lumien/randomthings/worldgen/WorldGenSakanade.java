package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSakanade implements IWorldGenerator
{
	public static WorldGenSakanade instance = new WorldGenSakanade();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (Worldgen.sakanade)
		{
			int x = chunkX * 16 + 8 + random.nextInt(16);
			int z = chunkZ * 16 + 8 + random.nextInt(16);

			BlockPos target = WorldUtil.getHeighestPos(world, x, z);

			if (target != null)
			{
				Biome biome = world.getBiome(target);
				if (world.getBlockState(target).getBlock() == Blocks.BROWN_MUSHROOM_BLOCK && world.isAirBlock(target.down()))
				{
					world.setBlockState(target.down(), ModBlocks.sakanade.getDefaultState());
				}
			}
		}
	}

}
