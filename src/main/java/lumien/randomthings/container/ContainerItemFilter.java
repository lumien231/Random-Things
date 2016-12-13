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
	ItemStack filterStack = ItemStack.field_190927_a;
	public ItemFilterRepresentation repres;

	public ContainerItemFilter(EntityPlayer player, World world, int x, int y, int z)
	{
		filterStack = player.getHeldItemMainhand();
		using = EnumHand.MAIN_HAND;
		if (filterStack.func_190926_b())
		{
			filterStack = player.getHeldItemOffhand();
			using = EnumHand.OFF_HAND;
		}

		if (!filterStack.func_190926_b() && filterStack.getItem() == ModItems.itemFilter)
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
			filterStack = ItemStack.field_190927_a;
		}

		bindPlayerInventory(player.inventory);
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn)
	{
		return false;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.field_190927_a;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();

			if (par2 < 9)
			{
				return ItemStack.field_190927_a;
			}
			else
			{
				for (int i = 0; i < 9; i++)
				{
					Slot s = this.inventorySlots.get(i);
					ItemStack ghostItem = s.getStack();
					
					if (ghostItem.func_190926_b())
					{
						itemstack = itemstack1.copy();
						itemstack.func_190920_e(1);
						
						s.putStack(itemstack);
						return ItemStack.field_190927_a;
					}
				}
			}
		}

		return ItemStack.field_190927_a;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !filterStack.func_190926_b() && playerIn.getHeldItem(using) == filterStack;
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
