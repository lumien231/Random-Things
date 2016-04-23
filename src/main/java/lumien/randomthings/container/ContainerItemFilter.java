package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotGhost;
import lumien.randomthings.item.ItemItemFilter;
import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ContainerItemFilter extends Container
{
	EnumHand using;
	ItemStack filterStack;
	public ItemFilterRepresentation repres;

	public ContainerItemFilter(EntityPlayer player, World world, int x, int y, int z)
	{
		filterStack = player.getHeldItemMainhand();
		using = EnumHand.MAIN_HAND;
		if (filterStack == null)
		{
			filterStack = player.getHeldItemOffhand();
			using = EnumHand.OFF_HAND;
		}

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
		else
		{
			filterStack = null;
		}

		bindPlayerInventory(player.inventory);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return null;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return filterStack != null && playerIn.getHeldItem(using) == filterStack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		repres.writeToItemStack();
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
			if (inventoryPlayer.getStackInSlot(i) == filterStack)
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
