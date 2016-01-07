package lumien.randomthings.item;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemItemFilter extends ItemBase
{
	public static class ItemFilterRepresentation
	{
		InventoryBasic filterInventory;

		public ItemFilterRepresentation()
		{
			filterInventory = new InventoryBasic("ItemFilter", false, 9);
		}

		public IInventory getFilterInventory()
		{
			return filterInventory;
		}

		public static ItemFilterRepresentation readFromItemStack(ItemStack filterStack)
		{
			ItemFilterRepresentation representation = new ItemFilterRepresentation();
			NBTTagCompound compound;

			if ((compound = filterStack.getTagCompound()) != null)
			{
				NBTTagCompound inventoryCompound;

				if ((inventoryCompound = compound.getCompoundTag("inventory")) != null)
				{
					InventoryUtil.readInventoryFromCompound(inventoryCompound, representation.filterInventory);
				}
			}

			return representation;
		}

		public void writeToItemStack(ItemStack filterStack)
		{
			NBTTagCompound compound = filterStack.getTagCompound();
			if (compound == null)
			{
				filterStack.setTagCompound(compound = new NBTTagCompound());
			}

			NBTTagCompound inventoryCompound = new NBTTagCompound();
			
			InventoryUtil.writeInventoryToCompound(inventoryCompound, filterInventory);
			
			compound.setTag("inventory", inventoryCompound);
		}
	}

	public ItemItemFilter()
	{
		super("itemFilter");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ITEM_FILTER, worldIn, 0, 0, 0);
		}


		return itemStackIn;
	}
}
