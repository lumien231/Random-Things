package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotItemHandlerOutputOnly;
import lumien.randomthings.tileentity.TileEntityImbuingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerImbuingStation extends Container
{
	TileEntityImbuingStation te;
	public int lastImbuingProgress;

	public ContainerImbuingStation(EntityPlayer player, World world, int x, int y, int z)
	{
		this.te = (TileEntityImbuingStation) world.getTileEntity(new BlockPos(x, y, z));

		IItemHandler itemHandler = te.getItemHandler();

		this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 80, 9));
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 1, 35, 54));
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 2, 80, 99));
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 3, 80, 54));
		this.addSlotToContainer(new SlotItemHandlerOutputOnly(itemHandler, 4, 125, 54));

		bindPlayerInventory(player.inventory);

		lastImbuingProgress = 0;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 184));
		}
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

			if (par2 < 5)
			{
				if (!this.mergeItemStack(itemstack1, 5, 41, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 4, false))
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

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int identifier, int value)
	{
		if (identifier == 0)
		{
			this.te.imbuingProgress = value;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendWindowProperty(this, 0, this.te.imbuingProgress);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icrafting = this.listeners.get(i);

			if (this.lastImbuingProgress != this.te.imbuingProgress)
			{
				icrafting.sendWindowProperty(this, 0, this.te.imbuingProgress);
			}
		}

		this.lastImbuingProgress = te.imbuingProgress;
	}
}
