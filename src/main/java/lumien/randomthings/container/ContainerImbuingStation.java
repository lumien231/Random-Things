package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotOutputOnly;
import lumien.randomthings.tileentity.TileEntityImbuingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerImbuingStation extends Container
{
	TileEntityImbuingStation te;
	public int lastImbuingProgress;

	public ContainerImbuingStation(EntityPlayer player, World world, int x, int y, int z)
	{
		this.te = (TileEntityImbuingStation) world.getTileEntity(new BlockPos(x,y,z));

		this.addSlotToContainer(new Slot(te, 0, 80, 9));
		this.addSlotToContainer(new Slot(te, 1, 35, 54));
		this.addSlotToContainer(new Slot(te, 2, 80, 99));
		this.addSlotToContainer(new Slot(te, 3, 80, 54));
		this.addSlotToContainer(new SlotOutputOnly(te, 4, 125, 54));

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
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 5)
			{
				if (!this.mergeItemStack(itemstack1, 5, 41, true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 4, false))
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
				slot = (Slot) this.inventorySlots.get(k);
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
				slot = (Slot) this.inventorySlots.get(k);
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
		return te.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onCraftGuiOpened(ICrafting listener)
    {
		super.onCraftGuiOpened(listener);
		listener.sendProgressBarUpdate(this, 0, this.te.imbuingProgress);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.lastImbuingProgress != this.te.imbuingProgress)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.te.imbuingProgress);
			}
		}

		this.lastImbuingProgress = te.imbuingProgress;
	}
}
