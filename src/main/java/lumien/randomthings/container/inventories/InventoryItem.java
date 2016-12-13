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

	ItemStack itemStack = ItemStack.field_190927_a;
	NonNullList<ItemStack> cacheInventory;

	boolean reading;

	public InventoryItem(String name, int size, ItemStack itemStack)
	{
		this.name = name;
		this.size = size;
		this.itemStack = itemStack;
		this.cacheInventory = NonNullList.func_191197_a(size, ItemStack.field_190927_a);
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
		if (!this.cacheInventory.get(index).func_190926_b())
		{
			ItemStack itemstack;

			if (this.cacheInventory.get(index).func_190916_E() <= count)
			{
				itemstack = this.cacheInventory.get(index);
				this.cacheInventory.set(index, ItemStack.field_190927_a);
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.cacheInventory.get(index).splitStack(count);

				if (this.cacheInventory.get(index).func_190916_E() == 0)
				{
					this.cacheInventory.set(index, ItemStack.field_190927_a);
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return ItemStack.field_190927_a;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (!this.cacheInventory.get(index).func_190926_b())
		{
			ItemStack itemstack = this.cacheInventory.get(index);
			this.cacheInventory.set(index, ItemStack.field_190927_a);
			return itemstack;
		}
		else
		{
			return ItemStack.field_190927_a;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.cacheInventory.set(index, stack);

		if (!stack.func_190926_b() && stack.func_190916_E() > this.getInventoryStackLimit())
		{
			stack.func_190920_e(this.getInventoryStackLimit());
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
	public boolean isUseableByPlayer(EntityPlayer player)
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
			this.cacheInventory.set(i, ItemStack.field_190927_a);
		}
	}

	public ItemStack getItemStack()
	{
		return itemStack;
	}

	@Override
	public boolean func_191420_l()
	{
		for (ItemStack itemstack : this.cacheInventory)
		{
			if (!itemstack.func_190926_b())
			{
				return false;
			}
		}

		return true;
	}

}
