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

	@ConfigOption(category = "Numbers", name = "AdvancedItemCollectorMaxRange", comment = "The maximum radius an advanced item collector can be configured to have")
	public static int ADVANCED_ITEM_COLLECTOR_MAX_RANGE = 10;

	@ConfigOption(category = "Numbers", name = "ItemCollectorRange", comment = "The radius of a normal item collector")
	public static int ITEM_COLLECTOR_RANGE = 3;

	@ConfigOption(category = "Numbers", name = "BlockDestabilizerLimit", comment = "How many blocks the Block Destabilizer can destabilize at once (0 = Unlimited)")
	public static int BLOCK_DESTABILIZER_LIMIT = 50;

	@ConfigOption(category = "VoxelProjector", name = "ModelTransferBandwidth", comment = "The amount of bytes that can be used to transfer models to clients per tick (The default 1000 Byte equal 20 kbyte/sec)")
	public static int MODEL_TRANSFER_BANDWIDTH = 1000;

	@ConfigOption(category = "Numbers", name = "AncientFurnaceLimit", comment = "How many blocks an Ancient Furnace can transform before stopping")
	public static int ANCIENT_FURNACE_LIMIT = 10000;
	
	@ConfigOption(category = "Numbers", name = "NumberedSpectreCoilEnergy", comment = "How much Energy a Numbered Spectre Coil produces per Tick")
	public static int NUMBERED_SPECTRECOIL_ENERGY = 128;
	
	@ConfigOption(category = "Numbers", name = "TimeInABottlePerSecond", comment = "How many ticks have to pass for a Time in a Bottle to gain 1 second (20 = 1 Second)")
	public static int TIME_IN_A_BOTTLE_SECOND = 20;
}
