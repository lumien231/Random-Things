package lumien.randomthings.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerWrapper implements IItemHandler
{
	IItemHandler actualHandler;

	int[] insertSlots;
	int[] outputSlots;

	public ItemHandlerWrapper(IItemHandler actualHandler, int[] insertSlots, int[] outputSlots)
	{
		this.actualHandler = actualHandler;

		this.insertSlots = insertSlots;
		this.outputSlots = outputSlots;
	}

	@Override
	public int getSlots()
	{
		return actualHandler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return actualHandler.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		for (int i = 0; i < insertSlots.length; i++)
		{
			if (insertSlots[i] == slot)
			{
				return actualHandler.insertItem(slot, stack, simulate);
			}
		}

		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		for (int i = 0; i < outputSlots.length; i++)
		{
			if (outputSlots[i] == slot)
			{
				return actualHandler.extractItem(slot, amount, simulate);
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return actualHandler.getSlotLimit(slot);
	}

}
