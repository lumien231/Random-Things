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

public class ReflectionUtilClient
{
	static Field pointedEntity;
	
	static
	{
		try
		{
			pointedEntity = EntityRenderer.class.getDeclaredField(MCPNames.field("field_78528_u"));
			pointedEntity.setAccessible(true);
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
	
	@SideOnly(Side.CLIENT)
	public static Entity getPointedEntity(EntityRenderer entityRenderer)
	{
		try
		{
			return (Entity) pointedEntity.get(entityRenderer);
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
