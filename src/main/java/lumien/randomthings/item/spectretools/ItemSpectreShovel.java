package lumien.randomthings.item.spectretools;

import com.google.common.collect.Multimap;

import lumien.randomthings.item.ItemBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSpade;

public class ItemSpectreShovel extends ItemSpade
{

	public ItemSpectreShovel()
	{
		super(ItemSpectreSword.spectreToolMaterial);

		ItemBase.registerItem("spectreShovel", this);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(ItemSpectrePickaxe.MOD_UUID, "Spectre Range Modifier", 3, 0));
		}

		return multimap;
	}
}
