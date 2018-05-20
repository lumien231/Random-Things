package lumien.randomthings.container.slots;

import com.google.common.base.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot
{
	Predicate<ItemStack> filterPredicate;

	public SlotFiltered(IInventory inventoryIn, int index, int xPosition, int yPosition, Predicate<ItemStack> filterPredicate)
	{
		super(inventoryIn, index, xPosition, yPosition);

		this.filterPredicate = filterPredicate;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return filterPredicate.apply(stack);
	}
}
