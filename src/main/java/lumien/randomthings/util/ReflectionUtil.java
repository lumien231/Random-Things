package lumien.randomthings.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil
{
	public static void makeModifiable(Field nameField) throws Exception
	{
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}
}
