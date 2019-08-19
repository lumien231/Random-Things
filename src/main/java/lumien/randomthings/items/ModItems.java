package lumien.randomthings.items;

import lumien.randomthings.RandomThings;
import lumien.randomthings.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("randomthings")
public class ModItems
{
	@ObjectHolder("fertilized_dirt")
	public static Item FERTILIZED_DIRT;

	@ObjectHolder("block_of_sticks")
	public static Item BLOCK_OF_STICKS;

	@ObjectHolder("block_of_sticks_returning")
	public static Item BLOCK_OF_STICKS_RETURNING;

	@ObjectHolder("rainbow_lamp")
	public static Item RAINBOW_LAMP;


	public static ItemGroup RT_ITEM_GROUP;

	public static void registerItems(Register<Item> itemRegistryEvent)
	{
		IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();

		registerItemForBlock(registry, ModBlocks.FERTILIZED_DIRT);
		registerItemForBlock(registry, ModBlocks.RAINBOW_LAMP);
		registerItemForBlock(registry, ModBlocks.BLOCK_OF_STICKS, ModBlocks.BLOCK_OF_STICKS_RETURNING);
		registerItemForBlock(registry, ModBlocks.PLATFORM_OAK, ModBlocks.PLATFORM_SPRUCE, ModBlocks.PLATFORM_BIRCH, ModBlocks.PLATFORM_JUNGLE, ModBlocks.PLATFORM_ACACIA, ModBlocks.PLATFORM_DARKOAK);
	}

	private static void registerItemForBlock(IForgeRegistry<Item> registry, Block... blocks)
	{
		for (Block block : blocks)
		{
			Item itemInstance = new BlockItem(block, new Item.Properties().group(RT_ITEM_GROUP));
			itemInstance.setRegistryName(block.getRegistryName());
			registry.register(itemInstance);
		}
	}

	public static void initItemGroup()
	{
		RT_ITEM_GROUP = new ItemGroup("randomthings")
		{
			@Override
			public ItemStack createIcon()
			{
				return new ItemStack(ModBlocks.FERTILIZED_DIRT);
			}
		};
	}

}
