package lumien.randomthings.block;

import lumien.randomthings.lib.properties.UnlistedBool;
import lumien.randomthings.tileentity.TileEntityFluidDisplay;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockFluidDisplay extends BlockContainerBase
{
	public static final FluidProperty FLUID = new FluidProperty();
	public static final UnlistedBool FLOWING = UnlistedBool.create("flowing");

	public BlockFluidDisplay()
	{
		super("fluidDisplay", Material.glass);

		this.setStepSound(soundTypeGlass);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityFluidDisplay te = (TileEntityFluidDisplay) worldIn.getTileEntity(pos);
		ItemStack currentItem = playerIn.getCurrentEquippedItem();

		if (currentItem != null)
		{
			FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(currentItem);
			if (liquid != null)
			{
				if (!worldIn.isRemote)
				{
					te.setFluidName(liquid.getFluid().getName());
					te.markDirty();
					worldIn.markBlockForUpdate(pos);
				}
				return true;
			}
		}
		else
		{
			if (!worldIn.isRemote)
			{
				te.toggleFlowing();
			}
			return true;
		}
		return false;
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { FLOWING, FLUID });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityFluidDisplay te = (TileEntityFluidDisplay) worldIn.getTileEntity(pos);
		IExtendedBlockState actualState = (IExtendedBlockState) state;

		return actualState.withProperty(FLUID, te.getFluid()).withProperty(FLOWING, te.flowing());
	}

	private static class FluidProperty implements IUnlistedProperty<String>
	{
		@Override
		public String getName()
		{
			return "fluid";
		}

		@Override
		public boolean isValid(String value)
		{
			return true;
		}

		@Override
		public Class<String> getType()
		{
			return String.class;
		}

		@Override
		public String valueToString(String value)
		{
			return value;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityFluidDisplay();
	}
}
