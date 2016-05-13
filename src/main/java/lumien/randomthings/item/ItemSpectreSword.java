package lumien.randomthings.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSpectreSword extends ItemSword
{
	static ToolMaterial spectreToolMaterial = EnumHelper.addToolMaterial("spectre", 3, 2000, 8, 3, 22);
	{
		spectreToolMaterial.setRepairItem(new ItemStack(ModItems.ingredients,1,3));
	}

	public ItemSpectreSword()
	{
		super(spectreToolMaterial);
		
		ItemBase.registerItem("spectreSword", this);
	}

}
