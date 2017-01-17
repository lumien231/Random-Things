package lumien.randomthings.container;

import java.util.List;

import com.google.common.base.Predicate;

import lumien.randomthings.container.slots.SlotFiltered;
import lumien.randomthings.container.slots.SlotOutputOnly;
import lumien.randomthings.tileentity.TileEntityPotionVaporizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		IInventory inventory = potionVaporizer.getInventory();

		this.addSlotToContainer(new SlotFurnaceFuel(inventory, 0, 80, 53));
		this.addSlotToContainer(new SlotFiltered(inventory, 1, 29, 17, new Predicate<ItemStack>()
		{
			@Override
			public boolean apply(ItemStack input)
			{
				if (input.getItem() != Items.POTIONITEM)
				{
					return false;
				}
				List<PotionEffect> effects = PotionUtils.getEffectsFromStack(input);
				if (effects==null || effects.size()==0)
				{
					return false;
				}
				
				return !effects.get(0).getPotion().isInstant();
				
			}
		}));

		this.addSlotToContainer(new SlotOutputOnly(inventory, 2, 131, 17));

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
	public boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
	{
		boolean flag1 = false;
		int k = par2;

		if (par4)
		{
			k = par3 - 1;
		}

		Slot slot;
		ItemStack itemstack1;

		if (par1ItemStack.isStackable())
		{
			while (par1ItemStack.getCount() > 0 && (!par4 && k < par3 || par4 && k >= par2))
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

				if (par4)
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
			if (par4)
			{
				k = par3 - 1;
			}
			else
			{
				k = par2;
			}

			while (!par4 && k < par3 || par4 && k >= par2)
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

				if (par4)
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
				listener.sendProgressBarUpdate(this, 0, this.potionVaporizer.getDurationLeft());

			if (lastDuration != potionVaporizer.getDuration())
				listener.sendProgressBarUpdate(this, 1, this.potionVaporizer.getDuration());

			if (lastColor != potionVaporizer.getColor())
				listener.sendProgressBarUpdate(this, 2, this.potionVaporizer.getColor());

			if (lastPotionID != potionVaporizer.getPotionID())
				listener.sendProgressBarUpdate(this, 3, this.potionVaporizer.getPotionID());

			if (lastAmplifier != potionVaporizer.getAmplifier())
				listener.sendProgressBarUpdate(this, 4, this.potionVaporizer.getAmplifier());

			if (lastFuelBurnTime != potionVaporizer.getFuelBurnTime())
				listener.sendProgressBarUpdate(this, 5, this.potionVaporizer.getFuelBurnTime());

			if (lastFuelBurn != potionVaporizer.getFuelBurn())
				listener.sendProgressBarUpdate(this, 6, this.potionVaporizer.getFuelBurn());
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
