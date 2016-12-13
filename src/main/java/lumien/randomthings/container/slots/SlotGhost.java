package lumien.randomthings.container.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotGhost extends Slot
{
	public SlotGhost(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		ItemStack holding = playerIn.inventory.getItemStack();

		if (!holding.func_190926_b())
		{
			holding = holding.copy();
			holding.func_190920_e(1);
		}
		this.putStack(holding);
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		ItemStack copy = stack.copy();
		copy.func_190920_e(1);
		this.putStack(copy);
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		this.putStack(ItemStack.field_190927_a);
		return ItemStack.field_190927_a;
	}
}
