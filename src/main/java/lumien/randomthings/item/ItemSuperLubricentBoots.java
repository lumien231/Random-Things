package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemSuperLubricentBoots extends ItemArmor
{
	public ItemSuperLubricentBoots()
	{
		super(ItemArmor.ArmorMaterial.IRON, 0, EntityEquipmentSlot.FEET);

		ItemBase.registerItem("superLubricentBoots", this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		return "randomthings:textures/models/armor/superLubricentBoots.png";
	}
}
