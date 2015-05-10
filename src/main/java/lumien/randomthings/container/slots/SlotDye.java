package lumien.randomthings.container.slots;

import lumien.randomthings.util.DyeUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDye extends Slot
{
	public SlotDye(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return DyeUtil.isVanillaDye(par1ItemStack);
	}
}
