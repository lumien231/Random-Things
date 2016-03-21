package lumien.randomthings.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemSoundRecorder extends ItemBase
{
	public ItemSoundRecorder()
	{
		super("soundRecorder");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		NBTTagCompound stackCompound;
		if ((stackCompound = stack.getTagCompound()) != null)
		{
			if (stackCompound.hasKey("soundData"))
			{
				NBTTagCompound soundDataCompound = stackCompound.getCompoundTag("soundData");

				if (soundDataCompound.hasKey("soundList"))
				{
					NBTTagList soundList = soundDataCompound.getTagList("soundList", 8);

					for (int i = 0; i < soundList.tagCount(); i++)
					{
						tooltip.add(soundList.getStringTagAt(i));
					}
				}
			}
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return !ItemStack.areItemsEqual(oldStack, newStack);
	}
}
