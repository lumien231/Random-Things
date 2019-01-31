package lumien.randomthings.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds
{
	public static SoundEvent TIME;
	
	public static void preInit(FMLPreInitializationEvent event)
	{
		ResourceLocation TIME_LOCATION = new ResourceLocation("randomthings", "time");
		TIME = new SoundEvent(TIME_LOCATION);
		TIME.setRegistryName(TIME_LOCATION);
		
		ForgeRegistries.SOUND_EVENTS.register(TIME);
	}
}
