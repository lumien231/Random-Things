package lumien.randomthings.worldgen;

import lumien.randomthings.worldgen.spectre.SpectreWorldProvider;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static int SPECTRE_ID = "Spectre".hashCode();

	public static void register()
	{
		//DimensionManager.registerProviderType(SPECTRE_ID, SpectreWorldProvider.class, true);
		//DimensionManager.registerDimension(SPECTRE_ID, SPECTRE_ID);
	}
}
