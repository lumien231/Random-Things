package lumien.randomthings.container.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotDisplay extends Slot
{
	public SlotDisplay(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return false;
	}
}
