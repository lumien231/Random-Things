package lumien.randomthings.recipes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.recipes.anvil.AnvilRecipeHandler;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class OldRecipes
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
		final ItemStack waterBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
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

		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.playerInterface), "oco", "ono", "oso", 'o', obsidian, 'c', enderChest, 's', stableEnderpearl, 'n', netherStar);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.fertilizedDirt, 2, 0), "rbr", "bdb", "rbr", 'r', rottenFlesh, 'b', boneMeal, 'd', dirt);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.lapisGlass, 8, 0), "ggg", "glg", "ggg", 'g', "blockGlass", 'l', lapisBlock);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.onlineDetector, 1, 0), "srs", "rtr", "srs", 's', "stone", 'r', repeater, 't', "dyeBlue");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.chatDetector, 1, 0), "srs", "rtr", "srs", 's', "stone", 'r', repeater, 't', "dyeRed");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.enderBridge), "eee", "ers", "eee", 'e', endStone, 'r', redstoneDust, 's', stableEnderpearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.enderAnchor), "ooo", "oso", "ooo", 'o', obsidian, 's', stableEnderpearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.lapisLamp), "xlx", "lal", "xlx", 'l', lapis, 'a', redstoneLamp);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.dyeingMachine), "xgx", "rcb", "xwx", 'g', "dyeGreen", 'r', "dyeRed", 'b', "dyeBlue", 'w', blackWool, 'c', craftingTable);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.prismarineEnderBridge), "scs", "cbc", "scs", 's', prismarineShard, 'c', prismarineCrystal, 'b', new ItemStack(ModBlocks.enderBridge));
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.basicRedstoneInterface), "iri", "rsr", "iri", 'i', "ingotIron", 'r', redstoneDust, 's', stableEnderpearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.lightRedirector), "pgp", "gxg", "pgp", 'g', "blockGlass", 'p', "plankWood");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.imbuingStation), "xwx", "vcv", "lel", 'w', Items.WATER_BUCKET, 'v', Blocks.VINE, 'c', Blocks.HARDENED_CLAY, 'l', Blocks.WATERLILY, 'e', Items.EMERALD);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.analogEmitter), "tir", "iii", "rit", 't', redstoneTorch, 'i', "ingotIron", 'r', redstoneDust);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.fluidDisplay), "ggg", "gbg", "ggg", 'g', "blockGlassColorless", 'b', Items.GLASS_BOTTLE);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.enderMailbox), "ehe", "iii", "xfx", 'e', enderPearl, 'h', Blocks.HOPPER, 'i', "ingotIron", 'f', Blocks.OAK_FENCE);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.entityDetector), "srs", "epe", "srs", 's', "stone", 'r', redstoneTorch, 'e', enderPearl, 'p', stonePressurePlate);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.quartzLamp), "xqx", "qlq", "xqx", 'q', quartz, 'l', redstoneLamp);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.quartzGlass, 8, 0), "ggg", "gqg", "ggg", 'g', "blockGlass", 'q', quartzBlock);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.potionVaporizer), "ctc", "iui", "cfc", 'c', "cobblestone", 't', Blocks.IRON_TRAPDOOR, 'i', "ingotIron", 'u', Items.CAULDRON, 'f', Blocks.FURNACE);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.voxelProjector), "rgb", "wlw", "www", 'r', "blockGlassRed", 'g', "blockGlassGreen", 'b', "blockGlassBlue", 'w', blackWool, 'l', redstoneLamp);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.rainShield), "xfx", "xbx", "nnn", 'f', flint, 'b', Items.BLAZE_ROD, 'n', netherRack);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.blockBreaker), "cpc", "crc", "ccc", 'c', "cobblestone", 'p', Items.IRON_PICKAXE, 'r', redstoneTorch);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.rainbowLamp), "xgx", "rlb", 'g', "dyeGreen", 'r', "dyeRed", 'b', "dyeBlue", 'l', redstoneLamp);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.advancedRedstoneInterface), "ror", "oio", "ror", 'r', "blockRedstone", 'o', obsidian, 'i', ModBlocks.basicRedstoneInterface);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.itemRedirector, 2), "ooo", "xhx", "olo", 'o', Blocks.OBSIDIAN, 'h', Blocks.HOPPER, 'l', ModBlocks.superLubricentIce);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.itemCorrector), "ili", "ccc", "ccc", 'c', "cobblestone", 'i', "ingotIron", 'l', ModBlocks.superLubricentIce);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.superLubricentIce, 16), "s", "i", "w", 's', "slimeball", 'i', Blocks.ICE, 'w', Items.WATER_BUCKET);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.itemSealer), "ala", "ccc", "ccc", 'c', "cobblestone", 'a', "gemLapis", 'l', ModBlocks.superLubricentIce);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.itemRejuvenator), "wlw", "ccc", "ccc", 'c', "cobblestone", 'w', Blocks.WOOL, 'l', ModBlocks.superLubricentIce);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.superLubricentPlatform, 6), "iii", "xex", 'i', ModBlocks.superLubricentIce, 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.filteredItemRedirector), "xpx", "xrx", "xsx", 'p', paper, 'r', ModBlocks.itemRedirector, 's', Items.STRING);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.filteredSuperLubricentPlatform), "xpx", "xrx", "xsx", 'p', paper, 'r', ModBlocks.superLubricentPlatform, 's', Items.STRING);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.redstoneObserver), "rqr", "qeq", "rqr", 'r', redstoneDust, 'q', Items.QUARTZ, 'e', Items.ENDER_EYE);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeRadar), "iii", "gsg", "iii", 'i', "ingotIron", 'g', "blockGlass", 's', biomeSensor);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.ironDropper), "iii", "ixi", "iri", 'i', "ingotIron", 'r', redstoneDust);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.blockOfSticks, 16), "sss", "sxs", "sss", 's', "stickWood");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.igniter), "ncc", "nfc", "ncc", 'n', Blocks.NETHERRACK, 'c', "cobblestone", 'f', Items.FLINT_AND_STEEL);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.blockOfSticks, 8, 1), "sss", "ses", "sss", 's', new ItemStack(ModBlocks.blockOfSticks), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.inventoryRerouter), "sbs", "bhb", "sbs", 's', "stone", 'b', Blocks.IRON_BARS, 'h', Blocks.HOPPER);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.slimeCube), "xsx", "sns", "xsx", 's', Items.SLIME_BALL, 'n', Items.NETHER_STAR);

		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.itemCollector), "xex", "xhx", "ooo", 'e', enderPearl, 'h', Blocks.HOPPER, 'o', obsidian);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.advancedItemCollector), "xrx", "gig", 'r', redstoneTorch, 'g', glowStone, 'i', ModBlocks.itemCollector);

		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.contactButton), "sis", "sbs", "sss", 's', "stone", 'i', Blocks.IRON_BARS, 'b', stoneButton);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.contactLever), "sis", "sbs", "sss", 's', "stone", 'i', Blocks.IRON_BARS, 'b', lever);

		outputShapeless(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.grassSeeds), Blocks.GRASS);

		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.stableEnderpearl), "olo", "lel", "olo", 'o', obsidian, 'l', lapis, 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.beans, 1, 1), "nnn", "nbn", "nnn", 'b', "cropBean", 'n', "nuggetGold");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.beanStew, 1, 0), "xwx", "bbb", "xox", 'b', bean, 'w', wheat, 'o', bowl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.beanStew, 1, 0), "bbb", "xwx", "xox", 'b', bean, 'w', wheat, 'o', bowl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.positionFilter, 1, 0), "xdx", "dpd", "xdx", 'd', "dyePurple", 'p', paper);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.redstoneTool), "xrx", "xsx", "xsx", 'r', redstoneDust, 's', "stickWood");
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.obsidianSkull), "oro", "bwb", "oro", 'o', obsidian, 'r', Items.BLAZE_ROD, 'w', witherSkull, 'b', netherBrick);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.enderLetter), "xxx", "pep", "xpx", 'p', Items.PAPER, 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.entityFilter), "xdx", "dpd", "xdx", 'd', "dyeBlue", 'p', paper);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.itemFilter), "xyx", "ypy", "xyx", 'y', "dyeYellow", 'p', paper);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectreKey), "ixx", "ipx", "xxi", 'i', spectreIngot, 'p', stableEnderpearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.redstoneActivator), "iri", "iti", "iii", 'i', "ingotIron", 'r', "dustRedstone", 't', redstoneTorch);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.redstoneRemote), "aaa", "oso", "ooo", 'a', ModItems.redstoneActivator, 'o', Blocks.OBSIDIAN, 's', stableEnderpearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectreAnchor), "xix", "iei", "iii", 'i', "ingotIron", 'e', ectoPlasm);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.goldenCompass), "xgx", "gcg", "xgx", 'g', "ingotGold", 'c', Items.COMPASS);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.blazeAndSteel), "ib", 'b', Items.BLAZE_POWDER, 'i', Items.IRON_INGOT);

		// Spectre Tools
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectreSword), "xsx", "xsx", "xox", 's', spectreIngot, 'o', obsidian);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectreAxe), "ssx", "sox", "xox", 's', spectreIngot, 'o', obsidian);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectrePickaxe), "sss", "xox", "xox", 's', spectreIngot, 'o', obsidian);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.spectreShovel), "xsx", "xox", "xox", 's', spectreIngot, 'o', obsidian);

		// Ingredients
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.EVIL_TEAR.id), "xsx", "xtx", "xex", 's', witherSkull, 't', ghastTear, 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id), "xlx", "xix", "xex", 'l', lapis, 'i', "ingotGold", 'e', ectoPlasm);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.ingredients, 9, ItemIngredient.INGREDIENT.SPECTRE_INGOT.id), "ele", "eie", "eee", 'l', lapisBlock, 'i', "blockGold", 'e', ectoPlasm);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.BIOME_SENSOR.id), "iii", "rci", "iri", 'i', "ingotIron", 'r', redstoneDust, 'c', ModItems.biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.ingredients, 4, ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id), "gdg", 'g', "blockGlassColorless", 'd', glowStone);

		// Biome Blocks
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeStone, 16, 0), "ccc", "cbc", "ccc", 'c', cobblestone, 'b', biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeStone, 16, 1), "sss", "sbs", "sss", 's', stone, 'b', biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeStone, 16, 2), "rrr", "rbr", "rrr", 'r', stoneBricks, 'b', biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeStone, 16, 3), "ccc", "cbc", "ccc", 'c', crackedStoneBricks, 'b', biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeStone, 16, 4), "ccc", "cbc", "ccc", 'c', chiseledStoneBricks, 'b', biomeCrystal);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.biomeGlass, 16), "ggg", "gbg", "ggg", 'g', "blockGlassColorless", 'b', biomeCrystal);

		// Platforms
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 0), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 0), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 1), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 1), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 2), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 2), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 3), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 3), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 4), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 4), 'e', enderPearl);
		outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.platform, 6, 5), "www", "xex", 'w', new ItemStack(Blocks.PLANKS, 1, 5), 'e', enderPearl);

		ForgeRegistries.RECIPES.register(new RecipeWorkbench());

		outputShapeless(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.spectrePlank, 4), ModBlocks.spectreLog);

		createDyeRecipes(new ItemStack(Blocks.BRICK_BLOCK), ModBlocks.stainedBrick);
		createGrassSeedsRecipes();

		// Imbuing Station
		ImbuingRecipeHandler.addRecipe(waterBottle, vine, boneMeal, cobblestone, mossyCobblestone);

		ImbuingRecipeHandler.addRecipe(coal, flint, blazePowder, waterBottle, new ItemStack(ModItems.imbue, 1, 0));
		ImbuingRecipeHandler.addRecipe(spiderEye, rottenFlesh, redMushroom, waterBottle, new ItemStack(ModItems.imbue, 1, 1));
		ImbuingRecipeHandler.addRecipe(new ItemStack(ModItems.beans, 1, 1), lapis, glowStone, waterBottle, new ItemStack(ModItems.imbue, 1, 2));
		ImbuingRecipeHandler.addRecipe(witherSkull, netherBrick, ghastTear, waterBottle, new ItemStack(ModItems.imbue, 1, 3));

		// Anvil
		if (Loader.isModLoaded("baubles"))
		{
			AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianSkull), new ItemStack(Items.FIRE_CHARGE), new ItemStack(ModItems.obsidianSkullRing), 3);
		}

		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkull), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.waterWalkingBoots), new ItemStack(ModItems.obsidianSkullRing), new ItemStack(ModItems.obsidianWaterWalkingBoots), 10);
		AnvilRecipeHandler.addAnvilRecipe(new ItemStack(ModItems.obsidianWaterWalkingBoots), new ItemStack(ModItems.lavaCharm), new ItemStack(ModItems.lavaWader), 15);

		createLuminousRecipes();
		createRunicDustRecipes();

		// Spectre Anchor
		IRecipe anchorRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "spectreAnchorCombine"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack anchor = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
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
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
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

					if (!is.isEmpty())
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

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("spectreAnchorCombine", anchorRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(anchorRecipe);

		// Golden Compass
		IRecipe goldenCompassRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "goldenCompassSetPosition"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack compass = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
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
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				NonNullList aitemstack = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.positionFilter)
						{
							aitemstack.set(i, is.copy());
						}
					}
				}

				return aitemstack;
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

					if (!is.isEmpty())
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

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("goldenCompass", goldenCompassRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(goldenCompassRecipe);

		// Luminous Powder
		IRecipe luminousPowderRecipe = new SimpleRecipe(new ResourceLocation("randomthings", "luminousPowder"))
		{
			@Override
			public boolean matches(InventoryCrafting inv, World worldIn)
			{
				ItemStack powder = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.ingredients && is.getItemDamage() == ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id)
						{
							if (powder == null)
							{
								powder = is;
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
								if (!is.isItemEnchanted())
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
				return powder != null && target != null && (!target.hasTagCompound() || !target.getTagCompound().hasKey("luminousEnchantment"));
			}

			@Override
			public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
			{
				return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
			}

			@Override
			public ItemStack getRecipeOutput()
			{
				return new ItemStack(ModItems.spectreAnchor);
			}

			@Override
			public ItemStack getCraftingResult(InventoryCrafting inv)
			{
				ItemStack powder = null;
				ItemStack target = null;

				for (int i = 0; i < inv.getSizeInventory(); i++)
				{
					ItemStack is = inv.getStackInSlot(i);

					if (!is.isEmpty())
					{
						if (is.getItem() == ModItems.ingredients)
						{
							powder = is;
						}
						else
						{
							target = is;
						}
					}
				}

				ItemStack result = target.copy();

				result.setTagInfo("luminousEnchantment", new NBTTagByte((byte) 0));

				return result;
			}

			@Override
			public boolean canFit(int width, int height)
			{
				return true;
			}
		};

		RecipeSorter.register("luminousPowder", luminousPowderRecipe.getClass(), Category.SHAPELESS, "");
		ForgeRegistries.RECIPES.register(luminousPowderRecipe);
	}

	private static void createRunicDustRecipes()
	{
		for (int i = 0; i < 16; i++)
		{
			outputShapeless(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.runeDust, 8, i), Items.CLAY_BALL, new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id), oreDictDyes[i]);
		}
	}

	private static void createGrassSeedsRecipes()
	{
		for (int i = 0; i < 16; i++)
		{
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			outputShapeless(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModItems.grassSeeds, 1, i + 1), new ItemStack(ModItems.grassSeeds, 1, 0), oreDictDyes[i]);
		}
	}

	private static void createDyeRecipes(ItemStack original, Block dyedBlock)
	{
		for (int i = 0; i < 16; i++)
		{
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(dyedBlock, 8, i), "ttt", "tdt", "ttt", 't', original, 'd', oreDictDyes[i]);
		}
	}

	private static void createLuminousRecipes()
	{
		for (int i = 0; i < 16; i++)
		{
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			outputShaped(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.luminousBlock, 1, i), "ld", "ll", 'l', new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id), 'd', oreDictDyes[i]);
			outputShapeless(new ResourceLocation("randomthings", "recipes"), new ItemStack(ModBlocks.luminousStainedBrick, 1, i), new ItemStack(ModBlocks.stainedBrick, 1, i), new ItemStack(ModItems.ingredients, 1, ItemIngredient.INGREDIENT.LUMINOUS_POWDER.id));
		}
	}

	private static void outputShapeless(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject masterObject = new JsonObject();
		masterObject.add("type", new JsonPrimitive("minecraft:crafting_shapeless"));

		ResourceLocation resultLocation = result.getItem().getRegistryName();
		String resultName = resultLocation.toString();
		int resultData = result.getItemDamage();

		JsonArray ingredientsArray = new JsonArray();

		masterObject.add("ingredients", ingredientsArray);

		for (Object object : recipe)
		{
			JsonObject ingredientObject = new JsonObject();

			if (object instanceof Item)
			{
				object = new ItemStack((Item) object);
			}
			else if (object instanceof Block)
			{
				object = new ItemStack((Block) object);
			}

			if (object instanceof String)
			{
				ingredientObject.add("type", new JsonPrimitive("forge:ore_dict"));
				ingredientObject.add("ore", new JsonPrimitive((String) object));
			}
			else if (object instanceof ItemStack)
			{
				ItemStack is = (ItemStack) object;
				ingredientObject.add("item", new JsonPrimitive(is.getItem().getRegistryName().toString()));

				if (is.getItemDamage() != 0 || is.getItem().getHasSubtypes())
					ingredientObject.add("data", new JsonPrimitive(is.getItemDamage()));
			}

			ingredientsArray.add(ingredientObject);
		}

		JsonObject resultObject = new JsonObject();
		masterObject.add("result", resultObject);

		resultObject.add("item", new JsonPrimitive(resultName));

		if (resultData != 0 || result.getItem().getHasSubtypes())
		{
			resultObject.add("data", new JsonPrimitive(resultData));
		}
		
		if (result.getCount() != 1)
		{
			resultObject.add("count", new JsonPrimitive(result.getCount()));
		}

		try
		{
			File file = new File("./output_recipes/" + resultLocation.getResourcePath() + ".json");

			if (result.getHasSubtypes())
			{
				file = new File("./output_recipes/" + result.getItem().getUnlocalizedName(result).replaceAll("\\.", "_").replaceAll("item_", "").replaceAll("tile_", "") + ".json");
			}

			int number = 2;
			while (file.exists())
			{
				file = new File(file.getAbsolutePath().replace(".json", "") + "_"+number+".json");
				number++;
			}
			
			if (!file.exists())
			{
				Files.touch(file);
			}
			else
			{
				System.out.println("OVERWRITING " + resultLocation.getResourcePath());
			}
			Files.write(gson.toJson(masterObject), file, Charset.defaultCharset());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void outputShaped(ResourceLocation group, @Nonnull ItemStack result, Object... recipe)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject masterObject = new JsonObject();
		masterObject.add("type", new JsonPrimitive("minecraft:crafting_shaped"));

		ResourceLocation resultLocation = result.getItem().getRegistryName();
		String resultName = resultLocation.toString();
		int resultData = result.getItemDamage();

		int line = 0;

		JsonArray patternArray = new JsonArray();

		while (recipe[line] instanceof String)
		{
			String patternLine = (String) recipe[line];
			patternLine = patternLine.replaceAll("x", " ");

			patternArray.add(patternLine.toUpperCase());

			line++;
		}

		masterObject.add("pattern", patternArray);

		JsonObject keysObject = new JsonObject();

		masterObject.add("key", keysObject);

		int i = line;

		char currentChar = '0';

		while (i < recipe.length)
		{
			Object object = recipe[i];

			if (object instanceof Character)
			{
				char keyCharacter = (char) object;

				currentChar = keyCharacter;
			}
			else
			{
				if (currentChar == '0')
				{
					System.out.println("Recipe ERROR at " + resultLocation);
				}

				JsonObject keyObject = new JsonObject();

				if (object instanceof Item)
				{
					object = new ItemStack((Item) object);
				}
				else if (object instanceof Block)
				{
					object = new ItemStack((Block) object);
				}

				if (object instanceof String)
				{
					keyObject.add("type", new JsonPrimitive("forge:ore_dict"));
					keyObject.add("ore", new JsonPrimitive((String) object));
				}
				else if (object instanceof ItemStack)
				{
					ItemStack is = (ItemStack) object;
					keyObject.add("item", new JsonPrimitive(is.getItem().getRegistryName().toString()));

					if (is.getItemDamage() != 0 || is.getItem().getHasSubtypes())
						keyObject.add("data", new JsonPrimitive(is.getItemDamage()));
				}

				keysObject.add((currentChar + "").toUpperCase(), keyObject);

				currentChar = '0';
			}


			i++;
		}

		JsonObject resultObject = new JsonObject();
		masterObject.add("result", resultObject);

		resultObject.add("item", new JsonPrimitive(resultName));

		if (resultData != 0 || result.getItem().getHasSubtypes())
		{
			resultObject.add("data", new JsonPrimitive(resultData));
		}
		
		if (result.getCount() != 1)
		{
			resultObject.add("count", new JsonPrimitive(result.getCount()));
		}

		try
		{
			File file = new File("./output_recipes/" + resultLocation.getResourcePath() + ".json");

			if (result.getHasSubtypes())
			{
				file = new File("./output_recipes/" + result.getItem().getUnlocalizedName(result).replaceAll("\\.", "_").replaceAll("item_", "").replaceAll("tile_", "") + ".json");
			}
			
			int number = 2;
			while (file.exists())
			{
				file = new File(file.getAbsolutePath().replace(".json", "") + "_"+number+".json");
				number++;
			}

			if (!file.exists())
			{
				Files.touch(file);
			}
			else
			{
				System.out.println("OVERWRITING " + resultLocation.getResourcePath());
			}
			Files.write(gson.toJson(masterObject), file, Charset.defaultCharset());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
