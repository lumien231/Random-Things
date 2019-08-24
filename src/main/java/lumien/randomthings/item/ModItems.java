package lumien.randomthings.item;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraftforge.common.Tags;
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

		// Items
		registerDiviningRod(registry, "coal", new Color(20, 20, 20, 50), Tags.Blocks.ORES_COAL.getId().toString());
		registerDiviningRod(registry, "iron", new Color(211, 180, 159, 50), Tags.Blocks.ORES_IRON.getId().toString());
		registerDiviningRod(registry, "gold", new Color(246, 233, 80, 50), Tags.Blocks.ORES_GOLD.getId().toString());
		registerDiviningRod(registry, "lapis", new Color(5, 45, 150, 50), Tags.Blocks.ORES_LAPIS.getId().toString());
		registerDiviningRod(registry, "redstone", new Color(211, 1, 1, 50), Tags.Blocks.ORES_REDSTONE.getId().toString());
		registerDiviningRod(registry, "emerald", new Color(0, 220, 0, 50), Tags.Blocks.ORES_EMERALD.getId().toString());
		registerDiviningRod(registry, "diamond", new Color(87, 221, 229, 50), Tags.Blocks.ORES_DIAMOND.getId().toString());
		registerDiviningRod(registry, "vanilla", colorHolder.toArray(new Color[0]), tagHolder.toArray(new String[0]));

		tagHolder.clear();
		colorHolder.clear();

		// Item Blocks
		registerItemForBlock(registry, ModBlocks.FERTILIZED_DIRT);
		registerItemForBlock(registry, ModBlocks.RAINBOW_LAMP);
		registry.register(new WallOrFloorItem(ModBlocks.ADVANCED_REDSTONE_TORCH, ModBlocks.ADVANCED_WALL_REDSTONE_TORCH, new Item.Properties().group(RT_ITEM_GROUP)).setRegistryName(ModBlocks.ADVANCED_REDSTONE_TORCH.getRegistryName()));
		registerItemForBlock(registry, ModBlocks.BLOCK_OF_STICKS, ModBlocks.BLOCK_OF_STICKS_RETURNING);
		registerItemForBlock(registry, ModBlocks.PLATFORM_OAK, ModBlocks.PLATFORM_SPRUCE, ModBlocks.PLATFORM_BIRCH, ModBlocks.PLATFORM_JUNGLE, ModBlocks.PLATFORM_ACACIA, ModBlocks.PLATFORM_DARKOAK);
	}

	static ArrayList<Color> colorHolder = new ArrayList<Color>();
	static ArrayList<String> tagHolder = new ArrayList<String>();

	private static void registerDiviningRod(IForgeRegistry<Item> registry, String name, Color[] colors, String[] tags)
	{
		tagHolder.addAll(Arrays.asList(tags));
		colorHolder.addAll(Arrays.asList(colors));

		registry.register(new DiviningRodItem(new Item.Properties().group(RT_ITEM_GROUP), colors, tags).setRegistryName("divining_rod_" + name));
	}

	private static void registerDiviningRod(IForgeRegistry<Item> registry, String name, Color color, String tag)
	{
		registerDiviningRod(registry, name, new Color[] { color }, new String[] { tag });
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
