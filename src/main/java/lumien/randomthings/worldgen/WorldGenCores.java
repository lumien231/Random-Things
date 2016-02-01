package lumien.randomthings.worldgen;

import java.util.Random;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import lumien.randomthings.util.BlockPattern;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenCores implements IWorldGenerator
{
	public static BlockPattern natureCore;
	public static BlockPattern enderCore;

	public WorldGenCores()
	{
		natureCore = new BlockPattern();
		enderCore = new BlockPattern();

		IBlockState obsidian = Blocks.obsidian.getDefaultState();
		IBlockState blackGlass = Blocks.stained_glass.getDefaultState().withProperty(BlockStainedGlass.COLOR, EnumDyeColor.BLACK);

		enderCore.addBlock(obsidian, 0, 0, 0);
		enderCore.addBlock(blackGlass, 0, 2, 0);
		enderCore.addBlock(ModBlocks.enderCore.getDefaultState(), 0, 1, 0);

		enderCore.addBlock(obsidian, -1, 0, -1);
		enderCore.addBlock(obsidian, 1, 0, -1);
		enderCore.addBlock(obsidian, -1, 0, 1);
		enderCore.addBlock(obsidian, 1, 0, 1);

		enderCore.addBlock(blackGlass, 1, 1, 0);
		enderCore.addBlock(blackGlass, 0, 1, 1);
		enderCore.addBlock(blackGlass, 0, 1, -1);
		enderCore.addBlock(blackGlass, -1, 1, 0);

		enderCore.addBlock(obsidian, -1, 2, -1);
		enderCore.addBlock(obsidian, 1, 2, -1);
		enderCore.addBlock(obsidian, -1, 2, 1);
		enderCore.addBlock(obsidian, 1, 2, 1);

		enderCore.addBlock(obsidian, 1, 0, 0);
		enderCore.addBlock(obsidian, 0, 0, 1);
		enderCore.addBlock(obsidian, 0, 0, -1);
		enderCore.addBlock(obsidian, -1, 0, 0);

		enderCore.addBlock(obsidian, -1, 1, -1);
		enderCore.addBlock(obsidian, 1, 1, -1);
		enderCore.addBlock(obsidian, -1, 1, 1);
		enderCore.addBlock(obsidian, 1, 1, 1);

		enderCore.addBlock(obsidian, 1, 2, 0);
		enderCore.addBlock(obsidian, 0, 2, 1);
		enderCore.addBlock(obsidian, 0, 2, -1);
		enderCore.addBlock(obsidian, -1, 2, 0);

		IBlockState jungleLog = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
		IBlockState jungleLeaves = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(BlockLeaves.DECAYABLE, false);

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
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		if (Worldgen.cores)
		{
			if (world.getWorldType() != WorldType.DEBUG_WORLD)
			{
				if (world.provider.getDimensionId() == 0)
				{
					int x = chunkX * 16 + random.nextInt(16);
					int z = chunkZ * 16 + random.nextInt(16);
					BlockPos target = world.getTopSolidOrLiquidBlock(new BlockPos(x, 40, z));
					BiomeGenBase biome = world.getBiomeGenForCoords(target);

					int enderMult = 40;
					if (BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL))
					{
						enderMult -= 8;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.SPOOKY))
					{
						enderMult -= 4;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.SWAMP))
					{
						enderMult -= 8;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
					{
						enderMult += 10;
					}

					int natureMult = 30;
					if (BiomeDictionary.isBiomeOfType(biome, Type.DENSE))
					{
						natureMult -= 8;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.SPARSE))
					{
						natureMult += 4;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.WET))
					{
						natureMult -= 4;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.DRY))
					{
						natureMult += 2;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.DEAD))
					{
						natureMult += 10;
					}
					if (BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL))
					{
						natureMult -= 8;
					}

					boolean generateNatureCore = random.nextInt(18 * natureMult) == 0;
					boolean generateEnderCore = random.nextInt(18 * enderMult) == 0;

					if (generateNatureCore || generateEnderCore)
					{
						boolean canPlaceCore = true;
						for (int modX = -2; modX < 3; modX++)
						{
							for (int modZ = -2; modZ < 3; modZ++)
							{
								for (int modY = 0; modY < 3; modY++)
								{
									BlockPos check = new BlockPos(target.getX() + modX, target.getY() + modY, target.getZ() + modZ);
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
							if (generateEnderCore)
							{
								placeEnderCore(random, world, target);
							}
							else if (generateNatureCore)
							{
								placeNatureCore(random, world, target);
							}
						}
					}
				}
			}
		}
	}

	private void placeEnderCore(Random random, World world, BlockPos target)
	{
		enderCore.place(world, target, 2);

		for (int i = 0; i < 10; i++)
		{
			int rX = random.nextInt(10) - 5;
			int rY = random.nextInt(2) - 1;
			int rZ = random.nextInt(10) - 5;

			BlockPos pos = target.add(rX, rY, rZ);
			if (!world.isAirBlock(pos) && (Math.abs(rX) > 1 || Math.abs(rZ) > 1))
			{
				world.setBlockState(pos, Blocks.obsidian.getDefaultState());
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

					te.setInventorySlotContents(0, new ItemStack(ModItems.beans, random.nextInt(2) + 1, 2));
					if (random.nextBoolean())
						te.setInventorySlotContents(1, new ItemStack(ModItems.beans, random.nextInt(20) + 5));
					if (random.nextBoolean())
						te.setInventorySlotContents(2, new ItemStack(Items.wheat, random.nextInt(20) + 5));
					if (random.nextBoolean())
						te.setInventorySlotContents(3, new ItemStack(Items.wheat_seeds, random.nextInt(20) + 5));
					if (random.nextBoolean())
						te.setInventorySlotContents(4, new ItemStack(Items.pumpkin_seeds, random.nextInt(15) + 2));
					if (random.nextBoolean())
						te.setInventorySlotContents(5, new ItemStack(Items.melon_seeds, random.nextInt(15) + 2));
					if (random.nextBoolean())
						te.setInventorySlotContents(6, new ItemStack(Items.reeds, random.nextInt(20) + 5));
					if (random.nextBoolean())
						te.setInventorySlotContents(7, new ItemStack(Items.potato, random.nextInt(20) + 5));
					if (random.nextBoolean())
						te.setInventorySlotContents(8, new ItemStack(Items.carrot, random.nextInt(20) + 1));
					if (random.nextBoolean())
					{
						te.setInventorySlotContents(9, new ItemStack(Blocks.sapling, random.nextInt(20) + 1, random.nextInt(6)));
						if (random.nextBoolean())
							te.setInventorySlotContents(10, new ItemStack(Blocks.sapling, random.nextInt(20) + 1, random.nextInt(6)));
					}
					if (random.nextBoolean())
						te.setInventorySlotContents(11, new ItemStack(Blocks.vine, random.nextInt(20) + 1));
					if (random.nextBoolean())
						te.setInventorySlotContents(12, new ItemStack(Blocks.red_flower, random.nextInt(20) + 1, random.nextInt(9)));

					InventoryUtil.shuffleInventory(te);

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