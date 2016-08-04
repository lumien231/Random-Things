package lumien.randomthings.handler.compability.jei.imbuing;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import mezz.jei.api.recipe.BlankRecipeWrapper;

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
		this.inputs.addAll(ingredients);
		this.inputs.add(toImbue);

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
	public String toString()
	{
		return ingredients + " + " + toImbue + " = " + result;
	}
}