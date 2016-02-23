package lumien.randomthings.block;

import lumien.randomthings.lib.IExplosionImmune;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectreBlock extends BlockBase implements IExplosionImmune
{
	public BlockSpectreBlock()
	{
		super("spectreBlock", Material.rock);

		this.setBlockUnbreakable().setStepSound(soundTypeGlass);
		this.blockResistance = Float.MAX_VALUE - 1000f;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return blockResistance;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (block == this || iblockstate.getBlock() == ModBlocks.spectreCore)
		{
			return false;
		}

		if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate && iblockstate.getBlock() != ModBlocks.spectreCore)
		{
			return true;
		}

		return block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	}
}
