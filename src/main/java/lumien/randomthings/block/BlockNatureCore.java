package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.item.block.ItemBlockLuminous;
import lumien.randomthings.lib.ILuminous;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.tileentity.cores.TileEntityNatureCore;
import net.minecraft.block.SoundType;
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

public class BlockNatureCore extends BlockContainerBase implements ILuminous, IRTBlockColor
{

	protected BlockNatureCore()
	{
		super("natureCore", Material.ROCK, ItemBlockLuminous.class);

		this.setSoundType(SoundType.WOOD);
		this.setBlockUnbreakable().setResistance(6000000.0F);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 0;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityNatureCore();
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
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
	{
		return -2;
	}
}
