package lumien.randomthings.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTriggerGlass extends BlockBase
{
	public static PropertyBool TRIGGERED = PropertyBool.create("triggered");

	protected BlockTriggerGlass()
	{
		super("triggerGlass", Material.GROUND);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3f);

		this.setDefaultState(this.blockState.getBaseState().withProperty(TRIGGERED, false));
	}

	@Override
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
	{
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (!worldIn.isRemote && !state.getValue(TRIGGERED))
		{
			boolean powered = worldIn.isBlockIndirectlyGettingPowered(pos) > 0;

			IBlockState fromState = worldIn.getBlockState(fromPos);

			if (powered || fromState.getBlock() == this && fromState.getValue(TRIGGERED))
			{
				worldIn.setBlockState(pos, state.withProperty(TRIGGERED, true));
				worldIn.scheduleUpdate(pos, this, 60);
			}
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote && state.getValue(TRIGGERED))
		{
			worldIn.setBlockState(pos, state.withProperty(TRIGGERED, false));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TRIGGERED) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TRIGGERED, meta == 0 ? false : true);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { TRIGGERED });
	}

	@Override
	public Material getMaterial(IBlockState state)
	{
		return Material.GROUND;
	}

	@Override
	public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState)
	{
		if (!state.getValue(TRIGGERED))
		{
			super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (state != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return !state.getValue(TRIGGERED);
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
}
