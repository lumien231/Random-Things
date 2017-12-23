package lumien.randomthings.item.spectretools;

import com.google.common.collect.Multimap;

import lumien.randomthings.item.ItemBase;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSpectreSword extends ItemSword
{
	public static ToolMaterial spectreToolMaterial = EnumHelper.addToolMaterial("spectre", 3, 2000, 8, 3, 22);
	{
		spectreToolMaterial.setRepairItem(new ItemStack(ModItems.ingredients, 1, 3));
	}

	public ItemSpectreSword()
	{
		super(spectreToolMaterial);

		ItemBase.registerItem("spectreSword", this);
	}
}
