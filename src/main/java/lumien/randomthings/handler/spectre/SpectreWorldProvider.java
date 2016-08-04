package lumien.randomthings.handler.spectre;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpectreWorldProvider extends WorldProvider
{
	public SpectreWorldProvider()
	{
		this.hasNoSky = true;
	}

	@Override
	public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_)
	{
		return 0.5F;
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new SpectreChunkProvider(this.worldObj);
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		for (int i = 0; i <= 15; ++i)
		{
			this.lightBrightnessTable[i] = 1;
		}
	}

	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return false;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return false;
	}

	@Override
	public float[] calcSunriseSunsetColors(float par1, float par2)
	{
		return new float[] { 0, 0, 0, 0 };
	}

	@Override
	protected void createBiomeProvider()
	{
		this.biomeProvider = new BiomeProviderSingle(Biomes.HELL);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0.03, 0.2, 0.2);
	}

	@Override
	public float getCloudHeight()
	{
		return -5;
	}

	@Override
	public DimensionType getDimensionType()
	{
		return DimensionType.OVERWORLD;
	}
}
