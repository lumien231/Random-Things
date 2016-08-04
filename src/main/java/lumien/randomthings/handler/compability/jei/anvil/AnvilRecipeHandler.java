package lumien.randomthings.handler.compability.jei.anvil;

import javax.annotation.Nonnull;

import lumien.randomthings.handler.compability.jei.RandomThingsPlugin;
import lumien.randomthings.recipes.anvil.AnvilRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class AnvilRecipeHandler implements IRecipeHandler<AnvilRecipe>
{
	@Nonnull
	@Override
	public Class<AnvilRecipe> getRecipeClass()
	{
		return AnvilRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return RandomThingsPlugin.ANVIL_ID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull AnvilRecipe recipe)
	{
		return new AnvilRecipeWrapper(recipe.getFirst(),recipe.getSecond(), recipe.getOutput());
	}

	@Override
	public boolean isRecipeValid(@Nonnull AnvilRecipe recipe)
	{
		return true;
	}
}