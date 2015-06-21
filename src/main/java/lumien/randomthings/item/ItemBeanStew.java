package lumien.randomthings.item;

import lumien.randomthings.util.RegisterUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBeanStew extends ItemFood
{
	public ItemBeanStew()
	{
		super(8, false);

		RegisterUtil.registerItem(this, "beanStew");
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		super.onItemUseFinish(stack, worldIn, playerIn);

		if (stack.stackSize == 0)
		{
			return new ItemStack(Items.bowl);
		}
		else
		{
			boolean inventory = playerIn.inventory.addItemStackToInventory(new ItemStack(Items.bowl));

			if (!inventory && !worldIn.isRemote)
			{
				worldIn.spawnEntityInWorld(new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, new ItemStack(Items.bowl)));
			}
			return stack;
		}
	}
}
