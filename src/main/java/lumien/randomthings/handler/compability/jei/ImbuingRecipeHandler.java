package lumien.randomthings.handler.compability.jei;

import javax.annotation.Nonnull;

import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class ImbuingRecipeHandler implements IRecipeHandler<ImbuingRecipe>
{
	@Nonnull
	@Override
	public Class<ImbuingRecipe> getRecipeClass()
	{
		return ImbuingRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RandomThingsPlugin.IMBUE_ID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ImbuingRecipe recipe)
	{
		return new ImbuingRecipeWrapper(recipe.getIngredients(), recipe.toImbue(), recipe.getResult());
	}

	@Override
	public boolean isRecipeValid(@Nonnull ImbuingRecipe recipe)
	{
		return true;
	}
}