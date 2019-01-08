package lumien.randomthings.handler.compability.jei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import akka.io.Tcp.Register;
import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockBase;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.enchantment.ModEnchantments;
import lumien.randomthings.item.ItemBase;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.diviningrod.ItemDiviningRod;
import mezz.jei.api.IModRegistry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class DescriptionHandler
{
	static IModRegistry registry;

	public static void addDescriptions(IModRegistry registry)
	{
		DescriptionHandler.registry = registry;

		Map<Object, String> overrideMap = new HashMap<>();
		overrideMap.put(ModBlocks.customWorkbench, "tile.customWorkbench.info");
		overrideMap.put(ModBlocks.specialChest, null);
		overrideMap.put(ModItems.rezStone, null);
		overrideMap.put(ModBlocks.platform, "tile.platform.info");
		overrideMap.put(ModBlocks.contactLever, "tile.contactButton.info");
		overrideMap.put(ModBlocks.luminousBlock, "tile.luminousBlock.info");
		overrideMap.put(ModBlocks.translucentLuminousBlock, "tile.translucentLuminousBlock.info");
		overrideMap.put(ModBlocks.biomeStone, "tile.biomeStone.info");
		overrideMap.put(ModBlocks.coloredGrass, "tile.coloredGrass.info");
		overrideMap.put(ModBlocks.stainedBrick, "tile.stainedBrick.info");
		overrideMap.put(ModBlocks.luminousStainedBrick, "tile.luminousStainedBrick.info");
		overrideMap.put(ModItems.grassSeeds, "item.grassSeeds.info");
		overrideMap.put(ModItems.runeDust, "item.runeDust.info");
		overrideMap.put(ModBlocks.ancientBrick, null);

		List<ItemStack> stackBlackList = new ArrayList<>();
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.BIOME_SENSOR.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.EVIL_TEAR.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SUPERLUBRICENT_TINCTURE.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.FLOO_POWDER.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.PLATE_BASE.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.PRECIOUS_EMERALD.id));
		stackBlackList.add(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SPECTRE_STRING.id));

		for (int i = 8; i < ItemDiviningRod.types.size(); i++)
		{
			stackBlackList.add(new ItemStack(ModItems.diviningRod, 1, i));
			registry.addDescription(new ItemStack(ModItems.diviningRod, 1, i), "item.diviningRod.general.info");
		}


		removeDes(overrideMap, ModBlocks.spectreLeaf, ModBlocks.natureCore, ModBlocks.spectreLog, ModBlocks.spectrePlank, ModBlocks.specialChest, ModBlocks.superLubricentPlatform, ModBlocks.filteredSuperLubricentPlatform);


		// Manually Add
		registry.addDescription(new ItemStack(ModBlocks.blockDiaphanous, 1, OreDictionary.WILDCARD_VALUE), "tile.diaphanousBlock.info");
		registry.addDescription(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ModEnchantments.magnetic, 1)), "enchantment.randomthings.magnetic.desc");

		Stream.concat(BlockBase.rtBlockList.stream(), ItemBase.rtItemList.stream()).forEach(new Consumer<Object>()
		{
			@Override
			public void accept(Object t)
			{
				NonNullList<ItemStack> subItems = NonNullList.create();
				if (t instanceof Item)
				{
					Item item = (Item) t;

					if (item.getCreativeTab() == RandomThings.instance.creativeTab)
					{
						item.getSubItems(RandomThings.instance.creativeTab, subItems);
					}
				}
				else if (t instanceof Block)
				{
					Block block = (Block) t;

					if (block.getCreativeTabToDisplayOn() == RandomThings.instance.creativeTab)
					{
						block.getSubBlocks(RandomThings.instance.creativeTab, subItems);
					}
				}

				if (!subItems.isEmpty())
				{
					if (overrideMap.containsKey(t))
					{
						String override = overrideMap.get(t);

						if (override != null)
						{
							if (t instanceof Block)
							{
								registry.addDescription(new ItemStack((Block) t, 1, OreDictionary.WILDCARD_VALUE), override);
							}
							else if (t instanceof Item)
							{
								registry.addDescription(new ItemStack((Item) t, 1, OreDictionary.WILDCARD_VALUE), override);
							}
						}

						return;
					}
				}

				for (ItemStack is : subItems)
				{
					if (!is.isEmpty())
					{
						boolean blackListed = false;
						for (ItemStack b : stackBlackList)
						{
							if (ItemStack.areItemStacksEqual(b, is))
							{
								blackListed = true;
								break;
							}
						}

						if (!blackListed)
						{
							registry.addDescription(is, is.getUnlocalizedName() + ".info");
						}
					}
				}
			}
		});
	}

	private static void add(Item item, String key)
	{
		registry.addDescription(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), key);
	}

	private static void add(Block block, String key)
	{
		registry.addDescription(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), key);
	}

	private static void add(ItemStack stack, String key)
	{
		registry.addDescription(stack, key);
	}

	private static void removeDes(Map<Object, String> overrideMap, Object... toRemove)
	{
		for (Object o : toRemove)
		{
			overrideMap.put(o, null);
		}
	}
}
