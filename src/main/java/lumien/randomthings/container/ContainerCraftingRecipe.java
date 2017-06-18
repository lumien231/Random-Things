package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotDisplay;
import lumien.randomthings.container.slots.SlotGhost;
import lumien.randomthings.item.ItemCraftingRecipe;
import lumien.randomthings.item.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerCraftingRecipe extends Container
{
	ItemStack openedWith;
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	World worldObj;

	public ContainerCraftingRecipe(EntityPlayer player, World world, int x, int y, int z)
	{
		openedWith = player.getHeldItemMainhand();

		if (!openedWith.isEmpty() && openedWith.getItem() == ModItems.craftingRecipe)
		{
			ItemCraftingRecipe.load(openedWith, craftMatrix, craftResult);
		}

		InventoryPlayer playerInventory = player.inventory;
		worldObj = world;

		this.addSlotToContainer(new SlotDisplay(this.craftResult, 0, 124, 35));

		int i, j;

		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new SlotGhost(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
			}
		}

		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.findMatchingRecipe(this.craftMatrix, this.worldObj).getCraftingResult(craftMatrix));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		ItemStack equipped = playerIn.getHeldItemMainhand();
		return !equipped.isEmpty() && equipped.isItemEqual(openedWith);
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		ItemStack toSave = playerIn.getHeldItemMainhand();
		if (!toSave.isEmpty() && toSave.getItem() instanceof ItemCraftingRecipe)
		{
			ItemCraftingRecipe.save(toSave, craftMatrix, craftResult);
		}
	}
}
