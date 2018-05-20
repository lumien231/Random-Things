package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.tileentity.TileEntityRainShield;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRainShield extends BlockContainerBase
{
	protected static final AxisAlignedBB RAINSHIELD_AABB = new AxisAlignedBB(6F / 16F, 0.0F, 6F / 16F, 10F / 16F, 1.0F, 10F / 16F);

	protected BlockRainShield()
	{
		super("rainShield", Material.ROCK);

		this.setHardness(2.0F);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return RAINSHIELD_AABB;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.checkForDrop(worldIn, pos, state);

		TileEntityRainShield te = (TileEntityRainShield) worldIn.getTileEntity(pos);
		te.onBlockAdded(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityRainShield();
	}

	@SideOnly(Side.CLIENT)
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
	public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
	{
		super.randomDisplayTick(state, worldIn, pos, rand);

		if (worldIn.isRaining())
		{
			for (double mod = 0; mod < 1; mod += 0.1f)
			{
				for (double a = 0; a <= Math.PI * 2D; a += (Math.PI * 2D) / 3D)
				{
					double x = pos.getX() + 0.5 + (1 - mod) * Math.cos(a);
					double z = pos.getZ() + 0.5 + (1 - mod) * Math.sin(a);

					worldIn.spawnParticle(EnumParticleTypes.FLAME, x, pos.getY() + 0.7f + mod, z, 0, 0, 0);
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, pos.getY() + 0.6f + mod, z, 0, 0, 0);
				}
			}
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		TileEntity te;
		if ((te = worldIn.getTileEntity(pos)) instanceof TileEntityRainShield)
		{
			((TileEntityRainShield) te).neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		}

		checkForDrop(worldIn, pos, state);
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
}
