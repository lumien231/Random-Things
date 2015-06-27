package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityCustomWorkbench;
import lumien.randomthings.tileentity.TileEntityFluidDisplay;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomWorkbench extends BlockContainerBase
{
	public static final WoodStateProperty WOOD_STATE = new WoodStateProperty();

	public BlockCustomWorkbench()
	{
		super("customWorkbench", Material.wood);

		this.setHardness(2.5F).setStepSound(soundTypeWood);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCustomWorkbench();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { WOOD_STATE });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityCustomWorkbench te = (TileEntityCustomWorkbench) worldIn.getTileEntity(pos);
		IExtendedBlockState actualState = (IExtendedBlockState) state;

		return actualState.withProperty(WOOD_STATE, te.getWoodState());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		
	}

	private static class WoodStateProperty implements IUnlistedProperty<IBlockState>
	{
		@Override
		public String getName()
		{
			return "woodState";
		}

		@Override
		public boolean isValid(IBlockState value)
		{
			return true;
		}

		@Override
		public Class<IBlockState> getType()
		{
			return IBlockState.class;
		}

		@Override
		public String valueToString(IBlockState value)
		{
			return value.toString();
		}
	}
}
