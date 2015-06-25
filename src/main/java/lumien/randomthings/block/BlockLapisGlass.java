package lumien.randomthings.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLapisGlass extends BlockBase
{
	protected BlockLapisGlass()
	{
		super("lapisGlass", Material.ground);

		this.setStepSound(soundTypeGlass);
		this.setHardness(0.3f);
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		AxisAlignedBB axisalignedbb1 = this.getCollisionBoundingBox(worldIn, pos, state);

		if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1) && (collidingEntity instanceof EntityPlayer))
		{
			list.add(axisalignedbb1);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
		{
			return true;
		}

		if (block == this)
		{
			return false;
		}

		return block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isVisuallyOpaque()
	{
		return true;
	}
}
