package lumien.randomthings.handler.compability.jei.anvil;

import com.google.common.base.Objects;
import com.mojang.realmsclient.util.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import mezz.jei.api.recipe.BlankRecipeWrapper;

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
		this.inputs.add(input.first());
		this.inputs.add(input.second());
	}
	
	public Pair<ItemStack,ItemStack> getInputPair()
	{
		return input;
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
}