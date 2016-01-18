package lumien.randomthings.block;

import java.util.Random;

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

public class BlockContactButton extends BlockBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockContactButton()
	{
		super("contactButton", Material.rock);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
		this.setTickRandomly(true);
		this.setHardness(1.5F);
	}

	@Override
	public int tickRate(World worldIn)
	{
		return 20;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(POWERED).booleanValue())
		{
			this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		return state.getValue(POWERED).booleanValue() ? 15 : 0;
	}

	@Override
	public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		return state.getValue(POWERED).booleanValue() ? 15 : 0;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (state.getValue(POWERED).booleanValue())
			{
				worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
				this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
				worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
				worldIn.markBlockRangeForRenderUpdate(pos, pos);
			}
		}
	}

	private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing)
	{
		worldIn.notifyNeighborsOfStateChange(pos, this);

		for (EnumFacing f : EnumFacing.values())
		{
			worldIn.notifyNeighborsOfStateChange(pos.offset(f), this);
		}
	}

	@Override
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


	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i;

		i = state.getValue(FACING).ordinal();

		if (state.getValue(POWERED).booleanValue())
		{
			i += 6;
		}

		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING, POWERED });
	}

	private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			EnumFacing enumfacing = state.getValue(FACING);
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

	public void activate(World world, BlockPos pos,EnumFacing fromFacing)
	{
		IBlockState state = world.getBlockState(pos);
		if (state.getValue(POWERED).booleanValue())
		{
			return;
		}
		else
		{
			world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
			world.markBlockRangeForRenderUpdate(pos, pos);
			world.playSoundEffect(pos.offset(fromFacing).getX() + 0.5D, pos.offset(fromFacing).getY() + 0.5D, pos.offset(fromFacing).getZ() + 0.5D, "random.click", 0.3F, 0.6F);
			this.notifyNeighbors(world, pos, state.getValue(FACING));
			world.scheduleUpdate(pos, this, this.tickRate(world));
			return;
		}
	}
}
