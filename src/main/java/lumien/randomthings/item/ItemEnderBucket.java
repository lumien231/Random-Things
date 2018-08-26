package lumien.randomthings.item;

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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemEnderBucket extends ItemBase
{
	public ItemEnderBucket()
	{
		super("enderBucket");

		this.setMaxStackSize(1);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		if (item == null)
		{
			return 16; // Fluid Handler isn't present during Capability Gathering and Astral Sorcery wants the stack size during it.
		}
		
		FluidStack fluidStack = item.drain(1000, false);

		if (fluidStack != null)
		{
			return 1;
		}
		else
		{
			return 16;
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new FluidHandlerItemStackSimple(stack, 1000);
	}

	@Nullable
	public FluidStack getFluid(@Nonnull ItemStack container)
	{
		return FluidStack.loadFluidStackFromNBT(container.getTagCompound());
	}

	@Override
	@Nonnull
	public String getItemStackDisplayName(@Nonnull ItemStack stack)
	{
		IFluidHandlerItem item = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);

		FluidStack fluidStack = item.drain(1000, false);
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

		boolean isFull = false;

		if (item != null)
		{
			FluidStack drain = item.drain(1000, false);

			if (drain != null && drain.amount == 1000)
			{
				isFull = true;
			}
		}

		if (isFull)
		{
			FluidStack fluidStack = item.drain(1000, false);

			// clicked on a block?
			RayTraceResult mop = this.rayTrace(world, player, false);

			if (mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
			{
				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
			}

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

			// couldn't place liquid there2
			return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
		}
		else
		{
			RayTraceResult mop = WorldUtil.rayTraceAll(world, player, true);

			if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				IBlockState hitState = world.getBlockState(mop.getBlockPos());
				Block hitBlock = hitState.getBlock();

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
									if (itemstack.getCount() > 1)
									{
										ItemStack returnStack = itemstack.copy();
										returnStack.shrink(1);
										
										ItemStack add = pickupResult.result;
										pickupResult.result.setCount(1);
										
						                if (!player.inventory.addItemStackToInventory(add))
						                {
						                    player.dropItem(add, false);
						                }
										
										return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, returnStack);
									}
									else
									{
										return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, pickupResult.result);
									}
								}
								else
								{
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
				}
			}
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
	}
}