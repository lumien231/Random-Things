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

public class ItemMagicHood extends ItemArmor
{
	public ItemMagicHood()
	{
		super(ItemArmor.ArmorMaterial.CHAIN, 0, 0);
		ItemBase.registerItem("magicHood", this);
		
		ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, new SingleRandomChestContent(new ItemStack(this), 1, 1, 2));
		ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, new SingleRandomChestContent(new ItemStack(this), 1, 1, 5));
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "randomthings:textures/models/armor/magicHood.png";
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
