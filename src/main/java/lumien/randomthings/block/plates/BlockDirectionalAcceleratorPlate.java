package lumien.randomthings.block.plates;

import lumien.randomthings.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirectionalAcceleratorPlate extends BlockBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockDirectionalAcceleratorPlate()
	{
		super("plate_accelerator_directional", Material.GROUND);

		this.setHardness(0.3f);
		this.setSoundType(SoundType.STONE);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		checkForDrop(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		return worldIn.isSideSolid(pos, EnumFacing.UP);
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceOn(worldIn, pos.down()))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_)
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return VISUAL_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		EnumFacing acceleratingFacing = state.getValue(FACING).getOpposite();

		Vec3d accVector = new Vec3d(acceleratingFacing.getDirectionVec()).scale(0.1);

		Vec3d motionVec = new Vec3d(entityIn.motionX, entityIn.motionY, entityIn.motionZ);
		motionVec = motionVec.add(accVector);

		if (motionVec.lengthSquared() > 0.5 * 0.5)
		{
			motionVec = motionVec.normalize().scale(0.5);
		}

		entityIn.motionX = motionVec.x;
		entityIn.motionY = motionVec.y;
		entityIn.motionZ = motionVec.z;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumFacing currentInput = state.getValue(FACING);

		return currentInput.ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing input = EnumFacing.values()[meta];

		if (FACING.getAllowedValues().contains(input))
		{
			return this.getDefaultState().withProperty(FACING, input);
		}
		else
		{
			return this.getDefaultState();
		}
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}
}
