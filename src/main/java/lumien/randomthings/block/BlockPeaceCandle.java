package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.client.particles.ParticlePeaceCandle;
import lumien.randomthings.tileentity.TileEntityPeaceCandle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPeaceCandle extends BlockContainerBase
{
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.3125F, 0F, 0.3125F, 0.6875F, 0.125F, 0.6875F);

	protected BlockPeaceCandle()
	{
		super("peaceCandle", Material.ROCK);

		this.setHardness(2);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityPeaceCandle();
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
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = pos.getX() + 0.5D;
		double d1 = pos.getY() + 0.3D;
		double d2 = pos.getZ() + 0.5D;
		double d3 = 0.22D;
		double d4 = 0.27D;

		Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePeaceCandle(worldIn, d0, d1, d2, 0.0D, 0.0D, 0.0D));
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.checkForDrop(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		super.neighborChanged(state, worldIn, pos, neighborBlock, changedPos);

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
