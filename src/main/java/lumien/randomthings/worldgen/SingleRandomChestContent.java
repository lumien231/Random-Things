package lumien.randomthings.worldgen;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

public class SingleRandomChestContent extends WeightedRandomChestContent
{
	public SingleRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn)
	{
		super(stack, minimumChance, maximumChance, itemWeightIn);
	}

	@Override
	protected ItemStack[] generateChestContent(Random random, IInventory newInventory)
	{
		for (int slot = 0; slot < newInventory.getSizeInventory(); slot++)
		{
			ItemStack is = newInventory.getStackInSlot(slot);
			if (is != null)
			{
				if (ItemStack.areItemsEqual(is, this.theItemId))
				{
					return new ItemStack[] {};
				}
			}
		}

		return net.minecraftforge.common.ChestGenHooks.generateStacks(random, theItemId, minStackSize, maxStackSize);
	}
}
