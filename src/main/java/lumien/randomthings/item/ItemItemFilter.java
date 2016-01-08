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
import net.minecraftforge.oredict.OreDictionary;

public class ItemItemFilter extends ItemBase
{
	public static class ItemFilterRepresentation
	{
		InventoryBasic filterInventory;

		boolean metadata;
		boolean nbt;
		boolean oreDict;
		int listType;

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

		public boolean matchesItemStack(ItemStack stackToCheck)
		{
			boolean matches = false;

			for (int i = 0; i < filterInventory.getSizeInventory(); i++)
			{
				ItemStack item = filterInventory.getStackInSlot(i);

				if (item != null)
				{
					if (oreDict)
					{
						int[] idList = OreDictionary.getOreIDs(item);

						int[] checkList = OreDictionary.getOreIDs(stackToCheck);

						for (int id : idList)
						{
							for (int id2 : checkList)
							{
								if (id == id2)
								{
									matches = true;
									break;
								}
							}
							if (matches)
							{
								break;
							}
						}
					}

					if (matches)
					{
						break;
					}


					if (item.getItem() == stackToCheck.getItem())
					{
						if (!metadata || item.getItemDamage() == stackToCheck.getItemDamage())
						{
							if (!nbt || ItemStack.areItemStackTagsEqual(item, stackToCheck))
							{
								matches = true;
								break;
							}
						}
					}
				}
			}

			if (listType == 0)
			{
				return matches;
			}
			else
			{
				return !matches;
			}
		}

		public boolean respectOreDictionary()
		{
			return oreDict;
		}

		public boolean respectMetadata()
		{
			return metadata;
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
