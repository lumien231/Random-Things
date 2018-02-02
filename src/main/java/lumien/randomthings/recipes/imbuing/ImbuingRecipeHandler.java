package lumien.randomthings.recipes.imbuing;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ImbuingRecipeHandler
{
	public static ArrayList<ImbuingRecipe> imbuingRecipes = new ArrayList<>();

	public static ItemStack getRecipeOutput(IItemHandler iItemHandler)
	{
		for (ImbuingRecipe ir : imbuingRecipes)
		{
			if (ir.matchesItemHandler(iItemHandler))
			{
				return ir.result;
			}
		}
		return ItemStack.EMPTY;
	}

	public static void addRecipe(ItemStack ingredient1, ItemStack ingredient2, ItemStack ingredient3, ItemStack toImbue, ItemStack result)
	{
		ImbuingRecipe toAdd = new ImbuingRecipe(toImbue, result, ingredient1, ingredient2, ingredient3);
		imbuingRecipes.add(toAdd);
	}
}
