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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class EnderLetterHandler extends WorldSavedData
{
	public static String ID = "RTEnderLetters";
	
	HashMap<UUID, EnderMailboxInventory> inventoryMap;

	public EnderLetterHandler(String name)
	{
		super(name);
		
		inventoryMap = new HashMap<UUID,EnderMailboxInventory>();
	}

	public EnderLetterHandler()
	{
		super(ID);
		
		inventoryMap = new HashMap<UUID,EnderMailboxInventory>();
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
		EnderLetterHandler handler = (EnderLetterHandler) worldObj.loadItemData(EnderLetterHandler.class, ID);
		
		if (handler==null)
		{
			handler = new EnderLetterHandler();
			worldObj.setItemData(ID, handler);
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
	public void writeToNBT(NBTTagCompound modNBT)
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
	}

	public static class EnderMailboxInventory implements IInventory
	{
		ItemStack[] enderLetters;
		EnderLetterHandler handler;

		public EnderMailboxInventory(EnderLetterHandler handler)
		{
			enderLetters = new ItemStack[9];
			this.handler = handler;
		}

		public void writeToNBT(NBTTagCompound compound)
		{
			InventoryUtil.saveInventoryInCompound(compound, this);
		}

		public void readFromNBT(NBTTagCompound compound)
		{
			InventoryUtil.readInventoryFromCompound(compound, this);
		}

		@Override
		public String getCommandSenderName()
		{
			return "tile.enderMailbox";
		}

		@Override
		public boolean hasCustomName()
		{
			return false;
		}

		@Override
		public IChatComponent getDisplayName()
		{
			return new ChatComponentTranslation(getCommandSenderName());
		}

		@Override
		public int getSizeInventory()
		{
			return 9;
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return enderLetters[index];
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			if (this.enderLetters[index] != null)
			{
				ItemStack itemstack;

				if (this.enderLetters[index].stackSize <= count)
				{
					itemstack = this.enderLetters[index];
					this.enderLetters[index] = null;
					this.markDirty();
					return itemstack;
				}
				else
				{
					itemstack = this.enderLetters[index].splitStack(count);

					if (this.enderLetters[index].stackSize == 0)
					{
						this.enderLetters[index] = null;
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
		public ItemStack getStackInSlotOnClosing(int index)
		{
			if (this.enderLetters[index] != null)
			{
				ItemStack itemstack = this.enderLetters[index];
				this.enderLetters[index] = null;
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
			this.enderLetters[index] = stack;

			if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			{
				stack.stackSize = this.getInventoryStackLimit();
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
			for (int i = 0; i < this.enderLetters.length; ++i)
			{
				this.enderLetters[i] = null;
			}
		}
	}
}
