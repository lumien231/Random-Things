package lumien.randomthings.items;

import lumien.randomthings.RandomThings;
import lumien.randomthings.blocks.ModBlocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;

public class ModItems
{

	public static void registerItems(Register<Item> itemRegistryEvent)
	{
		// ItemBlocks
		ModBlocks.itemBlocksToRegister.forEach((blockInstance) -> {
			Item itemInstance = new BlockItem(blockInstance, new Item.Properties().group(RandomThings.INSTANCE.getItemGroup()));
			itemInstance.setRegistryName(blockInstance.getRegistryName());
			itemRegistryEvent.getRegistry().register(itemInstance);
		});
		ModBlocks.itemBlocksToRegister.clear();
	}

}
