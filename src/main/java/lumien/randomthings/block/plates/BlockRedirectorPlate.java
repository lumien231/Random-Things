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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedirectorPlate extends BlockBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);

	public static final PropertyDirection INPUT_FACING = PropertyDirection.create("inputfacing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyDirection OUTPUT_FACING = PropertyDirection.create("outputfacing", EnumFacing.Plane.HORIZONTAL);

	public BlockRedirectorPlate()
	{
		super("plate_redirector", Material.GROUND);

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
	public int getMetaFromState(IBlockState state)
	{
		EnumFacing currentInput = state.getValue(INPUT_FACING);
		EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

		return (currentInput.ordinal() - 2) + (currentOutput.ordinal() - 2) * 4;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing output = EnumFacing.values()[(meta / 4) + 2];
		EnumFacing input = EnumFacing.values()[meta - (output.ordinal() - 2) * 4 + 2];
		return this.getDefaultState().withProperty(INPUT_FACING, input).withProperty(OUTPUT_FACING, output);
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { INPUT_FACING, OUTPUT_FACING });
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		Vec3d motionVec = new Vec3d(entityIn.motionX, entityIn.motionY, entityIn.motionZ);

		EnumFacing roughMovingFacing = EnumFacing.getFacingFromVector((float) motionVec.x, (float) motionVec.y, (float) motionVec.z).getOpposite();

		Vec3d center = new Vec3d(pos).addVector(0.5, 0, 0.5);
		Vec3d difVec = center.subtract(entityIn.getPositionVector());

		EnumFacing facing = EnumFacing.getFacingFromVector((float) difVec.x, (float) difVec.y, (float) difVec.z).getOpposite();

		EnumFacing currentInput = state.getValue(INPUT_FACING);
		EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

		EnumFacing outputFacing = null;
		if (facing == currentInput && roughMovingFacing == currentInput)
		{
			outputFacing = currentOutput;
		}
		else if (facing == currentOutput && roughMovingFacing == currentOutput)
		{
			outputFacing = currentInput;
		}

		if (outputFacing != null)
		{
			Vec3d facingVec = new Vec3d(outputFacing.getDirectionVec()).scale(0.4).add(center);

			float dif = facing.getOpposite().getHorizontalAngle() - outputFacing.getHorizontalAngle();

			Vec3d outputMotionVec = motionVec.rotateYaw((float) Math.toRadians(dif));
			entityIn.setPosition(facingVec.x, facingVec.y, facingVec.z);

			entityIn.motionX = outputMotionVec.x;
			entityIn.motionY = outputMotionVec.y;
			entityIn.motionZ = outputMotionVec.z;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side == EnumFacing.UP)
		{
			EnumFacing currentInput = state.getValue(INPUT_FACING);
			EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

			EnumFacing newOutput = EnumFacing.getFacingFromVector(hitX - 0.5F, 0, hitZ - 0.5F);

			if (currentInput != newOutput && currentOutput != newOutput)
			{
				if (!worldIn.isRemote)
				{
					worldIn.setBlockState(pos, state.withProperty(OUTPUT_FACING, newOutput));
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.setDefaultFacing(worldIn, pos, state);
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(INPUT_FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OUTPUT_FACING, placer.getHorizontalFacing());
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(INPUT_FACING);

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

			worldIn.setBlockState(pos, state.withProperty(INPUT_FACING, enumfacing).withProperty(OUTPUT_FACING, enumfacing.getOpposite()), 2);
		}
	}
}
