package lumien.randomthings.container.inventories;

import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryItem implements IInventory
{
	String name;
	int size;

	ItemStack itemStack = ItemStack.EMPTY;
	NonNullList<ItemStack> cacheInventory;

	boolean reading;

	public InventoryItem(String name, int size, ItemStack itemStack)
	{
		this.name = name;
		this.size = size;
		this.itemStack = itemStack;
		this.cacheInventory = NonNullList.withSize(size, ItemStack.EMPTY);
		this.reading = false;

		loadInventory();
	}

	private void loadInventory()
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound mainCompound = itemStack.getTagCompound();
		if (mainCompound.hasKey(name))
		{
			NBTTagCompound inventoryCompound = mainCompound.getCompoundTag(name);

			this.reading = true;
			InventoryUtil.readInventoryFromCompound(inventoryCompound, this);

			this.reading = false;
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation(getName());
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return cacheInventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (!this.cacheInventory.get(index).isEmpty())
		{
			ItemStack itemstack;

			if (this.cacheInventory.get(index).getCount() <= count)
			{
				itemstack = this.cacheInventory.get(index);
				this.cacheInventory.set(index, ItemStack.EMPTY);
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.cacheInventory.get(index).splitStack(count);

				if (this.cacheInventory.get(index).getCount() == 0)
				{
					this.cacheInventory.set(index, ItemStack.EMPTY);
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (!this.cacheInventory.get(index).isEmpty())
		{
			ItemStack itemstack = this.cacheInventory.get(index);
			this.cacheInventory.set(index, ItemStack.EMPTY);
			return itemstack;
		}
		else
		{
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.cacheInventory.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		if (!reading)
		{
			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound compound = itemStack.getTagCompound().getCompoundTag(name);

		InventoryUtil.writeInventoryToCompound(compound, this);

		itemStack.getTagCompound().setTag(name, compound);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.cacheInventory.size(); ++i)
		{
			this.cacheInventory.set(i, ItemStack.EMPTY);
		}
	}

	public ItemStack getItemStack()
	{
		return itemStack;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.cacheInventory)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

}
