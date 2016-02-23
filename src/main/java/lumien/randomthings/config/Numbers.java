package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Numbers
{
	@ConfigOption(category = "Numbers", name = "RainShieldRange", comment = "How far does the effect of a Rain Shield work? (In Blocks)")
	public static int RAIN_SHIELD_RANGE = 5 * 16;

	@ConfigOption(category = "Numbers", name = "SpiritLifeTime", comment = "How long a Spirit stays in the world after spawning (20=1 second)")
	public static int SPIRIT_LIFETIME = 20 * 20;

	@ConfigOption(category = "Numbers", name = "SpiritChanceNormal", comment = "The base chance of a spirit spawning when an entity dies (0.01 = 1%)")
	public static double SPIRIT_CHANCE_NORMAL = 0.01;

	@ConfigOption(category = "Numbers", name = "SpiritChanceMoonMult", comment = "How much does the moon increase the chance of a spirit spawning? (2 = 2% on full moon)")
	public static double SPIRIT_CHANCE_MOON_MULT = 2;

	@ConfigOption(category = "Numbers", name = "SpiritChanceEndIncrease", comment = "How much does the chance of a spirit spawning increase after the ender dragon is defeated? (0.07 = 7%)")
	public static double SPIRIT_CHANCE_END_INCREASE = 0.07;
}
