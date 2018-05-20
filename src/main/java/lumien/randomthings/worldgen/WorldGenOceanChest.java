package lumien.randomthings.worldgen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.BlockSpecialChest;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces.MonumentCoreRoom;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

public class WorldGenOceanChest
{
	static Random rng = new Random();
	static Method setBlockState;
	static
	{
		try
		{
			setBlockState = StructureComponent.class.getDeclaredMethod(MCPNames.method("func_175811_a"), World.class, IBlockState.class, int.class, int.class, int.class, StructureBoundingBox.class);
			setBlockState.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, MonumentCoreRoom coreRoom)
	{
		if (Worldgen.WATER_CHEST)
		{
			BlockPos blockpos = new BlockPos(getXWithOffset(6, 6, coreRoom, coreRoom.getBoundingBox()), getYWithOffset(1, coreRoom, coreRoom.getBoundingBox()), getZWithOffset(6, 6, coreRoom, coreRoom.getBoundingBox()));

			if (structureBoundingBoxIn.isVecInside(blockpos))
			{
				try
				{
					setBlockState.invoke(coreRoom, worldIn, ModBlocks.specialChest.getDefaultState().withProperty(BlockSpecialChest.FACING, EnumFacing.SOUTH), 6, 1, 6, structureBoundingBoxIn);
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}

				TileEntity te = worldIn.getTileEntity(blockpos);

				if (te != null && te instanceof TileEntitySpecialChest)
				{
					TileEntitySpecialChest chestTE = (TileEntitySpecialChest) te;
					chestTE.setChestType(1);

					ItemStack rareLoot = null;
					if (rng.nextBoolean())
					{
						rareLoot = new ItemStack(ModItems.bottleOfAir);
					}
					else
					{
						rareLoot = new ItemStack(ModItems.waterWalkingBoots);
					}

					LootTable lootTable = worldIn.getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_JUNGLE_TEMPLE);

					LootContext.Builder builder = new LootContext.Builder((WorldServer) worldIn);
					lootTable.fillInventory(chestTE, rng, builder.build());

					chestTE.setInventorySlotContents(0, rareLoot);
				}
			}
		}
	}

	private static int getXWithOffset(int x, int z, StructureComponent comp, StructureBoundingBox boundingBox)
	{
		EnumFacing enumfacing = comp.getCoordBaseMode();

		if (enumfacing == null)
		{
			return x;
		}
		else
		{
			switch (enumfacing)
			{
			case NORTH:
			case SOUTH:
				return boundingBox.minX + x;
			case WEST:
				return boundingBox.maxX - z;
			case EAST:
				return boundingBox.minX + z;
			default:
				return x;
			}
		}
	}

	private static int getYWithOffset(int y, StructureComponent comp, StructureBoundingBox boundingBox)
	{
		return comp.getCoordBaseMode() == null ? y : y + boundingBox.minY;
	}

	private static int getZWithOffset(int x, int z, StructureComponent comp, StructureBoundingBox boundingBox)
	{
		EnumFacing enumfacing = comp.getCoordBaseMode();

		if (enumfacing == null)
		{
			return z;
		}
		else
		{
			switch (enumfacing)
			{
			case NORTH:
				return boundingBox.maxZ - z;
			case SOUTH:
				return boundingBox.minZ + z;
			case WEST:
			case EAST:
				return boundingBox.minZ + x;
			default:
				return z;
			}
		}
	}
}
