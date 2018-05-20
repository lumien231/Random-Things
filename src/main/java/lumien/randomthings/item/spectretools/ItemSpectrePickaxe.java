package lumien.randomthings.item.spectretools;

import java.util.UUID;

import com.google.common.collect.Multimap;

import lumien.randomthings.item.ItemBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;

public class ItemSpectrePickaxe extends ItemPickaxe
{
	public static UUID MOD_UUID = UUID.nameUUIDFromBytes("SoectreRangeModifier".getBytes());

	public ItemSpectrePickaxe()
	{
		super(ItemSpectreSword.spectreToolMaterial);

		ItemBase.registerItem("spectrePickaxe", this);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(MOD_UUID, "Spectre Range Modifier", 3, 0));
		}

		return multimap;
	}

}
