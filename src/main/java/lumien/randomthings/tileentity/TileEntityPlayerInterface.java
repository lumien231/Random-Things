package lumien.randomthings.tileentity;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class TileEntityPlayerInterface extends TileEntityBase
{
	UUID playerUUID;

	public TileEntityPlayerInterface()
	{
		playerUUID = null;
	}

	public boolean isCurrentlyConnected()
	{
		return getPlayerInventory() != null;
	}

	protected PlayerInventoryWrapper getPlayerInventory()
	{
		if (this.playerUUID == null)
		{
			return null;
		}
		else
		{
			EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerUUID);
			return player != null ? new PlayerInventoryWrapper((ContainerPlayer) player.inventoryContainer, player.inventory) : null;
		}
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			PlayerInventoryWrapper playerInventory = getPlayerInventory();
			if (playerInventory == null)
			{
				return (T) new EmptyHandler();
			}

			if (facing == EnumFacing.UP)
			{
				return (T) new RangedWrapper(new InvWrapper(playerInventory), 36, 40);
			}
			else if (facing == EnumFacing.DOWN)
			{
				return (T) new RangedWrapper(new InvWrapper(playerInventory), 0, 9);
			}
			else if (facing == EnumFacing.NORTH)
			{
				return (T) new RangedWrapper(new InvWrapper(playerInventory), 40, 41);
			}
			else
			{
				return (T) new RangedWrapper(new InvWrapper(playerInventory), 9, 36);
			}
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return true;
		}

		return super.hasCapability(capability, facing);
	}

	public void setPlayerUUID(UUID uuid)
	{
		this.playerUUID = uuid;
		this.markDirty();
		syncTE();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound nbt, boolean sync)
	{
		if (this.playerUUID != null)
		{
			nbt.setString("player-uuid", this.playerUUID.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound nbt, boolean sync)
	{
		if (nbt.hasKey("player-uuid"))
		{
			this.playerUUID = UUID.fromString(nbt.getString("player-uuid"));
		}
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	private class PlayerInventoryWrapper implements IInventory
	{
		ContainerPlayer container;
		InventoryPlayer inventory;

		public PlayerInventoryWrapper(ContainerPlayer container, InventoryPlayer inventory)
		{
			this.container = container;
			this.inventory = inventory;
		}

		@Override
		public String getName()
		{
			return inventory.getName();
		}

		@Override
		public boolean hasCustomName()
		{
			return inventory.hasCustomName();
		}

		@Override
		public ITextComponent getDisplayName()
		{
			return inventory.getDisplayName();
		}

		@Override
		public int getSizeInventory()
		{
			return inventory.getSizeInventory();
		}

		@Override
		public boolean isEmpty()
		{
			return inventory.isEmpty();
		}

		@Override
		public ItemStack getStackInSlot(int index)
		{
			return inventory.getStackInSlot(index);
		}

		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			return inventory.decrStackSize(index, count);
		}

		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			return inventory.removeStackFromSlot(index);
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{
			inventory.setInventorySlotContents(index, stack);
		}

		@Override
		public int getInventoryStackLimit()
		{
			return inventory.getInventoryStackLimit();
		}

		@Override
		public void markDirty()
		{
			inventory.markDirty();
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player)
		{
			return inventory.isUsableByPlayer(player);
		}

		@Override
		public void openInventory(EntityPlayer player)
		{
			inventory.openInventory(player);
		}

		@Override
		public void closeInventory(EntityPlayer player)
		{
			inventory.closeInventory(player);
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return container.getSlotFromInventory(inventory, index).isItemValid(stack);
		}

		@Override
		public int getField(int id)
		{
			return inventory.getField(id);
		}

		@Override
		public void setField(int id, int value)
		{
			inventory.setField(id, value);
		}

		@Override
		public int getFieldCount()
		{
			return inventory.getFieldCount();
		}

		@Override
		public void clear()
		{
			inventory.clear();
		}

	}
}
