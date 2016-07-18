package lumien.randomthings.recipes;

import org.apache.commons.lang3.ArrayUtils;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.recipes.anvil.AnvilRecipeHandler;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes
{
	public static String[] oreDictDyes = new String[16];
	static String[] dyes = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };

	public static void register()
	{
		RecipeSorter.register("randomthings:customWorkbenchRecipe", RecipeWorkbench.class, Category.SHAPED, "");

		final ItemStack stone = new ItemStack(Blocks.STONE);
		final ItemStack rottenFlesh = new ItemStack(Items.ROTTEN_FLESH);
		final ItemStack boneMeal = new ItemStack(Items.DYE, 1, 15);
		final ItemStack lapis = new ItemStack(Items.DYE, 1, 4);
		final ItemStack dirt = new ItemStack(Blocks.DIRT);
		final ItemStack lapisBlock = new ItemStack(Blocks.LAPIS_BLOCK);
		final ItemStack repeater = new ItemStack(Items.REPEATER);
		final ItemStack redstoneTorch = new ItemStack(Blocks.REDSTONE_TORCH);
		final ItemStack obsidian = new ItemStack(Blocks.OBSIDIAN);
		final ItemStack stableEnderpearl = new ItemStack(ModItems.stableEnderpearl);
		final ItemStack endStone = new ItemStack(Blocks.END_STONE);
		final ItemStack redstoneDust = new ItemStack(Items.REDSTONE);
		final ItemStack enderPearl = new ItemStack(Items.ENDER_PEARL);
		final ItemStack netherStar = new ItemStack(Items.NETHER_STAR);
		final ItemStack enderChest = new ItemStack(Blocks.ENDER_CHEST);
		final ItemStack redstoneLamp = new ItemStack(Blocks.REDSTONE_LAMP);
		final ItemStack blackWool = new ItemStack(Blocks.WOOL, 1, 15);
		final ItemStack cobblestone = new ItemStack(Blocks.COBBLESTONE);
		final ItemStack biomeCrystal = new ItemStack(ModItems.biomeCrystal);
		final ItemStack stoneBricks = new ItemStack(Blocks.STONEBRICK);
		final ItemStack crackedStoneBricks = new ItemStack(Blocks.STONEBRICK, 1, 2);
		final ItemStack chiseledStoneBricks = new ItemStack(Blocks.STONEBRICK, 1, 3);
		final ItemStack prismarineShard = new ItemStack(Items.PRISMARINE_SHARD);
		final ItemStack prismarineCrystal = new ItemStack(Items.PRISMARINE_CRYSTALS);
		final ItemStack bean = new ItemStack(ModItems.beans);
		final ItemStack wheat = new ItemStack(Items.WHEAT);
		final ItemStack bowl = new ItemStack(Items.BOWL);
		final ItemStack ghastTear = new ItemStack(Items.GHAST_TEAR);
		final ItemStack craftingTable = new ItemStack(Blocks.CRAFTING_TABLE);
		final ItemStack paper = new ItemStack(Items.PAPER);
		final ItemStack stick = new ItemStack(Items.STICK);
		final ItemStack vine = new ItemStack(Blocks.VINE);
		final ItemStack waterBottle = new ItemStack(Items.POTIONITEM);
		final ItemStack mossyCobblestone = new ItemStack(Blocks.MOSSY_COBBLESTONE);
		final ItemStack netherBrick = new ItemStack(Blocks.NETHER_BRICK);
		final ItemStack netherRack = new ItemStack(Blocks.NETHERRACK);
		final ItemStack coal = new ItemStack(Items.COAL);
		final ItemStack blazePowder = new ItemStack(Items.BLAZE_POWDER);
		final ItemStack flint = new ItemStack(Items.FLINT);
		final ItemStack spiderEye = new ItemStack(Items.SPIDER_EYE);
		final ItemStack redMushroom = new ItemStack(Blocks.RED_MUSHROOM);
		final ItemStack glowStone = new ItemStack(Items.GLOWSTONE_DUST);
		final ItemStack glisteringMelon = new ItemStack(Items.SPECKLED_MELON);
		final ItemStack witherSkull = new ItemStack(Items.SKULL, 1, 1);
		final ItemStack stonePressurePlate = new ItemStack(Blocks.STONE_PRESSURE_PLATE);
		final ItemStack quartz = new ItemStack(Items.QUARTZ);
		final ItemStack quartzBlock = new ItemStack(Blocks.QUARTZ_BLOCK);
		final ItemStack lever = new ItemStack(Blocks.LEVER);
		final ItemStack stoneButton = new ItemStack(Blocks.STONE_BUTTON);

		final ItemStack ectoPlasm = new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.ECTO_PLASM.id);
		final ItemStack spectreIngot = new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id);
		final ItemStack biomeSensor = new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.BIOME_SENSOR.id);

		for (int i = 0; i < oreDictDyes.length; i++)
		{
			oreDictDyes[i] = "dye" + dyes[i];
		}

		ArrayUtils.reverse(oreDictDyes);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.playerInterface), "oco", "ono", "oso", 'o', obsidian, 'c', enderChest, 's', stableEnderpearl, 'n', netherStar));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fertilizedDirt, 2, 0), "rbr", "bdb", "rbr", 'r', rottenFlesh, 'b', boneMeal, 'd', dirt));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.lapisGlass, 8, 0), "ggg", "glg", "ggg", 'g', "blockGlass", 'l', lapisBlock));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.onlineDetector, 1, 0), "srs", "rtr", "srs", 's', "stone", 'r', repeater, 't', "dyeBlue"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.chatDetector, 1, 0), "srs", "rtr", "srs", 's', "stone", 'r', repeater, 't', "dyeRed"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.enderBridge), "eee", "ers", "eee", 'e', endStone, 'r', redstoneDust, 's', stableEnderpearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.enderAnchor), "ooo", "oso", "ooo", 'o', obsidian, 's', stableEnderpearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.lapisLamp), "xlx", "lal", "xlx", 'l', lapis, 'a', redstoneLamp));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.dyeingMachine), "xgx", "rcb", "xwx", 'g', "dyeGreen", 'r', "dyeRed", 'b', "dyeBlue", 'w', blackWool, 'c', craftingTable));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.prismarineEnderBridge), "scs", "cbc", "scs", 's', prismarineShard, 'c', prismarineCrystal, 'b', new ItemStack(ModBlocks.enderBridge)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.basicRedstoneInterface), "iri", "rsr", "iri", 'i', "ingotIron", 'r', redstoneDust, 's', stableEnderpearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.lightRedirector), "pgp", "gxg", "pgp", 'g', "blockGlass", 'p', "plankWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.imbuingStation), "xwx", "vcv", "lel", 'w', Items.WATER_BUCKET, 'v', Blocks.VINE, 'c', Blocks.HARDENED_CLAY, 'l', Blocks.WATERLILY, 'e', Items.EMERALD));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.analogEmitter), "tir", "iii", "rit", 't', redstoneTorch, 'i', "ingotIron", 'r', redstoneDust));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fluidDisplay), "ggg", "gbg", "ggg", 'g', "blockGlassColorless", 'b', Items.GLASS_BOTTLE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.enderMailbox), "ehe", "iii", "xfx", 'e', enderPearl, 'h', Blocks.HOPPER, 'i', "ingotIron", 'f', Blocks.OAK_FENCE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.entityDetector), "srs", "epe", "srs", 's', "stone", 'r', redstoneTorch, 'e', enderPearl, 'p', stonePressurePlate));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.quartzLamp), "xqx", "qlq", "xqx", 'q', quartz, 'l', redstoneLamp));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.quartzGlass, 8, 0), "ggg", "gqg", "ggg", 'g', "blockGlass", 'q', quartzBlock));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.potionVaporizer), "ctc", "iui", "cfc", 'c', "cobblestone", 't', Blocks.IRON_TRAPDOOR, 'i', "ingotIron", 'u', Items.CAULDRON, 'f', Blocks.FURNACE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.voxelProjector), "rgb", "wlw", "www", 'r', "blockGlassRed", 'g', "blockGlassGreen", 'b', "blockGlassBlue", 'w', blackWool, 'l', redstoneLamp));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.rainShield), "xfx", "xbx", "nnn", 'f', flint, 'b', Items.BLAZE_ROD, 'n', netherRack));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockBreaker), "cpc", "crc", "ccc", 'c', "cobblestone", 'p', Items.IRON_PICKAXE, 'r', redstoneTorch));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.rainbowLamp), "xgx", "rlb", 'g', "dyeGreen", 'r', "dyeRed", 'b', "dyeBlue", 'l', redstoneLamp));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.advancedRedstoneInterface), "ror", "oio", "ror", 'r', "blockRedstone", 'o', obsidian, 'i', ModBlocks.basicRedstoneInterface));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemRedirector), "ooo", "ger", "olo", 'o', Blocks.OBSIDIAN, 'g', "dyeGreen", 'e', enderPearl, 'l', ModBlocks.superLubricentIce, 'r', "dyeRed"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemCorrector), "ili", "ccc", "ccc", 'c', "cobblestone", 'i', "ingotIron", 'l', ModBlocks.superLubricentIce));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.superLubricentIce, 16), "s", "i", "w", 's', "slimeball", 'i', Blocks.ICE, 'w', Items.WATER_BUCKET));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemSealer), "ala", "ccc", "ccc", 'c', "cobblestone", 'a', "gemLapis", 'l', ModBlocks.superLubricentIce));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemRejuvenator), "wlw", "ccc", "ccc", 'c', "cobblestone", 'w', Blocks.WOOL, 'l', ModBlocks.superLubricentIce));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.superLubricentPlatform, 6), "iii", "xex", 'i', ModBlocks.superLubricentIce, 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.filteredItemRedirector), "xpx", "xrx", "xsx", 'p', paper, 'r', ModBlocks.itemRedirector, 's', Items.STRING));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.filteredSuperLubricentPlatform), "xpx", "xrx", "xsx", 'p', paper, 'r', ModBlocks.superLubricentPlatform, 's', Items.STRING));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.redstoneObserver), "rqr", "qeq", "rqr", 'r', redstoneDust, 'q', Items.QUARTZ, 'e', Items.ENDER_EYE));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeRadar), "iii", "gsg", "iii", 'i', "ingotIron", 'g', "blockGlass",'s',biomeSensor));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.ironDropper), "iii","ixi","iri",'i',"ingotIron",'r',redstoneDust));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.itemCollector), "xex", "xhx", "ooo", 'e', enderPearl, 'h', Blocks.HOPPER, 'o', obsidian));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.advancedItemCollector), "xrx", "gig", 'r', redstoneTorch, 'g', glowStone, 'i', ModBlocks.itemCollector));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.contactButton), "sis", "sbs", "sss", 's', "stone", 'i', Blocks.IRON_BARS, 'b', stoneButton));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.contactLever), "sis", "sbs", "sss", 's', "stone", 'i', Blocks.IRON_BARS, 'b', lever));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.grassSeeds), Blocks.GRASS));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.stableEnderpearl), "olo", "lel", "olo", 'o', obsidian, 'l', lapis, 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.beans, 1, 1), "nnn", "nbn", "nnn", 'b', bean, 'n', "nuggetGold"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.beanStew, 1, 0), "xwx", "bbb", "xox", 'b', bean, 'w', wheat, 'o', bowl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.beanStew, 1, 0), "bbb", "xwx", "xox", 'b', bean, 'w', wheat, 'o', bowl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.positionFilter, 1, 0), "xdx", "dpd", "xdx", 'd', "dyePurple", 'p', paper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.redstoneTool), "xrx", "xsx", "xsx", 'r', redstoneDust, 's', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.obsidianSkull), "oro", "bwb", "oro", 'o', obsidian, 'r', Items.BLAZE_ROD, 'w', witherSkull, 'b', netherBrick));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.enderLetter), "xxx", "pep", "xpx", 'p', Items.PAPER, 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.entityFilter), "xdx", "dpd", "xdx", 'd', "dyeBlue", 'p', paper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemFilter), "xyx", "ypy", "xyx", 'y', "dyeYellow", 'p', paper));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spectreKey), "ixx", "ipx", "xxi", 'i', spectreIngot, 'p', stableEnderpearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.redstoneActivator), "iri", "iti", "iii", 'i', "ingotIron", 'r', "dustRedstone", 't', redstoneTorch));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.redstoneRemote), "aaa", "oso", "ooo", 'a', ModItems.redstoneActivator, 'o', Blocks.OBSIDIAN, 's', stableEnderpearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spectreAnchor), "xix", "iei", "iii", 'i', "ingotIron", 'e', ectoPlasm));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.spectreSword), "xsx", "xsx", "xox", 's', spectreIngot, 'o', obsidian));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldenCompass), "xgx","gcg","xgx",'g',"ingotGold",'c',Items.COMPASS));
		
		// Ingredients
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.EVIL_TEAR.id), "xsx", "xtx", "xex", 's', witherSkull, 't', ghastTear, 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id), "xlx", "xix", "xex", 'l', lapis, 'i', "ingotGold", 'e', ectoPlasm));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients, 9, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id), "ele", "eie", "eee", 'l', lapisBlock, 'i', "blockGold", 'e', ectoPlasm));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ingredients,1,ItemIngredient.INGREDIENT.BIOME_SENSOR.id), "iii","rci","iri",'i',"ingotIron",'r',redstoneDust,'c',ModItems.biomeCrystal));
		
		// Biome Blocks
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeStone, 16, 0), "ccc", "cbc", "ccc", 'c', cobblestone, 'b', biomeCrystal));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeStone, 16, 1), "sss", "sbs", "sss", 's', stone, 'b', biomeCrystal));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeStone, 16, 2), "rrr", "rbr", "rrr", 'r', stoneBricks, 'b', biomeCrystal));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeStone, 16, 3), "ccc", "cbc", "ccc", 'c', crackedStoneBricks, 'b', biomeCrystal));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeStone, 16, 4), "ccc", "cbc", "ccc", 'c', chiseledStoneBricks, 'b', biomeCrystal));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.biomeGlass, 16), "ggg", "gbg", "ggg", 'g', "blockGlassColorless", 'b', biomeCrystal));

		// Platforms
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 0), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 0), 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 1), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 1), 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 2), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 2), 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 3), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 3), 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 4), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 4), 'e', enderPearl));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.platform, 6, 5), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 5), 'e', enderPearl));

		GameRegistry.addRecipe(new RecipeWorkbench());

		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.spectrePlank, 4), ModBlocks.spectreLog);

		createDyeRecipes(new ItemStack(Blocks.BRICK_BLOCK), ModBlocks.stainedBrick);
		createGrassSeedsRecipes();

		// Imbuing Station
		ImbuingRecipeHandler.addRecipe(waterBottle, vine, boneMeal, cobblestone, mossyCobblestone);

		ImbuingRecipeHandler.addRecipe(coal, flint, blazePowder, waterBottle, new ItemStack(ModItems.imbue, 1, 0));
		ImbuingRecipeHandler.addRecipe(spiderEye, rottenFlesh, redMushroom, waterBottle, new ItemStack(ModItems.imbue, 1, 1));
		ImbuingRecipeHandler.addRecipe(new ItemStack(ModItems.beans, 1, 1), lapis, glowStone, waterBottle, new ItemStack(ModItems.imbue, 1, 2));
		ImbuingRecipeHandler.addRecipe(witherSkull, netherBrick, ghastTear, waterBottle, new ItemStack(ModItems.imbue, 1, 3));
		ImbuingRecipeHandler.addRecipe(new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SAKANADE_SPORES.id), vine, new ItemStack(Items.SLIME_BALL), waterBottle, new ItemStack(ModItems.imbue, 1, 4));


		// Anvil
		if (Loader.isModLoaded("Baubles"))
		{
			AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianSkull), new ItemStack(Items.FIRE_CHARGE), new ItemStack(ModItems.obsidianSkullRing), 3);
		}

		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkull), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkullRing), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianWaterWalkingBoots), new ItemStack(ModItems.lavaCharm), new ItemStack(ModItems.lavaWader), 15);

		// Spectre Anchor
		IRecipe anchorRecipe = new IRecipe()
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack anchor = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (is != null)
					{
						if (is.getItem() == ModItems.spectreAnchor)
						{
							if (anchor == null)
							{
								anchor = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (is.getMaxStackSize() != 1)
								{
									return false;
								}
								else
								{
									target = is;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return anchor != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("spectreAnchor"));
			}

			@Override
			public ItemStack[] getRemainingItems(InventoryCrafting inv)
			{
				ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

				return aitemstack;
			}

			@Override
			public int getRecipeSize()
			{
				return 2;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.spectreAnchor);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack anchor = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (is != null)
					{
						if (is.getItem() == ModItems.spectreAnchor)
						{
							anchor = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = target.copy();

				result.setTagInfo("spectreAnchor", new NBTTagByte((byte) 0));

				return result;
			}
		};
		
		RecipeSorter.register("spectreAnchor", anchorRecipe.getClass(),Category.SHAPELESS , "");
		GameRegistry.addRecipe(anchorRecipe);

		// Golden Compass
		IRecipe goldenCompassRecipe = 
		new IRecipe()
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (is != null)
					{
						if (is.getItem() == ModItems.goldenCompass)
						{
							if (compass == null)
							{
								compass = is;
							}
							else
							{
								return false;
							}
						}
						else
						{
							if (target == null)
							{
								if (is.getItem() == ModItems.positionFilter)
								{
									target = is;
								}
								else
								{
									return false;
								}
							}
							else
							{
								return false;
							}
						}
					}
				}
				return compass != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("spectreAnchor"));
			}

			@Override
			public ItemStack[] getRemainingItems(InventoryCrafting inv)
			{
				ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (is != null)
					{
						if (is.getItem() == ModItems.positionFilter)
						{
							aitemstack[i] = is.copy();
						}
					}
				}
				
				return aitemstack;
			}

			@Override
			public int getRecipeSize()
			{
				return 2;
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.goldenCompass);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (is != null)
					{
						if (is.getItem() == ModItems.goldenCompass)
						{
							compass = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = compass.copy();

				BlockPos pos = ItemPositionFilter.getPosition(target);

				if (pos != null)
				{
					if (result.getTagCompound() == null)
					{
						result.setTagCompound(new NBTTagCompound());
					}
					NBTTagCompound compound = result.getTagCompound();

					compound.setInteger("targetX", pos.getX());
					compound.setInteger("targetZ", pos.getZ());
				}
				return result;
			}
				};

		RecipeSorter.register("goldenCompass", goldenCompassRecipe.getClass(), Category.SHAPELESS, "");
		GameRegistry.addRecipe(goldenCompassRecipe);
	}

	private static void createGrassSeedsRecipes()
	{
		for (int i = 0; i < 16; i++)
		{
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.grassSeeds, 1, i + 1), new ItemStack(ModItems.grassSeeds, 1, 0), oreDictDyes[i]));
		}
	}

	private static void createDyeRecipes(ItemStack original, Block dyedBlock)
	{
		for (int i = 0; i < 16; i++)
		{
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(dyedBlock, 8, i), "ttt", "tdt", "ttt", 't', original, 'd', oreDictDyes[i]));
		}
	}
}
