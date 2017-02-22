package lumien.randomthings.lib;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface ILuminousItem
{
	public boolean shouldGlow(ItemStack stack, int tintIndex);
}
