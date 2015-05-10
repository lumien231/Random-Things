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
import net.minecraft.inventory.SlotCrafting;
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
		openedWith = player.getCurrentEquippedItem();

		if (openedWith != null && openedWith.getItem() == ModItems.craftingRecipe)
		{
			ItemCraftingRecipe.load(openedWith,craftMatrix,craftResult);
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

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return null;
	}

	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		ItemStack equipped = playerIn.getCurrentEquippedItem();
		return equipped != null && equipped.isItemEqual(openedWith);
	}

	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		ItemStack toSave = playerIn.getCurrentEquippedItem();
		if (toSave != null && toSave.getItem() instanceof ItemCraftingRecipe)
		{
			ItemCraftingRecipe.save(toSave, craftMatrix, craftResult);
		}
	}
}
