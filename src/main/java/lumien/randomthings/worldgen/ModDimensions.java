package lumien.randomthings.worldgen;

import lumien.randomthings.worldgen.spectre.SpectreWorldProvider;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static int SPECTRE_ID = "Spectre".hashCode();

	public static void register()
	{
		// Spectre
		//DimensionManager.registerProviderType(SPECTRE_ID, SpectreWorldProvider.class, true);
		//DimensionManager.registerDimension(SPECTRE_ID, SPECTRE_ID);
	}
}
