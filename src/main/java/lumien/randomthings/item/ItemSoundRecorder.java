package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;

public class ItemSoundRecorder extends ItemBase
{
	public ItemSoundRecorder()
	{
		super("soundRecorder");
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null && compound.hasKey("recordList"))
		{
			NBTTagList recordList = compound.getTagList("recordList", 8);

			for (int i = 0; i < recordList.tagCount(); i++)
			{
				tooltip.add(recordList.getStringTagAt(i));
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			if (!itemStackIn.hasTagCompound())
			{
				itemStackIn.setTagCompound(new NBTTagCompound());
				
				itemStackIn.getTagCompound().setBoolean("recording", !itemStackIn.getTagCompound().getBoolean("recording"));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}

	public static void recordSound(ItemStack stack, PlaySoundAtEntityEvent event)
	{
		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null && compound.getBoolean("recording"))
		{
			NBTTagList recordList = compound.getTagList("recordList", 8);

			ResourceLocation soundName = event.getSound().getRegistryName();
			String soundString = soundName.toString();
			boolean duplicate = false;

			for (int i = 0; i < recordList.tagCount(); i++)
			{
				if (recordList.getStringTagAt(i).equals(soundString))
				{
					duplicate = true;
					break;
				}
			}
			
			if (!duplicate)
			{
				recordList.appendTag(new NBTTagString(soundString));
				
				if (recordList.tagCount() == 10)
				{
					compound.setBoolean("recording", false);
				}
				
				compound.setTag("recordList", recordList);
			}
		}
	}
}
