package lumien.randomthings.util;

import java.lang.reflect.Field;

import lumien.randomthings.asm.MCPNames;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;

public class EntityUtil
{
	static Field isJumping;

	static
	{
		try
		{
			isJumping = EntityLivingBase.class.getDeclaredField(MCPNames.field("field_70703_bu"));
			isJumping.setAccessible(true);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isJumping(EntityLivingBase entity)
	{
		try
		{
			return isJumping.getBoolean(entity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static String getEntityName(Entity entity)
	{
		String entityName = null;
		entityName = EntityList.getEntityString(entity);

		if (entityName == null)
		{
			EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(entity.getClass(), false);

			if (registration != null)
			{
				entityName = registration.getEntityName();
			}
		}
		
		return entityName;
	}
}
