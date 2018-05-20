package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotItemHandlerOutputOnly;
import lumien.randomthings.tileentity.TileEntityPotionVaporizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPotionVaporizer extends Container
{
	public int duration;
	public int durationLeft;
	public int color;
	public int potionID;
	public int amplifier;
	public int fuelBurnTime;
	public int fuelBurn;

	int lastDuration;
	int lastDurationLeft;
	int lastColor;
	int lastPotionID;
	int lastAmplifier;
	int lastFuelBurnTime;
	int lastFuelBurn;

	TileEntityPotionVaporizer potionVaporizer;

	public ContainerPotionVaporizer(EntityPlayer player, World world, int x, int y, int z)
	{
		potionVaporizer = (TileEntityPotionVaporizer) world.getTileEntity(new BlockPos(x, y, z));
		IItemHandler itemHandler = potionVaporizer.getItemHandler();

		this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 80, 53));
		this.addSlotToContainer(new SlotItemHandler(itemHandler, 1, 29, 17));
		this.addSlotToContainer(new SlotItemHandlerOutputOnly(itemHandler, 2, 131, 17));

		bindPlayerInventory(player.inventory);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 3)
			{
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 2, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean mergeItemStack(ItemStack par1ItemStack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag1 = false;
		int k = startIndex;

		if (reverseDirection)
		{
			k = endIndex - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (par1ItemStack.isStackable())
		{
			while (par1ItemStack.getCount() > 0 && (!reverseDirection && k < endIndex || reverseDirection && k >= startIndex))
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!itemstack1.isEmpty() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack))
				{
					int l = itemstack1.getCount() + par1ItemStack.getCount();

					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.setCount(0);
						itemstack1.setCount(l);
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.shrink(par1ItemStack.getMaxStackSize() - itemstack1.getCount());
						itemstack1.setCount(par1ItemStack.getMaxStackSize());
						slot.onSlotChanged();
						flag1 = true;
					}
				}

				if (reverseDirection)
				{
					--k;
				}
				else
				{
					++k;
				}
			}
		}

		if (par1ItemStack.getCount() > 0)
		{
			if (reverseDirection)
			{
				k = endIndex - 1;
			}
			else
			{
				k = startIndex;
			}

			while (!reverseDirection && k < endIndex || reverseDirection && k >= startIndex)
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1.isEmpty() && slot.isItemValid(par1ItemStack))
				{
					if (1 < par1ItemStack.getCount())
					{
						ItemStack copy = par1ItemStack.copy();
						copy.setCount(1);
						slot.putStack(copy);

						par1ItemStack.shrink(1);
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(par1ItemStack.copy());
						slot.onSlotChanged();
						par1ItemStack.setCount(0);
						flag1 = true;
						break;
					}
				}

				if (reverseDirection)
				{
					--k;
				}
				else
				{
					++k;
				}
			}
		}
		return flag1;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		switch (id)
		{
		case 0:
			this.durationLeft = data;
			break;
		case 1:
			this.duration = data;
			break;
		case 2:
			this.color = data;
			break;
		case 3:
			this.potionID = data;
			break;
		case 4:
			this.amplifier = data;
			break;
		case 5:
			this.fuelBurnTime = data;
			break;
		case 6:
			this.fuelBurn = data;
			break;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener listener = this.listeners.get(i);

			if (lastDurationLeft != potionVaporizer.getDurationLeft())
				listener.sendWindowProperty(this, 0, this.potionVaporizer.getDurationLeft());

			if (lastDuration != potionVaporizer.getDuration())
				listener.sendWindowProperty(this, 1, this.potionVaporizer.getDuration());

			if (lastColor != potionVaporizer.getColor())
				listener.sendWindowProperty(this, 2, this.potionVaporizer.getColor());

			if (lastPotionID != potionVaporizer.getPotionID())
				listener.sendWindowProperty(this, 3, this.potionVaporizer.getPotionID());

			if (lastAmplifier != potionVaporizer.getAmplifier())
				listener.sendWindowProperty(this, 4, this.potionVaporizer.getAmplifier());

			if (lastFuelBurnTime != potionVaporizer.getFuelBurnTime())
				listener.sendWindowProperty(this, 5, this.potionVaporizer.getFuelBurnTime());

			if (lastFuelBurn != potionVaporizer.getFuelBurn())
				listener.sendWindowProperty(this, 6, this.potionVaporizer.getFuelBurn());
		}

		this.lastDurationLeft = this.potionVaporizer.getDurationLeft();
		this.lastDuration = this.potionVaporizer.getDuration();
		this.lastColor = this.potionVaporizer.getColor();
		this.lastPotionID = this.potionVaporizer.getPotionID();
		this.lastAmplifier = this.potionVaporizer.getAmplifier();
		this.lastFuelBurnTime = this.potionVaporizer.getFuelBurnTime();
		this.lastFuelBurn = this.potionVaporizer.getFuelBurn();
	}
}
