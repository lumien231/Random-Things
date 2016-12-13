package lumien.randomthings.container;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.container.slots.SlotDye;
import lumien.randomthings.container.slots.SlotDyeCrafting;
import lumien.randomthings.container.slots.SlotDyeable;
import lumien.randomthings.util.DyeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerDyeingMachine extends Container
{
	InventoryCrafting ingredients = new InventoryCrafting(this, 2, 1);

	IInventory dyeResult = new InventoryCraftResult();
	IInventory enchantmentResult = new InventoryCraftResult();

	private int posX;
	private int posY;
	private int posZ;
	private World worldObj;

	public ContainerDyeingMachine(EntityPlayer player, World world, int x, int y, int z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.worldObj = world;

		this.addSlotToContainer(new SlotDyeable(ingredients, 0, 27, 22));
		this.addSlotToContainer(new SlotDye(ingredients, 1, 76, 22));
		this.addSlotToContainer(new SlotDyeCrafting(player, this, ingredients, dyeResult, 2, 133, 22));
		this.addSlotToContainer(new SlotDyeCrafting(player, this, ingredients, enchantmentResult, 2, 154, 22));
		bindPlayerInventory(player.inventory);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		ItemStack toDye = par1IInventory.getStackInSlot(0);
		ItemStack dye = par1IInventory.getStackInSlot(1);

		if (!toDye.func_190926_b() && !dye.func_190926_b())
		{
			int dyeColor = DyeUtil.getDyeColor(dye);
			ItemStack copy = toDye.copy();

			if (copy.getTagCompound() == null)
			{
				copy.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = copy.getTagCompound();
			copy.func_190920_e(1);
			compound.setInteger("rtDye", dyeColor);
			this.dyeResult.setInventorySlotContents(0, copy);

			// Enchantment Color
			ItemStack enchantmentCopy = toDye.copy();
			enchantmentCopy.func_190920_e(1);
			if (enchantmentCopy.getTagCompound() == null)
			{
				enchantmentCopy.setTagCompound(new NBTTagCompound());
			}

			enchantmentCopy.getTagCompound().setInteger("enchantmentColor", dyeColor);

			enchantmentResult.setInventorySlotContents(0, enchantmentCopy);
		}
		else if ( !toDye.func_190926_b() && dye.func_190926_b())
		{
			this.enchantmentResult.setInventorySlotContents(0, ItemStack.field_190927_a);
			ItemStack copy = toDye.copy();
			copy.func_190920_e(1);
			if (copy.getTagCompound() != null)
			{
				NBTTagCompound compound = copy.getTagCompound();
				if (compound.hasKey("rtDye"))
				{
					compound.removeTag("rtDye");
				}
				
				if (compound.hasKey("enchantmentColor"))
				{
					compound.removeTag("enchantmentColor");
				}

				if (compound.hasNoTags())
				{
					copy.setTagCompound(null);
				}
				this.dyeResult.setInventorySlotContents(0, copy);
			}
			else
			{
				this.dyeResult.setInventorySlotContents(0, ItemStack.field_190927_a);
			}
		}
		else
		{
			this.dyeResult.setInventorySlotContents(0, ItemStack.field_190927_a);
			this.enchantmentResult.setInventorySlotContents(0, ItemStack.field_190927_a);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < 2; ++i)
			{
				ItemStack itemstack = this.ingredients.removeStackFromSlot(i);

				if (!itemstack.func_190926_b())
				{
					par1EntityPlayer.dropItem(itemstack, false);
				}
			}
		}
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 59 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 117));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockState(new BlockPos(posX, posY, posZ)).getBlock() != ModBlocks.dyeingMachine ? false : par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
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

			if (par2 < 4)
			{
				if (!this.mergeItemStack(itemstack1, 4, 37, true))
				{
					return ItemStack.field_190927_a;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 2, false))
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
