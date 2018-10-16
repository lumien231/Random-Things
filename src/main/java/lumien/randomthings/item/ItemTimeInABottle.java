package lumien.randomthings.item;

import java.util.List;
import java.util.Optional;

import lumien.randomthings.entitys.EntityTimeAccelerator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTimeInABottle extends ItemBase
{

	public ItemTimeInABottle()
	{
		super("timeInABottle");
		
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		NBTTagCompound timeData = stack.getOrCreateSubCompound("timeData");
		int storedTime = timeData.getInteger("storedTime");

		int storedSeconds = storedTime / 20;

		int hours = storedSeconds / 3600;
		int minutes = (storedSeconds % 3600) / 60;
		int seconds = storedSeconds % 60;

		tooltip.add(I18n.format("tooltip.timeInABottle", hours, minutes, seconds));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!worldIn.isRemote)
		{
			NBTTagCompound timeData = stack.getOrCreateSubCompound("timeData");

			if (timeData.getInteger("storedTime") < 622080000)
			{
				timeData.setInteger("storedTime", timeData.getInteger("storedTime") + 1);
			}
		}
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!world.isRemote)
		{
			ItemStack me = player.getHeldItem(hand);

			Optional<EntityTimeAccelerator> o = world.getEntitiesWithinAABB(EntityTimeAccelerator.class, new AxisAlignedBB(pos).shrink(0.2)).stream().findFirst();

			if (o.isPresent())
			{
				EntityTimeAccelerator eta = o.get();

				int currentRate = eta.getTimeRate();

				int usedUpTime = 20 * 30 - eta.getRemainingTime();

				if (currentRate < 32)
				{
					int nextRate = currentRate * 2;

					int timeRequired = nextRate / 2 * 20 * 30;

					NBTTagCompound timeData = me.getSubCompound("timeData");
					int timeAvailable = timeData.getInteger("storedTime");

					if (timeAvailable >= timeRequired || player.capabilities.isCreativeMode)
					{
						int timeAdded = (nextRate * usedUpTime - currentRate * usedUpTime) / nextRate;

						if (!player.capabilities.isCreativeMode)
							timeData.setInteger("storedTime", timeAvailable - timeRequired);

						eta.setTimeRate(nextRate);
						eta.setRemainingTime(eta.getRemainingTime() + timeAdded);

						return EnumActionResult.SUCCESS;
					}
				}
			}
			else
			{
				NBTTagCompound timeData = me.getSubCompound("timeData");
				int timeAvailable = timeData.getInteger("storedTime");

				if (timeAvailable >= 20 * 30 || player.capabilities.isCreativeMode)
				{
					if (!player.capabilities.isCreativeMode)
						timeData.setInteger("storedTime", timeAvailable - 20 * 30);

					EntityTimeAccelerator n = new EntityTimeAccelerator(world, pos, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

					n.setTimeRate(1);
					n.setRemainingTime(20 * 30);

					world.spawnEntity(n);

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}
}
