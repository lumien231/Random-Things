package lumien.randomthings.handler.compability.jei;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.plugins.vanilla.VanillaRecipeWrapper;
import mezz.jei.util.Translator;

public class ImbuingRecipeWrapper extends BlankRecipeWrapper
{
	private final List<ItemStack> ingredients;
	private final ItemStack toImbue;
	private final ItemStack result;
	private final List inputs;
	private final int hashCode;

	@SuppressWarnings("unchecked")
	public ImbuingRecipeWrapper(List<ItemStack> ingredients, ItemStack toImbue, ItemStack result)
	{
		this.ingredients = ingredients;
		this.toImbue = toImbue;
		this.result = result;

		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(toImbue);
		this.inputs.add(ingredients);

		ItemStack firstIngredient = ingredients.get(0);

		this.hashCode = Objects.hashCode(toImbue.getMetadata(), result.getMetadata(), firstIngredient.getItem(), firstIngredient.getMetadata());
	}

	@Override
	public List getInputs()
	{
		return inputs;
	}

	@Override
	public List<ItemStack> getOutputs()
	{
		return Collections.singletonList(result);
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
	{

	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ImbuingRecipeWrapper))
		{
			return false;
		}
		ImbuingRecipeWrapper other = (ImbuingRecipeWrapper) obj;

		if (!ItemStack.areItemStacksEqual(other.result, result))
		{
			return false;
		}

		if (!ItemStack.areItemStacksEqual(other.toImbue, toImbue))
		{
			return false;
		}

		if (ingredients.size() != other.ingredients.size())
		{
			return false;
		}

		for (int i = 0; i < ingredients.size(); i++)
		{
			if (!ItemStack.areItemStacksEqual(ingredients.get(i), other.ingredients.get(i)))
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return ingredients + " + " + toImbue + " = " + result;
	}
}