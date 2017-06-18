package lumien.randomthings.handler.compability.jei.anvil;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import lumien.randomthings.handler.compability.jei.RandomThingsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AnvilRecipeCategory implements IRecipeCategory
{
	private static final int input1 = 0;
	private static final int input2 = 1;
	private static final int outputSlot = 2; // for display only

	private static final int outputSlotX = 116;
	private static final int outputSlotY = 39;

	@Nonnull
	private final IDrawable background;

	@Nonnull
	private final IDrawable slotDrawable;

	@Nonnull
	private final String localizedName;


	public AnvilRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("randomthings:textures/gui/jeiAnvil.png");
		background = guiHelper.createDrawable(location, 17, 7, 140, 57, 0, 0, 0, 0);
		localizedName = Translator.translateToLocal("tile.anvil.name");
		
		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return RandomThingsPlugin.ANVIL_ID;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft)
	{
		
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper, IIngredients ingredients)
	{
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(input1, true, 9, 39);
		itemStacks.init(input2, true, 58, 39);
		itemStacks.init(outputSlot, false, outputSlotX, outputSlotY);

		if (recipeWrapper instanceof AnvilRecipeWrapper)
		{
			List inputs = ingredients.getInputs(ItemStack.class);
			List<ItemStack> inputStacks1 = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(input1));
			List<ItemStack> inputStacks2 = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(input2));

			itemStacks.set(input1, inputStacks1);
			itemStacks.set(input2, inputStacks2);
			itemStacks.set(outputSlot, ingredients.getOutputs(ItemStack.class).get(0));
		}
	}

	@Override
	public IDrawable getIcon()
	{
		return null;
	}

	@Override
	public List getTooltipStrings(int mouseX, int mouseY)
	{
		return Lists.newArrayList();
	}

	@Override
	public String getModName()
	{
		return "Random Things";
	}
}