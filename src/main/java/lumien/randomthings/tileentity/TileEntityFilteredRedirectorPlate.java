package lumien.randomthings.tileentity;

import lumien.randomthings.lib.EntityFilterItemStack;
import lumien.randomthings.lib.IEntityFilterItem;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFilteredRedirectorPlate extends TileEntityBase implements IInventoryChangedListener
{
	EntityFilterItemStack[] filter;

	InventoryBasic filterInventory;

	boolean reading;

	public TileEntityFilteredRedirectorPlate()
	{
		filterInventory = new InventoryBasic("FilteredRedirectorPlate", false, 2);
		filter = new EntityFilterItemStack[2];
		filterInventory.addInventoryChangeListener(this);
	}

	public IInventory getInventory()
	{
		return filterInventory;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagCompound inventoryCompound = new NBTTagCompound();
		InventoryUtil.writeInventoryToCompound(inventoryCompound, filterInventory);

		compound.setTag("inventoryCompound", inventoryCompound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagCompound inventoryCompound = compound.getCompoundTag("inventoryCompound");

		reading = true;
		InventoryUtil.readInventoryFromCompound(inventoryCompound, filterInventory);
		reading = false;
	}

	@Override
	public void onInventoryChanged(IInventory inventory)
	{
		ItemStack f1 = inventory.getStackInSlot(0);
		ItemStack f2 = inventory.getStackInSlot(1);

		if (!f1.isEmpty() && f1.getItem() instanceof IEntityFilterItem)
		{
			filter[0] = new EntityFilterItemStack(f1, (IEntityFilterItem) f1.getItem());
		}
		else
		{
			filter[0] = null;
		}

		if (!f2.isEmpty() && f2.getItem() instanceof IEntityFilterItem)
		{
			filter[1] = new EntityFilterItemStack(f2, (IEntityFilterItem) f2.getItem());
		}
		else
		{
			filter[1] = null;
		}

		if (!reading)
		{
			this.syncTE();
		}
	}

	public EntityFilterItemStack[] getFilter()
	{
		return filter;
	}

}
