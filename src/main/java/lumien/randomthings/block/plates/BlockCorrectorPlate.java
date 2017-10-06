package lumien.randomthings.block.plates;

import lumien.randomthings.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCorrectorPlate extends BlockBase
{
	protected static final AxisAlignedBB AABB = null;
	protected static final AxisAlignedBB VISUAL_AABB = new AxisAlignedBB(0D, 0.0D, 0D, 1D, 0.03125D, 1D);


	public BlockCorrectorPlate()
	{
		super("plate_corrector", Material.ROCK);
	}
	
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
		
		if (Math.abs(entityIn.motionX) < Math.abs(entityIn.motionZ))
		{
			if (entityIn.posX != pos.getX() + 0.5)
			{
				entityIn.setPositionAndUpdate(pos.getX() + 0.5, entityIn.posY, entityIn.posZ);
			}
			
			if (entityIn.motionX != 0)
			{
				entityIn.motionX = 0;
			}
		}
		else if (Math.abs(entityIn.motionX) > Math.abs(entityIn.motionZ))
		{
			if (entityIn.posZ != pos.getZ() + 0.5)
			{
				entityIn.setPositionAndUpdate(entityIn.posX, entityIn.posY, pos.getZ() + 0.5);
			}
			
			if (entityIn.motionZ != 0)
			{
				entityIn.motionZ = 0;
			}
		}
	}
}
