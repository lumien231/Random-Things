package lumien.randomthings.container.slots;

import lumien.randomthings.container.ContainerDyeingMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class SlotDyeCrafting extends SlotCrafting
{
	IInventory craftMatrix;
	ContainerDyeingMachine containerDyeingMachine;

	public SlotDyeCrafting(EntityPlayer par1EntityPlayer, ContainerDyeingMachine containerDyeingMachine, InventoryCrafting par2iInventory, IInventory par3iInventory, int par4, int par5, int par6)
	{
		super(par1EntityPlayer, par2iInventory, par3iInventory, par4, par5, par6);

		craftMatrix = par2iInventory;
		this.containerDyeingMachine = containerDyeingMachine;
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack)
	{

	}

	@Override
	public ItemStack onTake(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);

			if (!itemstack1.isEmpty())
			{
				this.craftMatrix.decrStackSize(i, 1);
			}
		}

		this.containerDyeingMachine.onCraftMatrixChanged(craftMatrix);

		return par2ItemStack;
	}
}
