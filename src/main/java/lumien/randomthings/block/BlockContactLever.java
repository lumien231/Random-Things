package lumien.randomthings.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockContactLever extends BlockBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockContactLever()
	{
		super("contactLever", Material.rock);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (((Boolean) state.getValue(POWERED)).booleanValue())
		{
			this.notifyNeighbors(worldIn, pos, (EnumFacing) state.getValue(FACING));
		}

		super.breakBlock(worldIn, pos, state);
	}

	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		return ((Boolean) state.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		return ((Boolean) state.getValue(POWERED)).booleanValue() ? 15 : 0;
	}

	public boolean canProvidePower()
	{
		return true;
	}

	private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing)
	{
		worldIn.notifyNeighborsOfStateChange(pos, this);

		for (EnumFacing f : EnumFacing.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(f), this);
		}
	}

	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing;

		boolean powered = false;
		;
		int facing = meta;

		if (facing >= 6)
		{
			facing -= 6;
			powered = true;
		}

		return this.getDefaultState().withProperty(FACING, EnumFacing.values()[facing]).withProperty(POWERED, powered);
	}


	public int getMetaFromState(IBlockState state)
	{
		int i;

		i = state.getValue(FACING).ordinal();

		if (((Boolean) state.getValue(POWERED)).booleanValue())
		{
			i += 6;
		}

		return i;
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING, POWERED });
	}

	private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			boolean flag = worldIn.getBlockState(pos.north()).getBlock().isFullBlock();
			boolean flag1 = worldIn.getBlockState(pos.south()).getBlock().isFullBlock();

			if (enumfacing == EnumFacing.NORTH && flag && !flag1)
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag)
			{
				enumfacing = EnumFacing.NORTH;
			}
			else
			{
				boolean flag2 = worldIn.getBlockState(pos.west()).getBlock().isFullBlock();
				boolean flag3 = worldIn.getBlockState(pos.east()).getBlock().isFullBlock();

				if (enumfacing == EnumFacing.WEST && flag2 && !flag3)
				{
					enumfacing = EnumFacing.EAST;
				}
				else if (enumfacing == EnumFacing.EAST && flag3 && !flag2)
				{
					enumfacing = EnumFacing.WEST;
				}
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)), 2);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultDirection(worldIn, pos, state);
	}

	public void activate(World world, BlockPos pos, EnumFacing fromFacing)
	{
		IBlockState state = world.getBlockState(pos);
		state = state.cycleProperty(POWERED);
		world.setBlockState(pos, state, 3);
		this.notifyNeighbors(world, pos, fromFacing);
		world.playSoundEffect((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean) state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F);
		return;

	}
}
