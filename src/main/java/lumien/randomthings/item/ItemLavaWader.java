package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemLavaWader extends ItemArmor
{
	public ItemLavaWader()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, EntityEquipmentSlot.FEET);
		ItemBase.registerItem("lavaWader", this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
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
		if (!player.world.isRemote)
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
