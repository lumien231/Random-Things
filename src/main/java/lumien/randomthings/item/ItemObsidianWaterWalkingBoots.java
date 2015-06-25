package lumien.randomthings.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemObsidianWaterWalkingBoots extends ItemArmor
{
	public ItemObsidianWaterWalkingBoots()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, 3);
		ItemBase.registerItem("obsidianWaterWalkingBoots", this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
		return "randomthings:textures/models/armor/obsidianWaterWalkingBoots.png";
	}

	@Override
	public int getMaxDamage()
	{
		return 0;
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}
}
