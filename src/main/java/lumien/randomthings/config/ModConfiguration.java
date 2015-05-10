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

		PotionIds.IMBUE_FIRE = getPotionID("ImbueFire", 70);
		PotionIds.IMBUE_POISON = getPotionID("ImbuePoison", 71);
		PotionIds.IMBUE_EXPERIENCE = getPotionID("ImbueExperience", 72);
		PotionIds.IMBUE_WITHER = getPotionID("ImbueWither", 73);
		
		Features.removeAirBubble = configuration.getBoolean("RemoveUnderwaterTexture", "Features", false, "TRIES to remove the weird water texture showing around ALL non full blocks. This might look weird when you, for example, are on a ladder underwater.");

		if (configuration.hasChanged())
		{
			configuration.save();
		}
	}

	private int getPotionID(String name, int defaultID)
	{
		return configuration.get("PotionIDs", name, defaultID).getInt(defaultID);
	}
}
