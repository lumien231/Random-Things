package lumien.randomthings.item;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemItemFilter extends ItemBase
{
	public static class ItemFilterRepresentation
	{
		InventoryBasic filterInventory;

		boolean metadata = true;
		boolean nbt = true;
		boolean oreDict = false;
		int listType = 0;

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

				representation.metadata = compound.getBoolean("metadata");
				representation.oreDict = compound.getBoolean("oreDict");
				representation.nbt = compound.getBoolean("nbt");
				representation.listType = compound.getInteger("listType");
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

			compound.setBoolean("metadata", metadata);
			compound.setBoolean("oreDict", oreDict);
			compound.setBoolean("nbt", nbt);
			compound.setInteger("listType", listType);
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

		public boolean respectNBT()
		{
			return nbt;
		}

		public int getListType()
		{
			return listType;
		}

		public void toggleMetadata()
		{
			metadata = !metadata;
		}

		public void toggleOreDict()
		{
			oreDict = !oreDict;
		}

		public void toggleNBT()
		{
			nbt = !nbt;
		}

		public void toggleListType()
		{
			listType = (listType == 0 ? 1 : 0);
		}
	}

	public ItemItemFilter()
	{
		super("itemFilter");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ITEM_FILTER, worldIn, 0, 0, 0);
		}


		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
