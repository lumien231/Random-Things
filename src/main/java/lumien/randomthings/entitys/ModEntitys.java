package lumien.randomthings.entitys;

import lumien.randomthings.RandomThings;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntitys
{
	public static void init()
	{
		registerEntity(EntitySoul.class, "playerSoul", 0, RandomThings.instance, 80, 1, true);
		registerEntity(EntityReviveCircle.class, "reviveCircle", 1, RandomThings.instance, 80, 1, true);
		registerEntity(EntitySpirit.class, "spirit", 2, RandomThings.instance, 80, 1, true);
		registerEntity(EntityArtificialEndPortal.class, "artificialEndPortal", 3, RandomThings.instance, 80, 1, true);
		registerEntity(EntityProjectedItem.class, "projectedItem", 4, RandomThings.instance, 80, 20, false);
		registerEntity(EntityTemporaryFlooFireplace.class, "flooFirePlace", 5, RandomThings.instance, 80, 20, false);
		registerEntity(EntityFallingBlockSpecial.class, "fallingBlockSpecial", 6, RandomThings.instance, 160, 20, true);
		registerEntity(EntityGoldenChicken.class, "goldenChicken", 7, RandomThings.instance, 80, 3, true);
		registerEntity(EntityGoldenEgg.class, "goldenEgg", 8, RandomThings.instance, 64, 10, true);
		registerEntity(EntityThrownWeatherEgg.class, "thrownWeatherEgg", 9, RandomThings.instance, 64, 10, true);
		registerEntity(EntityWeatherCloud.class, "weatherCloud", 10, RandomThings.instance, 160, 10, true);
		
		EntityRegistry.registerEgg(new ResourceLocation("randomthings", "spirit"), 0, 2550);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("randomthings", entityName), entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
}
