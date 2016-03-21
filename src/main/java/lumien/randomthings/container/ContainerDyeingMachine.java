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

		if (toDye != null && dye != null)
		{
			int dyeColor = DyeUtil.getDyeColor(dye);
			ItemStack copy = toDye.copy();

			if (copy.getTagCompound() == null)
			{
				copy.setTagCompound(new NBTTagCompound());
			}

			NBTTagCompound compound = copy.getTagCompound();
			copy.stackSize = 1;
			compound.setInteger("rtDye", dyeColor);
			this.dyeResult.setInventorySlotContents(0, copy);

			// Enchantment Color
			ItemStack enchantmentCopy = toDye.copy();
			enchantmentCopy.stackSize = 1;
			if (enchantmentCopy.getTagCompound() == null)
			{
				enchantmentCopy.setTagCompound(new NBTTagCompound());
			}

			enchantmentCopy.getTagCompound().setInteger("enchantmentColor", dyeColor);

			enchantmentResult.setInventorySlotContents(0, enchantmentCopy);
		}
		else if (toDye != null && dye == null)
		{
			this.enchantmentResult.setInventorySlotContents(0, null);
			ItemStack copy = toDye.copy();
			copy.stackSize = 1;
			if (copy.getTagCompound() != null)
			{
				NBTTagCompound compound = copy.getTagCompound();
				if (compound.hasKey("rtDye"))
				{
					compound.removeTag("rtDye");
				}

				if (compound.hasNoTags())
				{
					copy.setTagCompound(null);
				}
				this.dyeResult.setInventorySlotContents(0, copy);
			}
			else
			{
				this.dyeResult.setInventorySlotContents(0, null);
			}
		}
		else
		{
			this.dyeResult.setInventorySlotContents(0, null);
			this.enchantmentResult.setInventorySlotContents(0, null);
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

				if (itemstack != null)
				{
					par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
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
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < 4)
			{
				if (!this.mergeItemStack(itemstack1, 4, 37, true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 2, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
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
			while (par1ItemStack.stackSize > 0 && (!par4 && k < par3 || par4 && k >= par2))
			{
				slot = this.inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (itemstack1 != null && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, itemstack1) && slot.isItemValid(par1ItemStack))
				{
					int l = itemstack1.stackSize + par1ItemStack.stackSize;

					if (l <= par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize = 0;
						itemstack1.stackSize = l;
						slot.onSlotChanged();
						flag1 = true;
					}
					else if (itemstack1.stackSize < par1ItemStack.getMaxStackSize())
					{
						par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = par1ItemStack.getMaxStackSize();
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

		if (par1ItemStack.stackSize > 0)
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

				if (itemstack1 == null && slot.isItemValid(par1ItemStack))
				{
					if (1 < par1ItemStack.stackSize)
					{
						ItemStack copy = par1ItemStack.copy();
						copy.stackSize = 1;
						slot.putStack(copy);

						par1ItemStack.stackSize -= 1;
						flag1 = true;
						break;
					}
					else
					{
						slot.putStack(par1ItemStack.copy());
						slot.onSlotChanged();
						par1ItemStack.stackSize = 0;
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
