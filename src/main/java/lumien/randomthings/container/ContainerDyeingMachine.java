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

		if (!toDye.isEmpty() && !dye.isEmpty())
		{
			int dyeColor = DyeUtil.getDyeColor(dye);
			ItemStack copy = toDye.copy();

			if (copy.getTagCompound() == null)
			{
				copy.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = copy.getTagCompound();
			copy.setCount(1);
			compound.setInteger("rtDye", dyeColor);
			this.dyeResult.setInventorySlotContents(0, copy);

			// Enchantment Color
			ItemStack enchantmentCopy = toDye.copy();
			enchantmentCopy.setCount(1);
			if (enchantmentCopy.getTagCompound() == null)
			{
				enchantmentCopy.setTagCompound(new NBTTagCompound());
			}

			enchantmentCopy.getTagCompound().setInteger("enchantmentColor", dyeColor);

			enchantmentResult.setInventorySlotContents(0, enchantmentCopy);
		}
		else if (!toDye.isEmpty() && dye.isEmpty())
		{
			this.enchantmentResult.setInventorySlotContents(0, ItemStack.EMPTY);
			ItemStack copy = toDye.copy();
			copy.setCount(1);
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
				this.dyeResult.setInventorySlotContents(0, ItemStack.EMPTY);
			}
		}
		else
		{
			this.dyeResult.setInventorySlotContents(0, ItemStack.EMPTY);
			this.enchantmentResult.setInventorySlotContents(0, ItemStack.EMPTY);
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

				if (!itemstack.isEmpty())
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
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 4)
			{
				if (!this.mergeItemStack(itemstack1, 4, 37, true))
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
}
