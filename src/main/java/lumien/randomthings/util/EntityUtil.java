package lumien.randomthings.util;

import java.lang.reflect.Field;

import lumien.randomthings.asm.MCPNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
}
