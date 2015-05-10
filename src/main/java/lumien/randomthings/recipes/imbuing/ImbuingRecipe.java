package lumien.randomthings.recipes.imbuing;

import java.util.ArrayList;
import java.util.HashMap;

import lumien.randomthings.util.ItemUtil;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ImbuingRecipe
{
	ItemStack toImbue;
	ArrayList<ItemStack> ingredients;
	ItemStack result;

	public ImbuingRecipe(ItemStack toImbue, ItemStack result, ItemStack... ingredients)
	{
		this.toImbue = toImbue;
		this.ingredients = new ArrayList<ItemStack>();
		this.result = result;
		for (ItemStack is : ingredients)
		{
			if (is != null)
				this.ingredients.add(is);
		}
	}

	public boolean matchesInventory(IInventory e)
	{
		HashMap<ItemStack, Boolean> providedIngredients = new HashMap<ItemStack, Boolean>();
		ItemStack i1 = e.getStackInSlot(0);
		ItemStack i2 = e.getStackInSlot(1);
		ItemStack i3 = e.getStackInSlot(2);
		ItemStack center = e.getStackInSlot(3);

		providedIngredients.put(i1, false);
		providedIngredients.put(i2, false);
		providedIngredients.put(i3, false);

		if (!ItemUtil.areItemStackContentEqual(center, toImbue) && !ItemUtil.areOreDictionaried(center, toImbue))
		{
			return false;
		}

		for (ItemStack needed : ingredients)
		{
			if (!containsItemStack(providedIngredients, needed))
			{
				return false;
			}
		}

		for (ItemStack is : providedIngredients.keySet())
		{
			if (!providedIngredients.get(is) && !(is == null))
			{
				return false;
			}
		}

		return true;
	}

	public boolean containsAsIngredient(ItemStack is)
	{
		if (ItemUtil.areItemStackContentEqual(toImbue, is) || ItemUtil.areOreDictionaried(toImbue, is))
		{
			return true;
		}

		for (ItemStack ingredient : ingredients)
		{
			if (ItemUtil.areItemStackContentEqual(ingredient, is) || ItemUtil.areOreDictionaried(ingredient, is))
			{
				return true;
			}
		}

		return false;
	}

	private boolean containsItemStack(HashMap<ItemStack, Boolean> list, ItemStack is)
	{
		for (ItemStack testItemStack : list.keySet())
		{
			if (ItemUtil.areItemStackContentEqual(testItemStack, is) || ItemUtil.areOreDictionaried(testItemStack, is))
			{
				list.put(testItemStack, true);
				return true;
			}
		}
		return false;
	}

	public ArrayList<ItemStack> getIngredients()
	{
		return ingredients;
	}

	public ItemStack toImbue()
	{
		return toImbue;
	}

	public ItemStack getResult()
	{
		return result;
	}
}
