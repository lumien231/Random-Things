package lumien.randomthings.item;

import lumien.randomthings.item.diviningrod.ItemDiviningRod;
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
	public static ItemSuperLubricentBoots superLubricentBoots;
	public static ItemFlooSign flooSign;
	public static ItemIDCard idCard;
	public static ItemEmeraldCompass emeraldCompass;
	public static ItemFlooToken flooToken;
	public static ItemPortKey portKey;
	public static ItemLotusSeeds lotusSeeds;
	public static ItemSoundPattern soundPattern;
	public static ItemSoundRecorder soundRecorder;
	public static ItemPortableSoundDampener portableSoundDampener;
	public static ItemEscapeRope escapeRope;
	public static ItemWeatherEgg weatherEgg;
	public static ItemChunkAnalyzer chunkAnalyzer;
	public static ItemFlooPouch flooPouch;
	public static ItemTimeInABottle timeInABottle;
	public static ItemSpectreIlluminator spectreIlluminator;
	public static ItemSpectreCharger spectreCharger;
	public static ItemDiviningRod diviningRod;
	public static ItemEclipsedClock eclipsedClock;
	
	public static ItemEnderBucket enderBucket;
	public static ItemReinforcedEnderBucket reinforcedEnderBucket;

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
		superLubricentBoots = new ItemSuperLubricentBoots();
		flooSign = new ItemFlooSign();
		idCard = new ItemIDCard();
		emeraldCompass = new ItemEmeraldCompass();
		flooToken = new ItemFlooToken();
		portKey = new ItemPortKey();
		lotusSeeds = new ItemLotusSeeds();
		soundPattern = new ItemSoundPattern();
		soundRecorder = new ItemSoundRecorder();
		portableSoundDampener = new ItemPortableSoundDampener();
		escapeRope = new ItemEscapeRope();
		weatherEgg = new ItemWeatherEgg();
		enderBucket = new ItemEnderBucket();
		reinforcedEnderBucket = new ItemReinforcedEnderBucket();
		chunkAnalyzer = new ItemChunkAnalyzer();
		flooPouch = new ItemFlooPouch();
		timeInABottle = new ItemTimeInABottle();
		spectreIlluminator = new ItemSpectreIlluminator();
		spectreCharger = new ItemSpectreCharger();
		diviningRod = new ItemDiviningRod();
		eclipsedClock = new ItemEclipsedClock();

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
