package lumien.randomthings.tileentity;

import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFilteredItemRedirector extends TileEntityBase implements IInventoryChangedListener
{
	ItemFilterRepresentation[] repres;

	InventoryBasic filterInventory;

	boolean reading;

	public TileEntityFilteredItemRedirector()
	{
		filterInventory = new InventoryBasic("FilteredItemRedirector", false, 2);
		repres = new ItemFilterRepresentation[2];
		filterInventory.addInventoryChangeListener(this);
	}

	public IInventory getInventory()
	{
		return filterInventory;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		NBTTagCompound inventoryCompound = new NBTTagCompound();
		InventoryUtil.writeInventoryToCompound(inventoryCompound, filterInventory);

		compound.setTag("inventoryCompound", inventoryCompound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		NBTTagCompound inventoryCompound = compound.getCompoundTag("inventoryCompound");

		reading = true;
		InventoryUtil.readInventoryFromCompound(inventoryCompound, filterInventory);
		reading = false;
	}

	public ItemFilterRepresentation[] getRepres()
	{
		return repres;
	}

	@Override
	public void onInventoryChanged(IInventory inventory)
	{
		ItemStack f1 = inventory.getStackInSlot(0);
		ItemStack f2 = inventory.getStackInSlot(1);

		if (f1 != null)
		{
			repres[0] = ItemFilterRepresentation.readFromItemStack(f1);
		}
		else
		{
			repres[0] = null;
		}

		if (f2 != null)
		{
			repres[1] = ItemFilterRepresentation.readFromItemStack(f2);
		}
		else
		{
			repres[1] = null;
		}
		
		if (!reading)
		{
			this.syncTE();
		}
	}

}
