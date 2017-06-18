package lumien.randomthings.handler.compability.jei.anvil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AnvilRecipeWrapper extends BlankRecipeWrapper
{
	private final Pair<ItemStack,ItemStack> input;
	private final ItemStack result;
	private final List inputs;

	@SuppressWarnings("unchecked")
	public AnvilRecipeWrapper(ItemStack input1,ItemStack input2, ItemStack result)
	{
		this.result = result;

		this.input = Pair.of(input1, input2);
		
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(input.getLeft());
		this.inputs.add(input.getRight());
	}
	
	public Pair<ItemStack,ItemStack> getInputPair()
	{
		return input;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, Collections.singletonList(result));
	}
}