package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import lumien.randomthings.util.BlockPattern;
import lumien.randomthings.util.InventoryUtil;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCores implements IWorldGenerator
{
	public static BlockPattern natureCore;

	public WorldGenCores()
	{
		natureCore = new BlockPattern();

		IBlockState jungleLog = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
		IBlockState jungleLeaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(BlockLeaves.DECAYABLE, false);

		natureCore.addBlock(jungleLog, 0, 0, 0);
		natureCore.addBlock(jungleLog, 0, 2, 0);
		natureCore.addBlock(ModBlocks.natureCore.getDefaultState(), 0, 1, 0);

		natureCore.addBlock(jungleLog, -1, 0, -1);
		natureCore.addBlock(jungleLog, 1, 0, -1);
		natureCore.addBlock(jungleLog, -1, 0, 1);
		natureCore.addBlock(jungleLog, 1, 0, 1);

		natureCore.addBlock(jungleLog, 1, 1, 0);
		natureCore.addBlock(jungleLog, 0, 1, 1);
		natureCore.addBlock(jungleLog, 0, 1, -1);
		natureCore.addBlock(jungleLog, -1, 1, 0);

		natureCore.addBlock(jungleLog, -1, 2, -1);
		natureCore.addBlock(jungleLog, 1, 2, -1);
		natureCore.addBlock(jungleLog, -1, 2, 1);
		natureCore.addBlock(jungleLog, 1, 2, 1);

		natureCore.addBlock(jungleLeaves, 1, 0, 0);
		natureCore.addBlock(jungleLeaves, 0, 0, 1);
		natureCore.addBlock(jungleLeaves, 0, 0, -1);
		natureCore.addBlock(jungleLeaves, -1, 0, 0);

		natureCore.addBlock(jungleLeaves, -1, 1, -1);
		natureCore.addBlock(jungleLeaves, 1, 1, -1);
		natureCore.addBlock(jungleLeaves, -1, 1, 1);
		natureCore.addBlock(jungleLeaves, 1, 1, 1);

		natureCore.addBlock(jungleLeaves, 1, 2, 0);
		natureCore.addBlock(jungleLeaves, 0, 2, 1);
		natureCore.addBlock(jungleLeaves, 0, 2, -1);
		natureCore.addBlock(jungleLeaves, -1, 2, 0);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if (world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES && world.getWorldInfo().isMapFeaturesEnabled())
		{
			if (world.provider.getDimension() == 0)
			{
				int x = chunkX * 16 + 8 + random.nextInt(16);
				int z = chunkZ * 16 + 8 + random.nextInt(16);
				BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));

				if (target != null && target.getY() >= 0)
				{
					Biome biome = world.getBiome(target);

					int natureMult = 30;
					if (BiomeDictionary.hasType(biome, Type.DENSE))
					{
						natureMult -= 8;
					}
					if (BiomeDictionary.hasType(biome, Type.SPARSE))
					{
						natureMult += 4;
					}
					if (BiomeDictionary.hasType(biome, Type.WET))
					{
						natureMult -= 4;
					}
					if (BiomeDictionary.hasType(biome, Type.DRY))
					{
						natureMult += 2;
					}
					if (BiomeDictionary.hasType(biome, Type.DEAD))
					{
						natureMult += 10;
					}
					if (BiomeDictionary.hasType(biome, Type.MAGICAL))
					{
						natureMult -= 8;
					}

					boolean generateNatureCore = random.nextInt(18 * natureMult) == 0;

					if (!Worldgen.natureCore)
					{
						generateNatureCore = false;
					}

					if (generateNatureCore)
					{
						boolean canPlaceCore = true;
						for (int modX = -2; modX < 3; modX++)
						{
							for (int modZ = -2; modZ < 3; modZ++)
							{
								for (int modY = 0; modY < 3; modY++)
								{
									BlockPos check = new BlockPos(target.getX() + modX, target.getY() + modY, target.getZ() + modZ);

									if (!WorldUtil.isValidPosition(check))
									{
										canPlaceCore = false;
										break;
									}

									if (!world.isAirBlock(check.down()) && world.isSideSolid(check.down(), EnumFacing.UP))
									{
										if (!(world.isAirBlock(check) || world.getBlockState(check).getBlock().isReplaceable(world, check)))
										{
											canPlaceCore = false;
											break;
										}
									}
								}
							}
						}

						if (canPlaceCore)
						{
							if (generateNatureCore)
							{
								placeNatureCore(random, world, target);
							}
						}
					}
				}
			}
		}
	}

	private void placeNatureCore(Random random, World world, BlockPos target)
	{
		natureCore.place(world, target, 2);

		boolean placed = false;

		// Place Plant Chest
		for (int modX = -5; modX < 6; modX++)
		{
			for (int modZ = -5; modZ < 6; modZ++)
			{
				BlockPos check = new BlockPos(target.getX() + modX, target.getY(), target.getZ() + modZ);
				if ((world.isAirBlock(check) || world.getBlockState(check).getBlock().isReplaceable(world, check)))
				{
					world.setBlockState(check, ModBlocks.specialChest.getDefaultState(), 2);
					TileEntitySpecialChest te = (TileEntitySpecialChest) world.getTileEntity(check);

					if (te != null)
					{
						if (Worldgen.beans)
						{
							te.setInventorySlotContents(0, new ItemStack(ModItems.beans, random.nextInt(2) + 1, 2));
						}
						if (random.nextBoolean() && Worldgen.beans)
							te.setInventorySlotContents(1, new ItemStack(ModItems.beans, random.nextInt(20) + 5));
						if (random.nextBoolean())
							te.setInventorySlotContents(2, new ItemStack(Items.WHEAT, random.nextInt(20) + 5));
						if (random.nextBoolean())
							te.setInventorySlotContents(3, new ItemStack(Items.WHEAT_SEEDS, random.nextInt(20) + 5));
						if (random.nextBoolean())
							te.setInventorySlotContents(4, new ItemStack(Items.PUMPKIN_SEEDS, random.nextInt(15) + 2));
						if (random.nextBoolean())
							te.setInventorySlotContents(5, new ItemStack(Items.MELON_SEEDS, random.nextInt(15) + 2));
						if (random.nextBoolean())
							te.setInventorySlotContents(6, new ItemStack(Items.REEDS, random.nextInt(20) + 5));
						if (random.nextBoolean())
							te.setInventorySlotContents(7, new ItemStack(Items.POTATO, random.nextInt(20) + 5));
						if (random.nextBoolean())
							te.setInventorySlotContents(8, new ItemStack(Items.CARROT, random.nextInt(20) + 1));
						if (random.nextBoolean())
						{
							te.setInventorySlotContents(9, new ItemStack(Blocks.SAPLING, random.nextInt(20) + 1, random.nextInt(6)));
							if (random.nextBoolean())
								te.setInventorySlotContents(10, new ItemStack(Blocks.SAPLING, random.nextInt(20) + 1, random.nextInt(6)));
						}
						if (random.nextBoolean())
							te.setInventorySlotContents(11, new ItemStack(Blocks.VINE, random.nextInt(20) + 1));
						if (random.nextBoolean())
							te.setInventorySlotContents(12, new ItemStack(Blocks.RED_FLOWER, random.nextInt(20) + 1, random.nextInt(9)));

						InventoryUtil.shuffleInventory(te);
					}

					placed = true;
				}
				if (placed)
					break;
			}
			if (placed)
				break;
		}
	}
}