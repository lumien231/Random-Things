package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.tileentity.cores.TileEntityNetherCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNetherCore extends BlockContainerBase
{
	protected BlockNetherCore()
	{
		super("netherCore", Material.rock);

		this.setBlockUnbreakable().setResistance(6000000.0F);
	}

	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityNetherCore();
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state,IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}
}
