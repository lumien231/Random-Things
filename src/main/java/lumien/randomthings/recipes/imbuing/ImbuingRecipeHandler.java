package lumien.randomthings.recipes.imbuing;

import java.util.ArrayList;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ImbuingRecipeHandler
{
	public static ArrayList<ImbuingRecipe> imbuingRecipes = new ArrayList<ImbuingRecipe>();

	public static ItemStack getRecipeOutput(IInventory imbuingItems)
	{
		for (ImbuingRecipe ir : imbuingRecipes)
		{
			if (ir.matchesInventory(imbuingItems))
			{
				return ir.result;
			}
		}
		return ItemStack.field_190927_a;
	}

	public static void addRecipe(ItemStack ingredient1, ItemStack ingredient2, ItemStack ingredient3, ItemStack toImbue, ItemStack result)
	{
		ImbuingRecipe toAdd = new ImbuingRecipe(toImbue, result, ingredient1, ingredient2, ingredient3);
		imbuingRecipes.add(toAdd);
	}
}
