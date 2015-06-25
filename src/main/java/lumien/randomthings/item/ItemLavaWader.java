package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemLavaWader extends ItemArmor
{
	public ItemLavaWader()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, 3);
		ItemBase.registerItem("lavaWader", this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "randomthings:textures/models/armor/lavaWader.png";
	}

	@Override
	public int getMaxDamage()
	{
		return 0;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		if (!player.worldObj.isRemote)
		{
			if (itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = itemStack.getTagCompound();

			int chargeCooldown = compound.getInteger("chargeCooldown");
			if (chargeCooldown > 0)
			{
				compound.setInteger("chargeCooldown", chargeCooldown - 1);
			}
			else
			{
				int charge = compound.getInteger("charge");
				if (charge < 200)
				{
					compound.setInteger("charge", charge + 1);
				}
			}
		}
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}
}
