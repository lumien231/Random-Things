package lumien.randomthings.item;

import lumien.randomthings.worldgen.SingleRandomChestContent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class ItemWaterWalkingBoots extends ItemArmor
{
	public ItemWaterWalkingBoots()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, 3);
		ItemBase.registerItem("waterWalkingBoots", this);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "randomthings:textures/models/armor/waterWalkingBoots.png";
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
