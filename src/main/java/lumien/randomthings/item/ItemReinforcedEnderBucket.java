package lumien.randomthings.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lumien.randomthings.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class ItemReinforcedEnderBucket extends ItemBase
{
	public ItemReinforcedEnderBucket()
	{
		super("reinforcedEnderBucket");

		this.setMaxStackSize(1);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new FluidHandlerItemStack(stack, 1000 * 10);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		FluidStack contained = item.drain(1000 * 10, false);

		if (contained != null)
		{
			Fluid f = contained.getFluid();

			if (f == FluidRegistry.WATER)
			{
				return Color.BLUE.getRGB();
			}
			else if (f == FluidRegistry.LAVA)
			{
				return Color.ORANGE.getRGB();
			}
			else if (f != null && f.getName() != null)
			{
				if (f.getName().equals("astralsorcery.liquidstarlight"))
				{
					return Color.WHITE.getRGB();
				}
			}
		}

		return super.getRGBDurabilityForDisplay(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		FluidStack contained = item.drain(1000 * 10, false);

		float filledPercent;
		if (contained == null)
		{
			filledPercent = 0;
		}
		else
		{
			filledPercent = contained.amount / (1000F * 10F);
		}

		return 1F - filledPercent;
	}

	@Override
	@Nonnull
	public String getItemStackDisplayName(@Nonnull ItemStack stack)
	{
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		FluidStack fluidStack = item.drain(1, false);
		if (fluidStack == null)
		{
			return super.getItemStackDisplayName(stack);
		}
		else
		{
			return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".filled.name", fluidStack.getLocalizedName());
		}
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);

		IFluidHandlerItem item = itemstack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		RayTraceResult mop = WorldUtil.rayTraceAll(world, player, true);

		if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState hitState = world.getBlockState(mop.getBlockPos());
			Block hitBlock = hitState.getBlock();

			boolean collectAll = player.isSneaking();

			if (hitBlock instanceof IFluidBlock || hitBlock instanceof BlockLiquid)
			{
				List<BlockPos> stack = new ArrayList<BlockPos>();
				Set<BlockPos> alreadyChecked = new HashSet<BlockPos>();

				stack.add(mop.getBlockPos());

				while (!stack.isEmpty())
				{
					BlockPos next = stack.remove(0);
					alreadyChecked.add(next);

					if (alreadyChecked.size() > 2000)
					{
						break;
					}

					if (world.isBlockLoaded(next))
					{
						IBlockState nextState = world.getBlockState(next);
						Block nextBlock = nextState.getBlock();

						if (nextBlock instanceof IFluidBlock || nextBlock instanceof BlockLiquid)
						{
							FluidActionResult pickupResult = FluidUtil.tryPickUpFluid(itemstack, player, world, next, EnumFacing.UP);

							if (pickupResult.success)
							{
								if (!collectAll)
								{
									return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, pickupResult.result);
								}
								else
								{
									itemstack = pickupResult.result;
								}
							}

							for (EnumFacing facing : EnumFacing.VALUES)
							{
								BlockPos addingPos = next.offset(facing);

								if (!alreadyChecked.contains(addingPos))
								{
									stack.add(addingPos);

									alreadyChecked.add(addingPos);
								}
							}
						}
					}
				}
			}
			else
			{
				FluidStack fluidStack = item.drain(1000, false);

				if (fluidStack != null && fluidStack.amount >= 1000)
				{
					BlockPos clickPos = mop.getBlockPos();

					if (world.isBlockModifiable(player, clickPos))
					{
						BlockPos targetPos = clickPos.offset(mop.sideHit);

						if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack))
						{
							FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
							if (result.isSuccess())
							{
								// success!
								player.addStat(StatList.getObjectUseStats(this));

								ItemStack drained = result.getResult();

								return ActionResult.newResult(EnumActionResult.SUCCESS, drained);
							}
						}
					}
				}

				// couldn't place liquid there2
				return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
	}
}