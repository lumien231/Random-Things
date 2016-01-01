package lumien.randomthings.handler.compability.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

public class ImbuingRecipeHandler implements IRecipeHandler<ImbuingRecipeWrapper>
{
	@Nonnull
	@Override
	public Class<ImbuingRecipeWrapper> getRecipeClass()
	{
		return ImbuingRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid()
	{
		return "Imbuing";
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ImbuingRecipeWrapper recipe)
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull ImbuingRecipeWrapper recipe)
	{
		return recipe.getInputs().size() == 4 && recipe.getOutputs().size() == 1;
	}
}