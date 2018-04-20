package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (!worldIn.isRemote)
		{
			List<ResourceLocation> rlList = new ArrayList<ResourceLocation>(SoundEvent.REGISTRY.getKeys());

			ResourceLocation random = rlList.get(new Random().nextInt(rlList.size()));

			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("sound", random.toString());

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
		}
		else
		{
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
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
