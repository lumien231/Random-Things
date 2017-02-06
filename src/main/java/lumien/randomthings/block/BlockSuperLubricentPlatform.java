package lumien.randomthings.block;

import java.util.List;

import lumien.randomthings.lib.ISuperLubricent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSuperLubricentPlatform extends BlockBase implements ISuperLubricent
{
	protected static final AxisAlignedBB PLATFORM_AABB = new AxisAlignedBB(0, 14F / 16F, 0, 1, 1, 1);

	protected BlockSuperLubricentPlatform()
	{
		super("superLubricentPlatform", Material.ICE);

		this.setHardness(0.5F).setLightOpacity(3);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return PLATFORM_AABB;
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
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity, boolean p_185477_7_)
	{
		if (collidingEntity != null)
		{
			if (collidingEntity.posY < pos.getY() + 14F / 16F)
			{
				return;
			}

			if (collidingEntity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) collidingEntity;

				if (player.isSneaking() && player.motionY <= 0)
				{
					return;
				}
			}
		}
		else
		{
			return;
		}

		super.addCollisionBoxToList(state, worldIn, pos, mask, list, collidingEntity, p_185477_7_);
	}
}
