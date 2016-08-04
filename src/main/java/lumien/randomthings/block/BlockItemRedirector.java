package lumien.randomthings.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemRedirector extends BlockBase
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.5D, 0.7D);

	public static final PropertyDirection INPUT_FACING = PropertyDirection.create("inputfacing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyDirection OUTPUT_FACING = PropertyDirection.create("outputfacing", EnumFacing.Plane.HORIZONTAL);

	protected BlockItemRedirector()
	{
		super("itemRedirector", Material.ROCK);

		this.setHardness(2.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(INPUT_FACING, EnumFacing.NORTH).withProperty(OUTPUT_FACING, EnumFacing.SOUTH));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		EnumFacing currentInput = state.getValue(INPUT_FACING);
		EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

		return (currentInput.ordinal() - 2) + (currentOutput.ordinal() - 2) * 4;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing output = EnumFacing.values()[(meta / 4) + 2];
		EnumFacing input = EnumFacing.values()[meta - (output.ordinal() - 2) * 4 + 2];
		return this.getDefaultState().withProperty(INPUT_FACING, input).withProperty(OUTPUT_FACING, output);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { INPUT_FACING, OUTPUT_FACING });
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (entityIn instanceof EntityItem)
		{
			Vec3d motionVec = new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ).subtract(new Vec3d(entityIn.lastTickPosX, entityIn.lastTickPosY, entityIn.lastTickPosZ));

			Vec3d center = new Vec3d(pos).addVector(0.5, 0, 0.5);
			Vec3d difVec = center.subtract(entityIn.getPositionVector());

			EnumFacing facing = EnumFacing.getFacingFromVector((float) difVec.xCoord, (float) difVec.yCoord, (float) difVec.zCoord).getOpposite();

			EnumFacing currentInput = state.getValue(INPUT_FACING);
			EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

			if (facing == currentInput)
			{
				Vec3d facingVec = new Vec3d(currentOutput.getDirectionVec()).scale(0.4).add(center);

				float dif = facing.getOpposite().getHorizontalAngle() - currentOutput.getHorizontalAngle();

				Vec3d outputMotionVec = motionVec.rotateYaw((float) Math.toRadians(dif));
				entityIn.setPosition(facingVec.xCoord, facingVec.yCoord, facingVec.zCoord);

				entityIn.motionX = outputMotionVec.xCoord;
				entityIn.motionY = outputMotionVec.yCoord;
				entityIn.motionZ = outputMotionVec.zCoord;
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side != EnumFacing.UP && side != EnumFacing.DOWN)
		{
			EnumFacing currentInput = state.getValue(INPUT_FACING);
			EnumFacing currentOutput = state.getValue(OUTPUT_FACING);

			if (currentInput != side && currentOutput != side)
			{
				if (!worldIn.isRemote)
				{
					worldIn.setBlockState(pos, state.withProperty(OUTPUT_FACING, side));
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
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
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
