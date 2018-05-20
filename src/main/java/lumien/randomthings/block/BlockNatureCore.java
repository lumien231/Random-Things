package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.block.material.MaterialHardWood;
import lumien.randomthings.item.block.ItemBlockLuminous;
import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.tileentity.cores.TileEntityNatureCore;
import net.minecraft.block.SoundType;
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

public class BlockNatureCore extends BlockContainerBase implements ILuminousBlock
{

	protected BlockNatureCore()
	{
		super("natureCore", MaterialHardWood.HARD_WOOD, ItemBlockLuminous.class);

		this.setSoundType(SoundType.WOOD);
		this.setHarvestLevel("axe", 3);

		this.setHardness(25.0F).setResistance(2000.0F);
	}

	@Override
	public int getHarvestLevel(IBlockState state)
	{
		return super.getHarvestLevel(state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1;
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
		return true;
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return tintIndex == 0;
	}
}
