package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.BlockAncientBrick;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.lib.AncientFurnaceConversion;
import lumien.randomthings.util.BlockPattern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenAncientFurnace implements IWorldGenerator
{
	public static BlockPattern pattern;

	public WorldGenAncientFurnace()
	{
		pattern = new BlockPattern();

		IBlockState defaultBrick = ModBlocks.ancientBrick.getDefaultState().withProperty(BlockAncientBrick.TYPE, BlockAncientBrick.VARIANT.DEFAULT);
		IBlockState runeBrick = ModBlocks.ancientBrick.getDefaultState().withProperty(BlockAncientBrick.TYPE, BlockAncientBrick.VARIANT.RUNES);
		IBlockState outputBrick = ModBlocks.ancientBrick.getDefaultState().withProperty(BlockAncientBrick.TYPE, BlockAncientBrick.VARIANT.OUTPUT);
		IBlockState emptyBrick = ModBlocks.ancientBrick.getDefaultState().withProperty(BlockAncientBrick.TYPE, BlockAncientBrick.VARIANT.STAR_EMPTY);
		IBlockState ancientFurnace = ModBlocks.ancientFurnace.getDefaultState();

		pattern.addBlock(ancientFurnace, 0, 1, 0);
		pattern.addBlock(outputBrick, 0, 0, 0);
		pattern.addBlock(emptyBrick, 0, 2, 0);

		pattern.addBlock(runeBrick, 0, 1, 1);
		pattern.addBlock(runeBrick, 1, 1, 0);
		pattern.addBlock(runeBrick, 0, 1, -1);
		pattern.addBlock(runeBrick, -1, 1, 0);
		pattern.addBlock(runeBrick, 1, 1, 1);
		pattern.addBlock(runeBrick, 1, 1, -1);
		pattern.addBlock(runeBrick, -1, 1, 1);
		pattern.addBlock(runeBrick, -1, 1, -1);

		pattern.addBlock(defaultBrick, 0, 2, 1);
		pattern.addBlock(defaultBrick, 1, 2, 0);
		pattern.addBlock(defaultBrick, 0, 2, -1);
		pattern.addBlock(defaultBrick, -1, 2, 0);
		pattern.addBlock(defaultBrick, 1, 2, 1);
		pattern.addBlock(defaultBrick, 1, 2, -1);
		pattern.addBlock(defaultBrick, -1, 2, 1);
		pattern.addBlock(defaultBrick, -1, 2, -1);

		pattern.addBlock(defaultBrick, 0, 0, 1);
		pattern.addBlock(defaultBrick, 1, 0, 0);
		pattern.addBlock(defaultBrick, 0, 0, -1);
		pattern.addBlock(defaultBrick, -1, 0, 0);
		pattern.addBlock(defaultBrick, 1, 0, 1);
		pattern.addBlock(defaultBrick, 1, 0, -1);
		pattern.addBlock(defaultBrick, -1, 0, 1);
		pattern.addBlock(defaultBrick, -1, 0, -1);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (Worldgen.ANCIENT_FURNACE && world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES && world.getWorldInfo().isMapFeaturesEnabled())
		{
			if (world.provider.getDimension() == 0 && random.nextInt(2000) == 0)
			{
				int x = chunkX * 16 + 8 + random.nextInt(16);
				int z = chunkZ * 16 + 8 + random.nextInt(16);
				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() >= 5)
				{
					Biome biome = world.getBiome(target);

					if (AncientFurnaceConversion.getHeatingConversion(biome) != null)
					{
						pattern.place(world, target.down(2), 0);
					}
				}
			}
		}
	}
}