package lumien.randomthings.item;

import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.lib.IEntityFilterItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemIDCard extends ItemBase implements IEntityFilterItem
{

	public ItemIDCard()
	{
		super("idcard");
	}

	@Override
	public String getHighlightTip(ItemStack stack, String displayName)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound compound = stack.getTagCompound();

			if (compound.hasKey("name"))
			{
				displayName += " (" + compound.getString("name") + ")";
			}
		}

		return displayName;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		if (stack.hasTagCompound())
		{
			NBTTagCompound compound = stack.getTagCompound();

			if (compound.hasKey("name"))
			{
				String name = compound.getString("name");

				tooltip.add(name);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack me = playerIn.getHeldItem(handIn);
		GameProfile playerProfile = playerIn.getGameProfile();

		if (playerProfile != null)
		{
			UUID uuid = playerProfile.getId();
			String name = playerProfile.getName();

			if (!me.hasTagCompound())
			{
				me.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = me.getTagCompound();

			compound.setString("uuid", uuid.toString());
			compound.setString("name", name != null ? name : "Anonymous");

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
		}
		else
		{
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}

	@Override
	public boolean apply(ItemStack me, Entity entity)
	{
		if (me.hasTagCompound())
		{
			NBTTagCompound compound = me.getTagCompound();

			if (compound.hasKey("uuid"))
			{
				UUID uuid = UUID.fromString(compound.getString("uuid"));

				if (entity instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entity;

					if (uuid.equals(player.getGameProfile().getId()))
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		return entity instanceof EntityPlayer;
	}

	public static UUID getUUID(ItemStack card)
	{
		if (card.hasTagCompound())
		{
			NBTTagCompound compound = card.getTagCompound();

			if (compound.hasKey("uuid"))
			{
				return UUID.fromString(compound.getString("uuid"));
			}
		}
		return null;
	}
}
