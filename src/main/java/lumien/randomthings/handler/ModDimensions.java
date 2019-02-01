package lumien.randomthings.handler;

import lumien.randomthings.config.Internals;
import lumien.randomthings.handler.spectre.SpectreWorldProvider;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static DimensionType SPECTRE_TYPE;

	public static void register()
	{
		int id = Internals.SPECTRE_ID;
		
		DimensionManager.registerDimension(id, SPECTRE_TYPE = DimensionType.register("Spectre", "Spectre_", id, SpectreWorldProvider.class, true));
	}
}
