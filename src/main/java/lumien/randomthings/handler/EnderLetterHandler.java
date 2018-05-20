package lumien.randomthings.handler;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class EnderLetterHandler extends WorldSavedData
{
	public static String ID = "RTEnderLetters";

	HashMap<UUID, EnderMailboxInventory> inventoryMap;

	public EnderLetterHandler(String name)
	{
		super(name);

		inventoryMap = new HashMap<>();
	}

	public EnderLetterHandler()
	{
		super(ID);

		inventoryMap = new HashMap<>();
	}

	public EnderMailboxInventory getOrCreateInventoryForPlayer(UUID playerUUID)
	{
		if (inventoryMap.containsKey(playerUUID))
		{
			return inventoryMap.get(playerUUID);
		}
		else
		{
			inventoryMap.put(playerUUID, new EnderMailboxInventory(this));
			this.markDirty();
			return inventoryMap.get(playerUUID);
		}
	}

	public static EnderLetterHandler get(World worldObj)
	{
		EnderLetterHandler handler = (EnderLetterHandler) worldObj.loadData(EnderLetterHandler.class, ID);

		if (handler == null)
		{
			handler = new EnderLetterHandler();
			worldObj.setData(ID, handler);
		}

		return handler;
	}

	public boolean hasInventoryFor(UUID playerUUID)
	{
		return inventoryMap.containsKey(playerUUID);
	}

	@Override
	public void readFromNBT(NBTTagCompound modNBT)
	{
		NBTTagCompound compound = modNBT.getCompoundTag("enderLetters");

		NBTTagList nbtList = compound.getTagList("entryList", (byte) 10);

		for (int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound entryCompound = nbtList.getCompoundTagAt(i);

			UUID uuid = UUID.fromString(entryCompound.getString("uuid"));

			EnderMailboxInventory inventory = new EnderMailboxInventory(this);
			inventory.readFromNBT(entryCompound.getCompoundTag("inventory"));

			inventoryMap.put(uuid, inventory);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound modNBT)
	{
		NBTTagCompound compound = modNBT.getCompoundTag("enderLetters");

		NBTTagList nbtList = new NBTTagList();

		for (Entry<UUID, EnderMailboxInventory> mapEntry : inventoryMap.entrySet())
		{
			NBTTagCompound entryCompound = new NBTTagCompound();

			entryCompound.setString("uuid", mapEntry.getKey().toString());

			NBTTagCompound inventoryCompound = new NBTTagCompound();

			mapEntry.getValue().writeToNBT(inventoryCompound);

			entryCompound.setTag("inventory", inventoryCompound);

			nbtList.appendTag(entryCompound);
		}

		compound.setTag("entryList", nbtList);

		modNBT.setTag("enderLetters", compound);

		return modNBT;
	}

	public static class EnderMailboxInventory implements IInventory
	{
		NonNullList<ItemStack> enderLetters;
		EnderLetterHandler handler;

		public EnderMailboxInventory(EnderLetterHandler handler)
		{
			enderLetters = NonNullList.withSize(9, ItemStack.EMPTY);
			this.handler = handler;
		}

		public void writeToNBT(NBTTagCompound compound)
		{
			InventoryUtil.writeInventoryToCompound(compound, this);
		}

		public void readFromNBT(NBTTagCompound compound)
		{
			InventoryUtil.readInventoryFromCompound(compound, this);
		}

		@Override
		public String getName()
		{
			return "tile.enderMailbox";
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
			return 9;
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return enderLetters.get(index);
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			if (!this.enderLetters.get(index).isEmpty())
			{
				ItemStack itemstack;

				if (this.enderLetters.get(index).getCount() <= count)
				{
					itemstack = this.enderLetters.get(index);
					this.enderLetters.set(index, ItemStack.EMPTY);
					this.markDirty();
					return itemstack;
				}
				else
				{
					itemstack = this.enderLetters.get(index).splitStack(count);

					if (this.enderLetters.get(index).getCount() == 0)
					{
						this.enderLetters.set(index, ItemStack.EMPTY);
					}

					this.markDirty();
					return itemstack;
				}
			}
			else
			{
				return null;
			}
		}

		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			if (!this.enderLetters.get(index).isEmpty())
			{
				ItemStack itemstack = this.enderLetters.get(index);
				this.enderLetters.set(index, ItemStack.EMPTY);
				return itemstack;
			}
			else
			{
				return null;
			}
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{
			this.enderLetters.set(index, stack);

			if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
			{
				stack.setCount(this.getInventoryStackLimit());
			}

			this.markDirty();
		}

		@Override
		public int getInventoryStackLimit()
		{
			return 64;
		}

		@Override
		public void markDirty()
		{
			if (this.handler != null)
			{
				this.handler.markDirty();
			}
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
			return stack.getItem() == ModItems.enderLetter;
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
			for (int i = 0; i < this.enderLetters.size(); ++i)
			{
				this.enderLetters.set(i, ItemStack.EMPTY);
			}
		}

		@Override
		public boolean isEmpty()
		{
			for (ItemStack itemstack : this.enderLetters)
			{
				if (!itemstack.isEmpty())
				{
					return false;
				}
			}

			return true;
		}
	}
}
