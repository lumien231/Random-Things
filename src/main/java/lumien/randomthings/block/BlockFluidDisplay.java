package lumien.randomthings.block;

import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.properties.UnlistedBool;
import lumien.randomthings.lib.properties.UnlistedEnum;
import lumien.randomthings.tileentity.TileEntityFluidDisplay;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class BlockFluidDisplay extends BlockContainerBase implements IRTBlockColor
{
	public static final FluidProperty FLUID = new FluidProperty();
	public static final UnlistedBool FLOWING = UnlistedBool.create("flowing");
	public static final UnlistedEnum<Rotation> ROTATION = UnlistedEnum.create("rotation", Rotation.class);

	public BlockFluidDisplay()
	{
		super("fluidDisplay", Material.GLASS);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(0.3F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityFluidDisplay te = (TileEntityFluidDisplay) worldIn.getTileEntity(pos);

		ItemStack heldItem = playerIn.getHeldItemMainhand();

		if (!heldItem.isEmpty() && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
		{
			IFluidHandlerItem handler = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

			IFluidTankProperties[] tanks = handler.getTankProperties();

			if (tanks.length > 0)
			{
				FluidStack liquid = tanks[0].getContents();

				if (liquid != null)
				{
					if (!worldIn.isRemote)
					{
						te.setFluidStack(new FluidStack(liquid.getFluid(), 1000, liquid.tag));
						te.syncTE();
					}
					return true;
				}
			}

		}
		else
		{
			if (!worldIn.isRemote)
			{
				if (playerIn.isSneaking())
				{
					te.cycleRotation();
				}
				else
				{
					te.toggleFlowing();
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] {}, new IUnlistedProperty[] { FLOWING, FLUID, ROTATION });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityFluidDisplay te = (TileEntityFluidDisplay) worldIn.getTileEntity(pos);

		IExtendedBlockState actualState = (IExtendedBlockState) state;
		if (te == null)
		{
			return actualState.withProperty(FLUID, null).withProperty(FLOWING, false);
		}

		return actualState.withProperty(FLUID, te.getFluidStack()).withProperty(FLOWING, te.flowing()).withProperty(ROTATION, te.getRotation());
	}

	private static class FluidProperty implements IUnlistedProperty<FluidStack>
	{
		@Override
		public String getName()
		{
			return "fluid";
		}

		@Override
		public boolean isValid(FluidStack value)
		{
			return true;
		}

		@Override
		public Class<FluidStack> getType()
		{
			return FluidStack.class;
		}

		@Override
		public String valueToString(FluidStack value)
		{
			return value.getLocalizedName();
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityFluidDisplay();
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
	{
		if (tintIndex == 0)
		{
			IExtendedBlockState extended = (IExtendedBlockState) state;
			FluidStack fluidStack = extended.getValue(FLUID);

			if (fluidStack != null)
			{
				return fluidStack.getFluid().getColor(fluidStack);
			}
		}
		return 0xFFFFFF;
	}
}
