package lumien.randomthings.worldgen.spectre;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
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
	public String getDimensionName()
	{
		return "The Spectre Dimension";
	}

	@Override
	public String getInternalNameSuffix()
	{
		return "_spectre";
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new SpectreChunkProvider(this.worldObj);
	}

	protected void generateLightBrightnessTable()
	{
		for (int i = 0; i <= 15; ++i)
		{
			this.lightBrightnessTable[i] = 1;
		}
	}

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
		return true;
	}
	
	@Override
	public float[] calcSunriseSunsetColors(float par1, float par2)
	{
		return new float[] { 0, 0, 0, 0 };
	}

	@Override
	protected void registerWorldChunkManager()
	{
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0F);
	}

	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3(0.03, 0.2, 0.2);
	}
	
	@Override
	public float getCloudHeight()
	{
		return -5;
	}
}
