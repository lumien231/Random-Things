package lumien.randomthings.item.spectretools;

import java.util.Set;

import lumien.randomthings.item.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemTool;

public class ItemSpectrePickaxe extends ItemPickaxe
{

	public ItemSpectrePickaxe()
	{
		super(ItemSpectreSword.spectreToolMaterial);
		
		ItemBase.registerItem("spectrePickaxe", this);
	}

	

}
