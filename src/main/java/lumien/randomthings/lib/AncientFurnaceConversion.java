package lumien.randomthings.lib;

import java.util.HashMap;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

public class AncientFurnaceConversion
{
	static HashMap<Biome, Biome> map = new HashMap<Biome, Biome>();

	static
	{
		map.put(Biomes.TAIGA, Biomes.FOREST);
		map.put(Biomes.FROZEN_OCEAN, Biomes.OCEAN);
		map.put(Biomes.FROZEN_RIVER, Biomes.RIVER);
		map.put(Biomes.ICE_PLAINS, Biomes.PLAINS);
		map.put(Biomes.ICE_MOUNTAINS, Biomes.FOREST_HILLS);
		map.put(Biomes.TAIGA_HILLS, Biomes.FOREST_HILLS);
		map.put(Biomes.COLD_BEACH, Biomes.BEACH);
		map.put(Biomes.COLD_TAIGA, Biomes.BIRCH_FOREST);
		map.put(Biomes.COLD_TAIGA_HILLS, Biomes.BIRCH_FOREST_HILLS);
		map.put(Biomes.REDWOOD_TAIGA_HILLS, Biomes.BIRCH_FOREST_HILLS);
		map.put(Biomes.MUTATED_TAIGA, Biomes.MUTATED_BIRCH_FOREST);
		map.put(Biomes.MUTATED_ICE_FLATS, Biomes.MUTATED_FOREST);
		map.put(Biomes.MUTATED_TAIGA_COLD, Biomes.MUTATED_BIRCH_FOREST_HILLS);
	}

	public static Biome getHeatingConversion(Biome b)
	{
		return map.get(b);
	}
}
