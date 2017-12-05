package lumien.randomthings.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lumien.randomthings.asm.MCPNames;
import net.minecraft.entity.item.EntityItem;

public class ReflectionUtil
{
	static Field entityItemAge;
	static
	{
		try
		{
			entityItemAge = EntityItem.class.getDeclaredField(MCPNames.field("field_70292_b"));
			entityItemAge.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void makeModifiable(Field nameField) throws Exception
	{
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}

	public static int getEntityItemAge(EntityItem entityItem)
	{
		try
		{
			return entityItemAge.getInt(entityItem);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
}
