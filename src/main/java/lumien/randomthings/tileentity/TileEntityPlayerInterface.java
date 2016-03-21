package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityPlayerInterface extends TileEntityBase implements ISidedInventory, ITickable
{
	UUID playerUUID;
	EntityPlayerMP playerEntity;

	int[] armorSlots = new int[4];
	int[] hotbarSlots = new int[9];
	int[] mainSlots = new int[27];

	public TileEntityPlayerInterface()
	{
		int i = 0;
		for (int slot = 36; slot < 40; slot++)
		{
			armorSlots[i] = slot;
			i += 1;
		}

		i = 0;
		for (int slot = 9; slot < 36; slot++)
		{
			mainSlots[i] = slot;
			i += 1;
		}

		i = 0;
		for (int slot = 0; slot < 9; slot++)
		{
			hotbarSlots[i] = slot;
			i += 1;
		}

		playerUUID = null;
	}

	public EntityPlayer getPlayer()
	{
		return this.playerEntity;
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			if (this.worldObj.getTotalWorldTime() % 20 == 0)
			{
				if (this.playerEntity == null && playerUUID != null)
				{
					EntityPlayerMP tempPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerUUID);
					if (tempPlayer != null)
					{
						playerEntity = tempPlayer;
						this.worldObj.notifyBlockOfStateChange(this.pos, ModBlocks.playerInterface);
					}
				}
				else
				{
					EntityPlayerMP tempPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerUUID);
					if (tempPlayer != playerEntity)
					{
						this.playerEntity = null;
						this.worldObj.notifyBlockOfStateChange(this.pos, ModBlocks.playerInterface);
					}
				}
			}
		}
	}

	public void setPlayerUUID(UUID uuid)
	{
		this.playerUUID = uuid;
		this.markDirty();
		syncTE();
	}

	private void checkPlayerEntity()
	{
		if (this.playerEntity == null)
		{
			return;
		}
		else if (this.playerEntity.isDead)
		{
			this.playerEntity = null;
		}
	}

	@Override
	public int getSizeInventory()
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return 0;
		}
		return playerEntity.inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return null;
		}
		return playerEntity.inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return null;
		}
		ItemStack newStack = playerEntity.inventory.decrStackSize(i, j);
		playerEntity.inventory.markDirty();
		return newStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int i)
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return null;
		}
		return playerEntity.inventory.removeStackFromSlot(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return;
		}
		playerEntity.inventory.setInventorySlotContents(i, itemstack);
		playerEntity.inventory.markDirty();
	}

	@Override
	public int getInventoryStackLimit()
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return 0;
		}
		return playerEntity.inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) < 64;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		checkPlayerEntity();
		if (this.playerEntity != null)
		{
			this.playerEntity.inventory.openInventory(player);
		}
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		checkPlayerEntity();
		if (this.playerEntity != null && this.playerEntity.inventoryContainer != null)
		{
			this.playerEntity.inventoryContainer.detectAndSendChanges();
			this.playerEntity.sendContainerToPlayer(playerEntity.inventoryContainer);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		checkPlayerEntity();
		if (this.playerEntity != null)
		{
			this.playerEntity.inventory.closeInventory(player);
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		checkPlayerEntity();
		if (this.playerEntity == null || this.playerEntity.inventoryContainer == null || this.playerEntity.inventory == null || this.playerEntity.inventoryContainer.getSlotFromInventory(this.playerEntity.inventory, i) == null)
		{
			return false;
		}
		return this.playerEntity.inventoryContainer.getSlotFromInventory(this.playerEntity.inventory, i).isItemValid(itemstack);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound nbt)
	{
		if (this.playerUUID != null)
		{
			nbt.setString("player-uuid", this.playerUUID.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("player-uuid"))
		{
			this.playerUUID = UUID.fromString(nbt.getString("player-uuid"));
		}
	}

	public boolean hasPlayer()
	{
		return this.playerEntity != null;
	}

	public InventoryPlayer getPlayerInventory()
	{
		return this.playerEntity.inventory;
	}

	public UUID getPlayerUUID()
	{
		return playerUUID;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		checkPlayerEntity();
		if (this.playerEntity == null)
		{
			return new int[] {};
		}
		if (side == EnumFacing.DOWN)
		{
			return hotbarSlots;
		}
		else if (side == EnumFacing.UP)
		{
			return armorSlots;
		}
		else
		{
			return mainSlots;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemstack, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemstack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack itemstack, EnumFacing direction)
	{
		return isItemValidForSlot(index, itemstack);
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
	public String getName()
	{
		return "container.playerinterface";
	}
}
