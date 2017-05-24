package lumien.randomthings.item;

import lumien.randomthings.item.spectretools.ItemSpectreAxe;
import lumien.randomthings.item.spectretools.ItemSpectrePickaxe;
import lumien.randomthings.item.spectretools.ItemSpectreShovel;
import lumien.randomthings.item.spectretools.ItemSpectreSword;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModItems
{
	public static ItemStableEnderpearl stableEnderpearl;
	public static ItemBiomeCrystal biomeCrystal;
	public static ItemPositionFilter positionFilter;
	public static ItemSummoningPendulum summoningPendulum;
	public static ItemGrassSeeds grassSeeds;
	public static ItemBean beans;
	public static ItemBeanStew beanStew;
	public static ItemCraftingRecipe craftingRecipe;
	public static ItemRedstoneTool redstoneTool;
	public static ItemWaterWalkingBoots waterWalkingBoots;
	public static ItemDungeonChestGenerator dungeonChestGenerator;
	public static ItemLavaCharm lavaCharm;
	public static ItemLavaWader lavaWader;
	public static ItemObsidianSkull obsidianSkull;
	public static ItemObsidianSkullRing obsidianSkullRing;
	public static ItemObsidianWaterWalkingBoots obsidianWaterWalkingBoots;
	public static ItemMagicHood magicHood;
	public static ItemImbue imbue;
	public static ItemBottleOfAir bottleOfAir;
	public static ItemRezStone rezStone;
	public static ItemEnderLetter enderLetter;
	public static ItemEntityFilter entityFilter;
	public static ItemIngredient ingredients;
	public static ItemItemFilter itemFilter;
	public static ItemRedstoneActivator redstoneActivator;
	public static ItemRedstoneRemote redstoneRemote;
	public static ItemGoldenCompass goldenCompass;
	public static ItemBlazeAndSteel blazeAndSteel;
	public static ItemRunePattern runePattern;
	
	public static ItemSpectreKey spectreKey;
	public static ItemSpectreAnchor spectreAnchor;
	public static ItemSpectreSword spectreSword;
	public static ItemSpectrePickaxe spectrePickaxe;
	public static ItemSpectreAxe spectreAxe;
	public static ItemSpectreShovel spectreShovel;
	
	public static ItemRuneDust runeDust;

	public static void load(FMLPreInitializationEvent event)
	{
		stableEnderpearl = new ItemStableEnderpearl();
		biomeCrystal = new ItemBiomeCrystal();
		positionFilter = new ItemPositionFilter();
		summoningPendulum = new ItemSummoningPendulum();
		beans = new ItemBean();
		beanStew = new ItemBeanStew();
		redstoneTool = new ItemRedstoneTool();
		waterWalkingBoots = new ItemWaterWalkingBoots();
		dungeonChestGenerator = new ItemDungeonChestGenerator();
		lavaCharm = new ItemLavaCharm();
		lavaWader = new ItemLavaWader();
		obsidianSkull = new ItemObsidianSkull();
		obsidianSkullRing = new ItemObsidianSkullRing();
		obsidianWaterWalkingBoots = new ItemObsidianWaterWalkingBoots();
		magicHood = new ItemMagicHood();
		imbue = new ItemImbue();
		bottleOfAir = new ItemBottleOfAir();
		rezStone = new ItemRezStone();
		enderLetter = new ItemEnderLetter();
		entityFilter = new ItemEntityFilter();
		ingredients = new ItemIngredient();
		itemFilter = new ItemItemFilter();
		redstoneActivator = new ItemRedstoneActivator();
		redstoneRemote = new ItemRedstoneRemote();
		goldenCompass = new ItemGoldenCompass();
		blazeAndSteel = new ItemBlazeAndSteel();
		runePattern = new ItemRunePattern();

		grassSeeds = new ItemGrassSeeds();
		
		spectreKey = new ItemSpectreKey();
		spectreAnchor = new ItemSpectreAnchor();
		spectreSword = new ItemSpectreSword();
		spectrePickaxe = new ItemSpectrePickaxe();
		spectreAxe = new ItemSpectreAxe();
		spectreShovel = new ItemSpectreShovel();
		
		runeDust = new ItemRuneDust();
	}
}
