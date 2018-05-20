package lumien.randomthings.item;

import java.util.ArrayList;
import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSoundRecorder extends ItemBase
{
	public ItemSoundRecorder()
	{
		super("soundRecorder");

		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			if (playerIn.isSneaking())
			{
				if (!itemStackIn.hasTagCompound())
				{
					itemStackIn.setTagCompound(new NBTTagCompound());
				}

				itemStackIn.getTagCompound().setBoolean("recording", !itemStackIn.getTagCompound().getBoolean("recording"));

				if (itemStackIn.getTagCompound().getBoolean("recording"))
				{
					itemStackIn.getTagCompound().removeTag("recordList");
				}
			}
			else
			{
				if (!itemStackIn.hasTagCompound() || !itemStackIn.getTagCompound().getBoolean("recording"))
				{
					playerIn.openGui(RandomThings.instance, GuiIds.SOUND_RECORDER, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
				}
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	public static void recordSound(ItemStack stack, String soundName)
	{
		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null && compound.getBoolean("recording"))
		{
			NBTTagList recordList = compound.getTagList("recordList", 8);

			boolean duplicate = false;

			for (int i = 0; i < recordList.tagCount(); i++)
			{
				if (recordList.getStringTagAt(i).equals(soundName))
				{
					duplicate = true;
					break;
				}
			}

			if (!duplicate)
			{
				recordList.appendTag(new NBTTagString(soundName));

				if (recordList.tagCount() >= 10)
				{
					compound.setBoolean("recording", false);
				}

				compound.setTag("recordList", recordList);
			}
		}
	}

	public static ArrayList<String> getRecordedSounds(ItemStack stack)
	{
		ArrayList<String> list = new ArrayList<String>();

		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null)
		{
			NBTTagList recordList = compound.getTagList("recordList", 8);

			for (int i = 0; i < recordList.tagCount(); i++)
			{
				list.add(recordList.getStringTagAt(i));
			}
		}

		return list;
	}
}
