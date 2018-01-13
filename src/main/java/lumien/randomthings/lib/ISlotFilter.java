package lumien.randomthings.lib;

import net.minecraft.item.ItemStack;

public interface ISlotFilter
{
	public boolean isItemStackValid(ItemStack is);
}
