package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Worldgen
{
	public static boolean beans;
	public static boolean pitcherPlants;
	public static boolean sakanade;

	public static boolean natureCore;

	
	// Worldgen
	
	@ConfigOption(category = "worldgen", name = "WaterChest")
	public static boolean WATER_CHEST = true;

	@ConfigOption(category = "worldgen", name = "PeaceCandle")
	public static boolean PEACE_CANDLE = true;

	@ConfigOption(category = "worldgen", name = "Lotus")
	public static boolean LOTUS = true;
	
	@ConfigOption(category = "worldgen", name = "GlowingMushrooms")
	public static boolean GLOWING_MUSHROOM = true;

	@ConfigOption(category = "worldgen", name = "AncientFurnace")
	public static boolean ANCIENT_FURNACE = true;

	// Loot
	@ConfigOption(category = "worldgen", name = "MagicHood")
	public static boolean MAGIC_HOOD = true;

	@ConfigOption(category = "worldgen", name = "SummoningPendulum")
	public static boolean SUMMONING_PENDULUM = true;

	@ConfigOption(category = "worldgen", name = "BiomeCrystal")
	public static boolean BIOME_CRYSTAL = true;

	@ConfigOption(category = "worldgen", name = "LavaCharm")
	public static boolean LAVA_CHARM = true;

	@ConfigOption(category = "worldgen", name = "SlimeCube")
	public static boolean SLIME_CUBE = true;
	
	@ConfigOption(category = "worldgen", name = "NumberedSpectreCoils")
	public static boolean NUMBERED_COILS = true;
}
