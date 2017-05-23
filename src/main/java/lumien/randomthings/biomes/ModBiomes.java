package lumien.randomthings.biomes;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;

public class ModBiomes
{
	public static BiomeSpectral spectralBiome;
	
	public static void preInit(FMLPreInitializationEvent event)
	{
		spectralBiome = new BiomeSpectral();
		
		GameData.getBiomeRegistry().register(spectralBiome);
	}
}
