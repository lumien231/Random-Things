package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Worldgen
{
	public static boolean beans;
	public static boolean pitcherPlants;
	public static boolean sakanade;
	
	public static boolean natureCore;
	
	
	@ConfigOption(category = "worldgen",name = "WaterChest")
	public static boolean WATER_CHEST = true;
}

