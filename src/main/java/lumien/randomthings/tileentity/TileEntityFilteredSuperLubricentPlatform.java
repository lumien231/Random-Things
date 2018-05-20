package lumien.randomthings.tileentity;

import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFilteredSuperLubricentPlatform extends TileEntityBase implements IInventoryChangedListener
{
	ItemFilterRepresentation repres;

	InventoryBasic filterInventory;

	boolean reading;

	public TileEntityFilteredSuperLubricentPlatform()
	{
		filterInventory = new InventoryBasic("FilteredSuperLubricentPlatform", false, 1);
		repres = null;
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

	public ItemFilterRepresentation getRepres()
	{
		return repres;
	}

	@Override
	public void onInventoryChanged(IInventory inventory)
	{
		ItemStack filter = inventory.getStackInSlot(0);

		if (filter != null)
		{
			repres = ItemFilterRepresentation.readFromItemStack(filter);
		}
		else
		{
			repres = null;
		}

		if (!reading)
		{
			this.syncTE();
		}
	}

}
