package lumien.randomthings.item;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModItems
{
	public static ItemStableEnderpearl stableEnderpearl;
	public static ItemBiomeCrystal biomeCrystal;
	public static ItemPositionFilter positionFilter;
	public static ItemImmortalityChalice chaliceOfImmortality;
	public static ItemSummoningPendulum summoningPendulum;
	public static ItemGrassSeeds grassSeeds;
	public static ItemBean beans;
	public static ItemBeanStew beanStew;
	public static ItemLinkingOrb linkingOrb;
	public static ItemInertLinkingOrb inertLinkingOrb;
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
	public static ItemSpectreKey spectreKey;

	public static void load(FMLPreInitializationEvent event)
	{
		stableEnderpearl = new ItemStableEnderpearl();
		biomeCrystal = new ItemBiomeCrystal();
		positionFilter = new ItemPositionFilter();
		chaliceOfImmortality = new ItemImmortalityChalice();
		summoningPendulum = new ItemSummoningPendulum();
		beans = new ItemBean();
		beanStew = new ItemBeanStew();
		linkingOrb = new ItemLinkingOrb();
		inertLinkingOrb = new ItemInertLinkingOrb();
		// craftingRecipe = new ItemCraftingRecipe();
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
		spectreKey = new ItemSpectreKey();

		grassSeeds = new ItemGrassSeeds();
	}
}
