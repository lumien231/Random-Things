package lumien.randomthings.item;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFlooPouch extends ItemBase
{

	public ItemFlooPouch()
	{
		super("flooPouch");

		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(getFlooCount(stack) + " / " + 128);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (playerIn.isSneaking())
		{
			ItemStack me = playerIn.getHeldItem(handIn);
			int flooCount = getFlooCount(me);

			if (flooCount < 128)
			{
				for (int slot = 0; slot < playerIn.inventory.getSizeInventory() && flooCount < 128; slot++)
				{
					ItemStack slotItem = playerIn.inventory.getStackInSlot(slot);

					if (!slotItem.isEmpty() && slotItem.getItem() == ModItems.ingredients && slotItem.getItemDamage() == ItemIngredient.INGREDIENT.FLOO_POWDER.id)
					{
						int used = Math.min(slotItem.getCount(), 128 - flooCount);

						flooCount += used;

						slotItem.shrink(used);
					}
				}
				
				ItemFlooPouch.setFlooCount(me, flooCount);
			}

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, me);
		}
		else
		{
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}

	public static void setFlooCount(ItemStack stack, int count)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound compound = stack.getTagCompound();

		compound.setInteger("flooCount", count);
	}

	public static int getFlooCount(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound compound = stack.getTagCompound();

			return compound.getInteger("flooCount");
		}
		else
		{
			return 0;
		}
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return 1 - (double) getFlooCount(stack) / 128;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return Color.GREEN.getRGB();
	}
}
