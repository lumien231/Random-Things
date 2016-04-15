package lumien.randomthings.container;

import lumien.randomthings.container.inventories.InventoryItem;
import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotOutputOnly;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ContainerEnderLetter extends Container
{
	ItemStack enderLetterStack;

	boolean received;

	InventoryItem inventory;

	public ContainerEnderLetter(EntityPlayer player, World world, int x, int y, int z)
	{
		enderLetterStack = player.inventory.getCurrentItem();
		inventory = new InventoryItem("EnderLetterContent", 9, enderLetterStack);

		NBTTagCompound compound;

		if ((compound = enderLetterStack.getTagCompound()) != null)
		{
			received = compound.getBoolean("received");
		}

		for (int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(received ? new SlotOutputOnly(inventory, i, 8 + i * 18, 18) : new Slot(inventory, i, 8 + i * 18, 18));
		}

		bindPlayerInventory(player.inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return ItemStack.areItemsEqual(inventory.getItemStack(), playerIn.getHeldItemMainhand());
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		if (received && !playerIn.worldObj.isRemote && InventoryUtil.isInventoryEmpty(inventory))
		{
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
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
			if (inventoryPlayer.getStackInSlot(i) == inventoryPlayer.player.getHeldItemMainhand())
			{
				addSlotToContainer(new SlotDisplay(inventoryPlayer, i, 8 + i * 18, 109));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 109));
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 9)
			{
				if (!this.mergeItemStack(itemstack1, 9, 45, true))
				{
					return null;
				}
			}
			else if (!received && !this.mergeItemStack(itemstack1, 0, 8, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
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
			while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack))
				{
					int l = itemstack1.stackSize + par1ItemStack.stackSize;

					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize = 0;
						itemstack1.stackSize = l;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = par1ItemStack.getMaxStackSize();
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

		if (par1ItemStack.stackSize > 0)
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

				if (itemstack1 == null && slot.isItemValid(par1ItemStack))
				{
					if (1 < par1ItemStack.stackSize)
					{
						ItemStack copy = par1ItemStack.copy();
						copy.stackSize = 1;
						slot.putStack(copy);

						par1ItemStack.stackSize -= 1;
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(par1ItemStack.copy());
						slot.onSlotChanged();
						par1ItemStack.stackSize = 0;
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
