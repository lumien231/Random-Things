package lumien.randomthings.container;

import com.google.common.base.Predicate;

import lumien.randomthings.container.inventories.InventoryItem;
import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotFiltered;
import lumien.randomthings.container.slots.SlotGhost;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ContainerRedstoneRemote extends Container
{
	InventoryItem remoteInventory;
	EnumHand using;
	ItemStack remoteStack;

	public ContainerRedstoneRemote(EntityPlayer player, World world, int x, int y, int z)
	{
		remoteStack = player.getHeldItemMainhand();
		using = EnumHand.MAIN_HAND;
		if (remoteStack.isEmpty())
		{
			remoteStack = player.getHeldItemOffhand();
			using = EnumHand.OFF_HAND;
		}

		if (!remoteStack.isEmpty() && remoteStack.getItem() == ModItems.redstoneRemote)
		{
			remoteInventory = new InventoryItem("RedstoneRemote", 18, remoteStack);

			int row = 0;

			for (int col = 0; col < 9; ++col)
			{
				addSlotToContainer(new SlotFiltered(remoteInventory, col + row * 9, 8 + col * 18, 18 + row * 18, new Predicate<ItemStack>()
				{

					@Override
					public boolean apply(ItemStack input)
					{
						return !input.isEmpty() && input.getItem() == ModItems.positionFilter;
					}
				}));
			}

			row = 1;

			for (int col = 0; col < 9; ++col)
			{
				addSlotToContainer(new SlotGhost(remoteInventory, col + row * 9, 8 + col * 18, 18 + row * 18));
			}
		}
		else
		{
			remoteStack = ItemStack.EMPTY;
		}

		bindPlayerInventory(player.inventory);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 68 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			if (inventoryPlayer.getStackInSlot(i) == remoteStack)
			{
				addSlotToContainer(new SlotDisplay(inventoryPlayer, i, 8 + i * 18, 126));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 126));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !remoteStack.isEmpty() && playerIn.getHeldItem(using) == remoteStack;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 9)
			{
				if (!this.mergeItemStack(itemstack1, 18, 54, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (par2 > 17 && !this.mergeItemStack(itemstack1, 0, 9, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
	{
		boolean flag1 = false;
		int k = par2;

		if (par4)
		{
			k = par3 - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (par1ItemStack.isStackable())
		{
			while (par1ItemStack.getCount() > 0 && (!par4 && k < par3 || par4 && k >= par2))
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!itemstack1.isEmpty() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack))
				{
					int l = itemstack1.getCount() + par1ItemStack.getCount();

					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.setCount(0);
						itemstack1.setCount(l);
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.shrink(par1ItemStack.getMaxStackSize() - itemstack1.getCount());
						itemstack1.setCount(par1ItemStack.getMaxStackSize());
						slot.onSlotChanged();
						flag1 = true;
					}
				}

				if (par4)
				{
					--k;
				}
				else
				{
					++k;
				}
			}
		}

		if (par1ItemStack.getCount() > 0)
		{
			if (par4)
			{
				k = par3 - 1;
			}
			else
			{
				k = par2;
			}

			while (!par4 && k < par3 || par4 && k >= par2)
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1.isEmpty() && slot.isItemValid(par1ItemStack))
				{
					if (1 < par1ItemStack.getCount())
					{
						ItemStack copy = par1ItemStack.copy();
						copy.setCount(1);
						slot.putStack(copy);

						par1ItemStack.shrink(1);
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(par1ItemStack.copy());
						slot.onSlotChanged();
						par1ItemStack.setCount(0);
						flag1 = true;
						break;
					}
				}

				if (par4)
				{
					--k;
				}
				else
				{
					++k;
				}
			}
		}
		return flag1;
	}
}
