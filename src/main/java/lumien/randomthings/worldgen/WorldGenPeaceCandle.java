package lumien.randomthings.worldgen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Worldgen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Church;

public class WorldGenPeaceCandle
{
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

	public static void addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn, Church church)
	{
		if (Worldgen.PEACE_CANDLE)
		{
			BlockPos blockpos = new BlockPos(getXWithOffset(2, 2, church, church.getBoundingBox()), getYWithOffset(2, church, church.getBoundingBox()), getZWithOffset(7, 7, church, church.getBoundingBox()));

			if (structureBoundingBoxIn.isVecInside(blockpos) && randomIn.nextInt(3) == 0)
			{
				try
				{
					setBlockState.invoke(church, worldIn, ModBlocks.peaceCandle.getDefaultState(), 2, 2, 7, structureBoundingBoxIn);
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
