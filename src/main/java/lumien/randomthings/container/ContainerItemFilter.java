package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotGhost;
import lumien.randomthings.item.ItemItemFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerItemFilter extends Container
{
	ItemFilterRepresentation repres;

	public ContainerItemFilter(EntityPlayer player, World world, int x, int y, int z)
	{
		ItemStack filterStack = player.getCurrentEquippedItem();

		if (filterStack != null && filterStack.getItem() == ModItems.itemFilter)
		{
			repres = ItemItemFilter.ItemFilterRepresentation.readFromItemStack(filterStack);

			for (int row = 0; row < 1; row++)
			{
				for (int col = 0; col < 9; ++col)
				{
					addSlotToContainer(new SlotGhost(repres.getFilterInventory(), col + row * 9, 8 + col * 18, 18 + row * 18));
				}
			}
		}

		bindPlayerInventory(player.inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == ModItems.itemFilter;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		ItemStack filterStack = playerIn.getCurrentEquippedItem();

		if (filterStack != null && filterStack.getItem() == ModItems.itemFilter)
		{
			repres.writeToItemStack(filterStack);
		}
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			if (inventoryPlayer.getStackInSlot(i) == inventoryPlayer.player.getCurrentEquippedItem())
			{
				addSlotToContainer(new SlotDisplay(inventoryPlayer, i, 8 + i * 18, 109));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 109));
			}
		}
	}
}
