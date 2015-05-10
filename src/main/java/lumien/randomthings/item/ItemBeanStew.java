package lumien.randomthings.item;

import lumien.randomthings.util.RegisterUtil;
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
		
		return new ItemStack(Items.bowl);
	}
}
