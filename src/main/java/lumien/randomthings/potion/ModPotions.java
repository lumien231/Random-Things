package lumien.randomthings.potion;

import java.lang.reflect.Field;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.potion.effects.EffectBoss;
import lumien.randomthings.potion.effects.EffectCollapse;
import lumien.randomthings.potion.imbues.ImbueCollapse;
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
	public static ImbueCollapse imbueCollapse;
	
	public static EffectCollapse collapse;
	public static EffectBoss boss;

	public static void preInit(FMLPreInitializationEvent event)
	{
		imbueFire = new ImbueFire();
		imbuePoison = new ImbuePoison();
		imbueExperience = new ImbueExperience();
		imbueWither = new ImbueWither();
		imbueCollapse = new ImbueCollapse();
		
		collapse = new EffectCollapse();
		boss = new EffectBoss();
	}
}
