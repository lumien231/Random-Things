package lumien.randomthings.container;

import com.google.common.base.Predicate;

import lumien.randomthings.container.inventories.InventoryItem;
import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotFiltered;
import lumien.randomthings.container.slots.SlotGhost;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ContainerRedstoneRemote extends Container
{
	InventoryItem remoteInventory;
	EnumHand using;
	ItemStack remoteStack;

	public ContainerRedstoneRemote(EntityPlayer player, World world, int x, int y, int z)
	{
		remoteStack = player.getHeldItemMainhand();
		using = EnumHand.MAIN_HAND;
		if (remoteStack.func_190926_b())
		{
			remoteStack = player.getHeldItemOffhand();
			using = EnumHand.OFF_HAND;
		}

		if (!remoteStack.func_190926_b() && remoteStack.getItem() == ModItems.redstoneRemote)
		{
			remoteInventory = new InventoryItem("RedstoneRemote", 18, remoteStack);

			int row = 0;

			for (int col = 0; col < 9; ++col)
			{
				addSlotToContainer(new SlotFiltered(remoteInventory, col + row * 9, 8 + col * 18, 18 + row * 18, new Predicate<ItemStack>()
				{

					@Override
					public boolean apply(ItemStack input)
					{
						return !input.func_190926_b() && input.getItem() == ModItems.positionFilter;
					}
				}));
			}

			row = 1;

			for (int col = 0; col < 9; ++col)
			{
				addSlotToContainer(new SlotGhost(remoteInventory, col + row * 9, 8 + col * 18, 18 + row * 18));
			}
		}
		else
		{
			remoteStack = ItemStack.field_190927_a;
		}

		bindPlayerInventory(player.inventory);
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 68 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			if (inventoryPlayer.getStackInSlot(i) == remoteStack)
			{
				addSlotToContainer(new SlotDisplay(inventoryPlayer, i, 8 + i * 18, 126));
			}
			else
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 126));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return !remoteStack.func_190926_b() && playerIn.getHeldItem(using) == remoteStack;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.field_190927_a;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 9)
			{
				if (!this.mergeItemStack(itemstack1, 18, 54, true))
				{
					return ItemStack.field_190927_a;
				}
			}
			else if (par2 > 17 && !this.mergeItemStack(itemstack1, 0, 9, false))
			{
				return ItemStack.field_190927_a;
			}

			if (itemstack1.func_190916_E() == 0)
			{
				slot.putStack(ItemStack.field_190927_a);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.func_190916_E() == itemstack.func_190916_E())
			{
				return ItemStack.field_190927_a;
			}

			slot.func_190901_a(par1EntityPlayer, itemstack1);
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
			while (par1ItemStack.func_190916_E() > 0 && (!par4 && k < par3 || par4 && k >= par2))
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!itemstack1.func_190926_b() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack))
				{
					int l = itemstack1.func_190916_E() + par1ItemStack.func_190916_E();

					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.func_190920_e(0);
						itemstack1.func_190920_e(l);
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.func_190916_E() < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.func_190918_g(par1ItemStack.getMaxStackSize() - itemstack1.func_190916_E());
						itemstack1.func_190920_e(par1ItemStack.getMaxStackSize());
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

		if (par1ItemStack.func_190916_E() > 0)
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

				if (itemstack1.func_190926_b() && slot.isItemValid(par1ItemStack))
				{
					if (1 < par1ItemStack.func_190916_E())
					{
						ItemStack copy = par1ItemStack.copy();
						copy.func_190920_e(1);
						slot.putStack(copy);

						par1ItemStack.func_190918_g(1);
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(par1ItemStack.copy());
						slot.onSlotChanged();
						par1ItemStack.func_190920_e(0);
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
}
