package lumien.randomthings.lib;

public enum ChestCategory
{
	MINESHAFT_CORRIDOR("mineshaftCorridor"), PYRAMID_DESERT_CHEST("pyramidDesertyChest"), PYRAMID_JUNGLE_CHEST("pyramidJungleChest"), PYRAMID_JUNGLE_DISPENSER("pyramidJungleDispenser"), STRONGHOLD_CORRIDOR("strongholdCorridor"), STRONGHOLD_LIBRARY("strongholdLibrary"), STRONGHOLD_CROSSING("strongholdCrossing"), VILLAGE_BLACKSMITH("villageBlacksmith"), BONUS_CHEST("bonusChest"), DUNGEON_CHEST("dungeonChest"), NETHER_FORTRESS("netherFortress");

	private final String name;

	private ChestCategory(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}
}
