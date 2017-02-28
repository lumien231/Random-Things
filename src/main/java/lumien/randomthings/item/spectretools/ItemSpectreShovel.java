package lumien.randomthings.item.spectretools;

import lumien.randomthings.item.ItemBase;
import net.minecraft.item.ItemSpade;

public class ItemSpectreShovel extends ItemSpade
{

	public ItemSpectreShovel()
	{
		super(ItemSpectreSword.spectreToolMaterial);
		
		ItemBase.registerItem("spectreShovel", this);
	}

}
