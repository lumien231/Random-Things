package lumien.randomthings.item;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemSoundPattern extends ItemBase
{

	public ItemSoundPattern()
	{
		super("soundPattern");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (playerIn.isSneaking())
		{
			ItemStack me = playerIn.getHeldItem(handIn);

			ResourceLocation sound = getSoundLocation(me);

			if (sound != null)
			{
				me.getTagCompound().removeTag("sound");
				me.setTagCompound(null);

				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
			}
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		NBTTagCompound compound;
		if (stack.hasTagCompound() && ((compound = stack.getTagCompound()).hasKey("sound")))
		{
			return super.getItemStackDisplayName(stack) + " <" + (new ResourceLocation(stack.getTagCompound().getString("sound")).getResourcePath()) + ">";
		}
		else
		{
			return super.getItemStackDisplayName(stack);
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound compound;
		if (stack.hasTagCompound() && ((compound = stack.getTagCompound()).hasKey("sound")))
		{
			tooltip.add(stack.getTagCompound().getString("sound"));
		}
		else
		{
			tooltip.add("<Empty>");
		}
	}

	public static ResourceLocation getSoundLocation(ItemStack pattern)
	{
		NBTTagCompound compound;
		if (pattern.hasTagCompound() && ((compound = pattern.getTagCompound()).hasKey("sound")))
		{
			return new ResourceLocation(pattern.getTagCompound().getString("sound"));
		}

		return null;
	}
}
