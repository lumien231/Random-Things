package lumien.randomthings.item;

import java.lang.reflect.Field;

import lumien.randomthings.asm.MCPNames;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ItemBottleOfAir extends ItemBase
{
	static Field inUseField = null;
	static
	{
		try
		{
			inUseField = EntityPlayer.class.getDeclaredField(MCPNames.field("field_71072_f"));
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		
		inUseField.setAccessible(true);
	}

	public ItemBottleOfAir()
	{
		super("bottleOfAir");
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World worldObj, EntityPlayer player)
	{
		if (player.isInsideOfMaterial(Material.water))
		{
			player.setItemInUse(itemStack, getMaxItemUseDuration(itemStack));
		}
		return itemStack;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		super.onUsingTick(stack, player, count);

		if (player.isInsideOfMaterial(Material.water) || player.getAir()<270)
		{
			if (count % 5 == 0)
			{
				if (player.getAir() < 270)
				{
					player.setAir(player.getAir() + 20);
				}
			}

			if (count == 5)
			{
				try
				{
					inUseField.set(player, 20);
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
