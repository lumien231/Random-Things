package lumien.randomthings.biomes;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModBiomes
{
	public static BiomeSpectral spectralBiome;

	public static void preInit(FMLPreInitializationEvent event)
	{
		spectralBiome = new BiomeSpectral();

		ForgeRegistries.BIOMES.register(spectralBiome);
		BiomeDictionary.addTypes(spectralBiome, Type.MAGICAL);
	}
}
