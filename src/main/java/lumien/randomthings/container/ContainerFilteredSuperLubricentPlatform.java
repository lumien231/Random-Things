package lumien.randomthings.container;

import com.google.common.base.Predicate;

import lumien.randomthings.container.slots.SlotFiltered;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityFilteredSuperLubricentPlatform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerFilteredSuperLubricentPlatform extends Container
{
	TileEntityFilteredSuperLubricentPlatform filteredSuperLubricentPlatform;
	BlockPos pos;

	public ContainerFilteredSuperLubricentPlatform(EntityPlayer player, World world, int x, int y, int z)
	{
		this.pos = new BlockPos(x, y, z);
		filteredSuperLubricentPlatform = (TileEntityFilteredSuperLubricentPlatform) world.getTileEntity(this.pos);

		IInventory inventory = filteredSuperLubricentPlatform.getInventory();

		this.addSlotToContainer(new SlotFiltered(inventory, 0, 80, 10, new Predicate<ItemStack>()
		{
			@Override
			public boolean apply(ItemStack input)
			{
				return !input.isEmpty() && input.getItem() == ModItems.itemFilter;
			}
		}));

		bindPlayerInventory(player.inventory);
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

			if (par2 < 1)
			{
				if (!this.mergeItemStack(itemstack1, 1, 37, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 1, false))
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

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 47 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 105));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return playerIn.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

}
