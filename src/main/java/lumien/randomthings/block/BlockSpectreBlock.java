package lumien.randomthings.block;

import lumien.randomthings.lib.IExplosionImmune;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectreBlock extends BlockBase implements IExplosionImmune
{
	public BlockSpectreBlock()
	{
		super("spectreBlock", Material.ROCK);

		this.setBlockUnbreakable();
		this.setSoundType(SoundType.GLASS);
		this.blockResistance = Float.MAX_VALUE - 1000f;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return blockResistance;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (block == this || iblockstate.getBlock() == ModBlocks.spectreCore)
		{
			return false;
		}

		if (state != iblockstate)
		{
			return true;
		}

		return false;
	}
}
