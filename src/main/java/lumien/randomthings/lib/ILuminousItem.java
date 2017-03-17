package lumien.randomthings.lib;

import net.minecraft.item.ItemStack;

public interface ILuminousItem
{
	public boolean shouldGlow(ItemStack stack, int tintIndex);
}
