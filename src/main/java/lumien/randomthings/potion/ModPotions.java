package lumien.randomthings.potion;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.potion.imbues.ImbueExperience;
import lumien.randomthings.potion.imbues.ImbueFire;
import lumien.randomthings.potion.imbues.ImbuePoison;
import lumien.randomthings.potion.imbues.ImbueWither;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModPotions
{
	public static ImbueFire imbueFire;
	public static ImbuePoison imbuePoison;
	public static ImbueExperience imbueExperience;
	public static ImbueWither imbueWither;
	
	public static void preInit(FMLPreInitializationEvent event)
	{
		extendPotionArray();
		
		imbueFire = new ImbueFire();
		imbuePoison = new ImbuePoison();
		imbueExperience = new ImbueExperience();
		imbueWither = new ImbueWither();
	}
	
	private static void extendPotionArray()
	{
		try
		{
			Field potionTypesField = Potion.class.getDeclaredField(MCPNames.field("field_76425_a"));
			ReflectionUtil.makeModifiable(potionTypesField);
			int potionArraySize = Potion.potionTypes.length;
			if (potionArraySize < 128)
			{
				Potion[] newArray = new Potion[128];

				for (int i = 0; i < potionArraySize; i++)
				{
					newArray[i] = Potion.potionTypes[i];
				}
				
				potionTypesField.set(null, newArray);
				RandomThings.instance.logger.log(Level.INFO, "Extended Potion ID Array to 128");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			RandomThings.instance.logger.log(Level.WARN, "Could not extend Potion Array to 128");
		}
	}
}
