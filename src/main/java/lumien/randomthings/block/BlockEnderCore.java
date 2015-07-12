package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.tileentity.cores.TileEntityEnderCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnderCore extends BlockContainerBase
{
	protected BlockEnderCore()
	{
		super("enderCore", Material.rock);

		this.setBlockUnbreakable().setResistance(6000000.0F);
	}

	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityEnderCore();
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
	{
		return 210;
	}
}
