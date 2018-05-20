package lumien.randomthings.container.slots;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFilteredItemHandler extends SlotItemHandler
{
	Predicate<ItemStack> filterPredicate;

	public SlotFilteredItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> filterPredicate)
	{
		super(itemHandler, index, xPosition, yPosition);

		this.filterPredicate = filterPredicate;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return filterPredicate.apply(stack);
	}
}
