package lumien.randomthings.item.spectretools;

import lumien.randomthings.item.ItemBase;
import lumien.randomthings.item.ModItems;
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
