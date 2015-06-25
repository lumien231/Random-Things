package lumien.randomthings.container.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotDyeable extends Slot
{

	public SlotDyeable(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItem() instanceof ItemBlock)
		{
			return false;
		}

		return true;
	}
}
