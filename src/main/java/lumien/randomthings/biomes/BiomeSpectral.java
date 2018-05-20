package lumien.randomthings.biomes;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeSpectral extends Biome
{

	public BiomeSpectral()
	{
		super(new BiomeProperties("Spectral").setRainDisabled().setWaterColor(Color.CYAN.getRGB()).setTemperature(0.2f));

		this.setRegistryName(new ResourceLocation("randomthings", "spectral"));
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();

	}

	@Override
	public int getSkyColorByTemp(float currentTemperature)
	{
		return super.getSkyColorByTemp(currentTemperature);
	}
}
