package lumien.randomthings.item.spectretools;

import lumien.randomthings.item.ItemBase;
import net.minecraft.item.ItemPickaxe;

public class ItemSpectrePickaxe extends ItemPickaxe
{

	public ItemSpectrePickaxe()
	{
		super(ItemSpectreSword.spectreToolMaterial);
		
		ItemBase.registerItem("spectrePickaxe", this);
	}

	

}
