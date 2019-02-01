package lumien.randomthings.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lumien.randomthings.asm.MCPNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.village.Village;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.ItemModelMesherForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IRegistryDelegate;

public class ReflectionUtil
{
	static Field entityItemAge;
	static Field simpleShapes;
	static Field village;
	static Field biomeName;
	static
	{
		try
		{
			entityItemAge = EntityItem.class.getDeclaredField(MCPNames.field("field_70292_b"));
			entityItemAge.setAccessible(true);

			village = EntityVillager.class.getDeclaredField(MCPNames.field("field_70954_d"));
			village.setAccessible(true);

			biomeName = Biome.class.getDeclaredField(MCPNames.field("field_76791_y"));
			biomeName.setAccessible(true);
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

	public static String getBiomeName(Biome b)
	{
		try
		{
			return (String) biomeName.get(b);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return "";
	}

	public static Village getVillage(EntityVillager villager)
	{
		try
		{
			return (Village) village.get(villager);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
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

	@SideOnly(Side.CLIENT)
	public static Map<IRegistryDelegate<Item>, Int2ObjectMap<ModelResourceLocation>> getModelMap()
	{
		if (simpleShapes == null)
		{
			try
			{
				simpleShapes = ItemModelMesherForge.class.getDeclaredField("locations");
			}
			catch (NoSuchFieldException e)
			{
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			simpleShapes.setAccessible(true);
		}

		try
		{
			return (Map<IRegistryDelegate<Item>, Int2ObjectMap<ModelResourceLocation>>) simpleShapes.get(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
