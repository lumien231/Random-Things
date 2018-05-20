package lumien.randomthings.recipes.anvil;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class AnvilRecipeHandler
{
	static List<AnvilRecipe> anvilRecipes = new ArrayList<>();

	public static List<AnvilRecipe> getAllRecipes()
	{
		return anvilRecipes;
	}

	public static void addAnvilRecipe(ItemStack input1, ItemStack input2, ItemStack output, int cost)
	{
		anvilRecipes.add(new AnvilRecipe(input1, input2, output, cost));
	}

	public static AnvilRecipe getRecipe(ItemStack input1, ItemStack input2)
	{
		for (AnvilRecipe recipe : anvilRecipes)
		{
			if (areStackPairsEqual(input1, input2, recipe.getFirst(), recipe.getSecond()))
			{
				return recipe;
			}
		}

		return null;
	}

	private static boolean areStackPairsEqual(ItemStack o1, ItemStack o2, ItemStack o3, ItemStack o4)
	{
		return ((ItemStack.areItemsEqual(o1, o3) && ItemStack.areItemsEqual(o2, o4)) || (ItemStack.areItemsEqual(o1, o4)) && (ItemStack.areItemsEqual(o2, o3)));
	}
}
