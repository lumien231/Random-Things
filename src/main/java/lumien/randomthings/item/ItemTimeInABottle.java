package lumien.randomthings.item;

import java.util.List;
import java.util.Optional;

import lumien.randomthings.config.Numbers;
import lumien.randomthings.entitys.EntityTimeAccelerator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
			int secondWorth = Numbers.TIME_IN_A_BOTTLE_SECOND;
			
			if (secondWorth == 0 || worldIn.getTotalWorldTime() % secondWorth == 0)
			{
				NBTTagCompound timeData = stack.getOrCreateSubCompound("timeData");

				if (timeData.getInteger("storedTime") < 622080000)
				{
					timeData.setInteger("storedTime", timeData.getInteger("storedTime") + 20);
				}
			}

			if (worldIn.getTotalWorldTime() % 60 == 0)
			{
				if (entityIn instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entityIn;

					for (int i = 0; i < player.inventory.getSizeInventory(); i++)
					{
						ItemStack invStack = player.inventory.getStackInSlot(i);

						if (invStack.getItem() == this && invStack != stack)
						{
							NBTTagCompound otherTimeData = invStack.getOrCreateSubCompound("timeData");
							NBTTagCompound myTimeData = stack.getOrCreateSubCompound("timeData");

							int myTime = myTimeData.getInteger("storedTime");
							int theirTime = otherTimeData.getInteger("storedTime");
							
							if (myTime < theirTime)
							{
								myTimeData.setInteger("storedTime", 0);
							}
						}
					}
				}
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

						float pitch = 1;
						
						switch (nextRate)
						{
							case 2:
								world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 0.793701F);
								break;
							case 4:
								world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 0.890899F);
								break;
							case 8:
								world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 1.059463F);
								break;
							case 16:
								world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 0.943874F);
								break;
							case 32:
								world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 0.890899F);
								break;
						}
						
						// C# D E G F E
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

					world.playSound(null, pos, SoundEvents.BLOCK_NOTE_HARP, SoundCategory.BLOCKS, 0.5F, 0.749154F);
					world.spawnEntity(n);

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.SUCCESS;
	}
	
	public static int getStoredTime(ItemStack is)
	{
		NBTTagCompound timeData = is.getSubCompound("timeData");
		int timeAvailable = timeData.getInteger("storedTime");
		
		return timeAvailable;
	}
	
	public static void setStoredTime(ItemStack is, int time)
	{
		NBTTagCompound timeData = is.getSubCompound("timeData");
		timeData.setInteger("storedTime",time);
	}
}
