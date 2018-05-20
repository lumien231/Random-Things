package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRedstoneActivator extends ItemBase
{
	int[] durations = new int[] { 2, 20, 100 };

	public ItemRedstoneActivator()
	{
		super("redstoneActivator");

		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);

		tooltip.add(I18n.format("tooltip.redstoneactivator.duration", durations[getDurationIndex(stack)]));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return !ItemStack.areItemStacksEqual(oldStack, newStack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack me = playerIn.getHeldItem(handIn);

		int currentDurationIndex = getDurationIndex(me);
		int nextDurationIndex;

		if (playerIn.isSneaking())
		{
			nextDurationIndex = currentDurationIndex - 1;
			nextDurationIndex = nextDurationIndex < 0 ? durations.length - 1 : nextDurationIndex;
		}
		else
		{
			nextDurationIndex = currentDurationIndex + 1;
			nextDurationIndex = nextDurationIndex >= durations.length ? 0 : nextDurationIndex;
		}

		setDurationIndex(me, nextDurationIndex);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			RedstoneSignalHandler.getHandler().addSignal(worldIn, pos, durations[getDurationIndex(stack)], 15);

			return EnumActionResult.SUCCESS;
		}
		else
		{
			((EntityPlayerSP) playerIn).connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, side, hand, hitX, hitY, hitZ));

			return EnumActionResult.SUCCESS;
		}
	}

	public int getDurationIndex(ItemStack stack)
	{
		NBTTagCompound compound;

		if ((compound = stack.getTagCompound()) != null && compound.hasKey("durationIndex"))
		{
			return compound.getInteger("durationIndex");
		}
		else
		{
			return 1;
		}
	}

	public void setDurationIndex(ItemStack stack, int index)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		stack.getTagCompound().setInteger("durationIndex", index);
	}
}
