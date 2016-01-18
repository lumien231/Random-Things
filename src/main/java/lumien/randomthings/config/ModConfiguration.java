package lumien.randomthings.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration
{
	Configuration configuration;

	public void preInit(FMLPreInitializationEvent event)
	{
		configuration = new Configuration(event.getSuggestedConfigurationFile());
		configuration.load();

		Features.removeAirBubble = configuration.getBoolean("RemoveUnderwaterTexture", "Features", false, "TRIES to remove the weird water texture showing around ALL non full blocks. This might look weird when you, for example, are on a ladder underwater.");

		configuration.getCategory("worldgen").setComment("Set to false to disable the generation of the respective objects");

		Worldgen.beans = configuration.get("worldgen", "Beans", true).getBoolean();
		Worldgen.cores = configuration.get("worldgen", "Cores", true).getBoolean();
		Worldgen.pitcherPlants = configuration.get("worldgen", "PitcherPlants", true).getBoolean();
		Worldgen.sakanade = configuration.get("worldgen", "Sakanade", true).getBoolean();
		
		if (configuration.hasChanged())
		{
			configuration.save();
		}
	}
}
