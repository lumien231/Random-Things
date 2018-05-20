package lumien.randomthings.item;

import java.lang.reflect.Field;

import lumien.randomthings.asm.MCPNames;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBottleOfAir extends ItemBase
{
	static Field inUseField = null;
	static
	{
		try
		{
			inUseField = EntityLivingBase.class.getDeclaredField(MCPNames.field("field_184628_bn"));
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

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldObj, EntityPlayer player, EnumHand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);
		if (player.isInsideOfMaterial(Material.WATER))
		{
			player.setActiveHand(hand);

			return new ActionResult(EnumActionResult.SUCCESS, itemStack);
		}
		return new ActionResult(EnumActionResult.FAIL, itemStack);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase livingEntity, int count)
	{
		super.onUsingTick(stack, livingEntity, count);

		if (livingEntity.isInsideOfMaterial(Material.WATER) || livingEntity.getAir() < 270)
		{
			if (count % 5 == 0)
			{
				if (livingEntity.getAir() < 270)
				{
					livingEntity.setAir(livingEntity.getAir() + 20);
				}
			}

			if (count == 5)
			{
				try
				{
					inUseField.set(livingEntity, 20);
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
