package lumien.randomthings.tileentity;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityImbuingStation extends TileEntityBase implements ITickable
{
	public int imbuingProgress;

	ItemStack currentOutput = ItemStack.EMPTY;

	public final static int IMBUING_LENGTH = 200;

	public TileEntityImbuingStation()
	{
		this.setItemHandler(5);
		this.setItemHandlerPublic(new int[] { 0, 1, 2, 3 }, new int[] { 4 });
	}

	@Override
	public void writeDataToNBT(NBTTagCompound nbt, boolean sync)
	{
		nbt.setInteger("imbuingProgress", imbuingProgress);

		if (!currentOutput.isEmpty())
		{
			NBTTagCompound outputCompound = new NBTTagCompound();
			currentOutput.writeToNBT(outputCompound);
			nbt.setTag("output", outputCompound);
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound nbt, boolean sync)
	{
		this.imbuingProgress = nbt.getInteger("imbuingProgress");

		boolean inventoryThere = false;
		for (int slot = 0; slot < getItemHandler().getSlots(); slot++)
		{
			if (nbt.hasKey("slot" + slot))
			{
				inventoryThere = true;
				break;
			}
		}

		if (inventoryThere)
		{
			InventoryBasic inventory = new InventoryBasic("", false, getItemHandler().getSlots());
			InventoryUtil.readInventoryFromCompound(nbt, inventory);
			for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
			{
				((ItemStackHandler) this.getItemHandler()).setStackInSlot(slot, inventory.getStackInSlot(slot));
			}

			RandomThings.instance.logger.log(Level.DEBUG, "Switching Imbuing Station to Item Handler...");
		}

		if (nbt.hasKey("output"))
		{
			currentOutput = new ItemStack(nbt.getCompoundTag("output"));
		}
	}

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			ItemStack validOutput = ImbuingRecipeHandler.getRecipeOutput(getItemHandler());
			if (!ItemStack.areItemStacksEqual(validOutput, currentOutput) && canHandleOutput(validOutput))
			{
				this.imbuingProgress = 0;
				currentOutput = validOutput;
			}

			if (!this.currentOutput.isEmpty())
			{
				this.imbuingProgress++;
				if (this.imbuingProgress >= IMBUING_LENGTH)
				{
					imbuingProgress = 0;
					imbue();
				}
			}
			else
			{
				this.imbuingProgress = 0;
			}
		}
	}

	private boolean canHandleOutput(ItemStack validOutput)
	{
		if (validOutput.isEmpty() || getItemHandler().getStackInSlot(4).isEmpty())
		{
			return true;
		}
		else
		{
			ItemStack currentInOutput = getItemHandler().getStackInSlot(4);
			ItemStack requiredOutput = validOutput;

			if (!(ItemStack.areItemsEqual(currentInOutput, requiredOutput) && ItemStack.areItemStackTagsEqual(currentInOutput, requiredOutput)))
			{
				return false;
			}
			else
			{
				if (currentInOutput.getCount() + requiredOutput.getCount() > currentInOutput.getMaxStackSize())
				{
					return false;
				}
			}
		}
		return true;
	}

	private void imbue()
	{
		// Set Output
		this.getItemHandler().insertItem(4, currentOutput.copy(), false);

		// Decrease Ingredients
		for (int slot = 0; slot < this.getItemHandler().getSlots() - 1; slot++)
		{
			this.getItemHandler().extractItem(slot, 1, false);
		}
	}
}
