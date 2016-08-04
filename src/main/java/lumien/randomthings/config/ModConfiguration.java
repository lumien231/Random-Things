package lumien.randomthings.config;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.ConfigOption;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
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
		Worldgen.natureCore = configuration.get("worldgen", "Nature-Core", true).getBoolean();
		Worldgen.pitcherPlants = configuration.get("worldgen", "PitcherPlants", true).getBoolean();
		Worldgen.sakanade = configuration.get("worldgen", "Sakanade", true).getBoolean();


		// Annotation Based Config
		doAnnoations(configuration);

		if (configuration.hasChanged())
		{
			configuration.save();
		}
	}

	private void doAnnoations(Configuration configuration)
	{
		ASMDataTable asmData = RandomThings.instance.getASMData();

		Set<ASMData> atlasSet = asmData.getAll(ConfigOption.class.getName());

		for (ASMData data : atlasSet)
		{
			try
			{
				Class clazz = Class.forName(data.getClassName());
				Field f = clazz.getDeclaredField(data.getObjectName());
				f.setAccessible(true);

				String name = (String) data.getAnnotationInfo().get("name");
				String category = (String) data.getAnnotationInfo().get("category");
				String comment = (String) data.getAnnotationInfo().get("comment");

				Object result = null;

				if (f.getType() == boolean.class)
				{
					result = configuration.get(category, name, f.getBoolean(null), comment).getBoolean();
				}
				else if (f.getType() == double.class)
				{
					result = configuration.get(category, name, f.getDouble(null), comment).getDouble();
				}
				else if (f.getType() == int.class)
				{
					result = configuration.get(category, name, f.getInt(null), comment).getInt();
				}

				if (result != null)
				{
					f.set(null, result);
				}
				else
				{
					throw new RuntimeException("Invalid Data Type for Config annotation: " + f.getType());
				}
			}
			catch (Exception e)
			{
				RandomThings.instance.logger.log(Level.ERROR, "Error stitching extra textures");
				e.printStackTrace();
			}
		}

	}
}
