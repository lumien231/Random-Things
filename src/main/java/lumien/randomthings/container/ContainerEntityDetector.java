package lumien.randomthings.container;

import com.google.common.base.Predicate;

import lumien.randomthings.container.slots.SlotFiltered;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerEntityDetector extends Container
{
	World worldObj;
	BlockPos pos;
	TileEntityEntityDetector entityDetector;

	public ContainerEntityDetector(EntityPlayer player, World world, int x, int y, int z)
	{
		this.worldObj = world;
		this.pos = new BlockPos(x, y, z);
		this.entityDetector = (TileEntityEntityDetector) world.getTileEntity(pos);

		this.addSlotToContainer(new SlotFiltered(entityDetector.getInventory(), 0, 77, 122, new Predicate<ItemStack>()
		{
			@Override
			public boolean apply(ItemStack input)
			{
				return input.getItem() == ModItems.entityFilter;
			}
		}));
		
		bindPlayerInventory(player.inventory);
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 153 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 211));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.worldObj.getTileEntity(this.pos) != entityDetector ? false : playerIn.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
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

			if (par2 < 1)
			{
				if (!this.mergeItemStack(itemstack1, 1, 37, true))
				{
					return ItemStack.field_190927_a;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 1, false))
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
