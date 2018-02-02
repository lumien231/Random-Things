package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.config.Features;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPortKey extends ItemBase
{

	public ItemPortKey()
	{
		super("portKey");

		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound targetCompound = stack.getSubCompound("target");

		if (targetCompound != null)
		{
			if (!Features.HIDE_CORDS)
			{
				int dimension = targetCompound.getInteger("dimension");
				int posX = targetCompound.getInteger("posX");
				int posY = targetCompound.getInteger("posY");
				int posZ = targetCompound.getInteger("posZ");

				tooltip.add(I18n.format("item.portkey.x", posX));
				tooltip.add(I18n.format("item.portkey.y", posY));
				tooltip.add(I18n.format("item.portkey.z", posZ));
			}
			else
			{
				tooltip.add(I18n.format("item.portkey.set"));
			}
		}
		else
		{
			tooltip.add(I18n.format("item.portkey.notset"));
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!world.isRemote)
		{
			ItemStack me = player.getHeldItem(hand);

			NBTTagCompound target = me.getOrCreateSubCompound("target");

			target.setInteger("dimension", world.provider.getDimension());
			target.setInteger("posX", pos.getX());
			target.setInteger("posY", pos.getY());
			target.setInteger("posZ", pos.getZ());
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public String getHighlightTip(ItemStack item, String displayName)
	{
		NBTTagCompound target = item.getSubCompound("target");

		if (target != null && !Features.HIDE_CORDS)
		{
			int dimension = target.getInteger("dimension");
			int posX = target.getInteger("posX");
			int posY = target.getInteger("posY");
			int posZ = target.getInteger("posZ");

			displayName += String.format(" (%d , %d , %d)", posX, posY, posZ);
		}

		return displayName;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		stack.removeSubCompound("trueage");
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		NBTTagCompound ageCompound = entityItem.getItem().getOrCreateSubCompound("trueage");

		if (ageCompound.getInteger("value") == 0)
		{
			entityItem.setNoDespawn();
		}

		ageCompound.setInteger("value", ageCompound.getInteger("value") + 1);

		return false;
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound age = stack.getSubCompound("trueage");

		if (age != null)
		{
			return age.getInteger("value") < 100 || stack.getSubCompound("target") == null;
		}
		else
		{
			return true;
		}
	}
}
