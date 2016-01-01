package lumien.randomthings.handler.compability.jei;

import java.util.ArrayList;
import java.util.List;

import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;

public class ImbuingRecipeMaker
{

	public static List<ImbuingRecipeWrapper> makeRecipes()
	{
		ArrayList<ImbuingRecipeWrapper> list  = new ArrayList<ImbuingRecipeWrapper>();
		
		for (ImbuingRecipe ir:ImbuingRecipeHandler.imbuingRecipes)
		{
			list.add(new ImbuingRecipeWrapper(ir.getIngredients(), ir.toImbue(), ir.getResult()));
		}
		
		return list;
	}

}
