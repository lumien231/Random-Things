package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityItemProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerItemProjector extends ContainerTE<TileEntityItemProjector>
{
	IItemHandler itemHandler;

	public ContainerItemProjector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);

		itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new SlotItemHandler(itemHandler, j + i * 3, 62 + j * 18, 17 + i * 18));
			}
		}

		IItemHandler playerItemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new SlotItemHandler(playerItemHandler, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new SlotItemHandler(playerItemHandler, l, 8 + l * 18, 142));
		}
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
				if (!this.mergeItemStack(itemstack1, 9, 45, true))
				{
					return ItemStack.field_190927_a;
				}
			}
			else if (par2 > 8 && !this.mergeItemStack(itemstack1, 0, 9, false))
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

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
			case 0:
				te.rotateSelectionMode();
				break;
		}
	}
}
