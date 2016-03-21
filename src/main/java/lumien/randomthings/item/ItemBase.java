package lumien.randomthings.item;

import lumien.randomthings.RandomThings;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item
{
	public ItemBase(String name)
	{
		registerItem(name, this);
		
		RandomThings.proxy.scheduleColor(this);
	}

	public static void registerItem(String name, Item item)
	{
		item.setCreativeTab(RandomThings.instance.creativeTab);
		item.setUnlocalizedName(name);

		GameRegistry.registerItem(item, name);
	}
}
