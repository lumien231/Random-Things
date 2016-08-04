package lumien.randomthings.potion;

import lumien.randomthings.potion.effects.EffectCollapse;
import lumien.randomthings.potion.imbues.ImbueCollapse;
import lumien.randomthings.potion.imbues.ImbueExperience;
import lumien.randomthings.potion.imbues.ImbueFire;
import lumien.randomthings.potion.imbues.ImbuePoison;
import lumien.randomthings.potion.imbues.ImbueWither;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModPotions
{
	public static ImbueFire imbueFire;
	public static ImbuePoison imbuePoison;
	public static ImbueExperience imbueExperience;
	public static ImbueWither imbueWither;
	public static ImbueCollapse imbueCollapse;
	
	public static EffectCollapse collapse;

	public static void preInit(FMLPreInitializationEvent event)
	{
		imbueFire = new ImbueFire();
		imbuePoison = new ImbuePoison();
		imbueExperience = new ImbueExperience();
		imbueWither = new ImbueWither();
		imbueCollapse = new ImbueCollapse();
		
		collapse = new EffectCollapse();
	}
}
