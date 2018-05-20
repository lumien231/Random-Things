package lumien.randomthings.handler.compability.jei.imbuing;

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

public class ImbuingRecipeCategory implements IRecipeCategory
{
	private static final int ingredientSlot1 = 0;
	private static final int ingredientSlot2 = 1;
	private static final int ingredientSlot3 = 2;
	private static final int toImbueSlot = 3;
	private static final int outputSlot = 4; // for display only

	private static final int outputSlotX = 92;
	private static final int outputSlotY = 47;

	@Nonnull
	private final IDrawable background;

	@Nonnull
	private final IDrawable slotDrawable;

	@Nonnull
	private final String localizedName;

	public ImbuingRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("randomthings:textures/gui/imbuingStation.png");
		background = guiHelper.createDrawable(location, 32, 6, 112, 112, 0, 0, 0, 0);
		localizedName = Translator.translateToLocal("tile.imbuingStation.name");

		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return RandomThingsPlugin.IMBUE_ID;
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

		itemStacks.init(ingredientSlot1, true, 47, 2);
		itemStacks.init(ingredientSlot2, true, 2, 47);
		itemStacks.init(ingredientSlot3, true, 47, 92);
		itemStacks.init(toImbueSlot, true, 47, 47);
		itemStacks.init(outputSlot, false, outputSlotX, outputSlotY);

		if (recipeWrapper instanceof ImbuingRecipeWrapper)
		{
			List inputs = ingredients.getInputs(ItemStack.class);
			List<ItemStack> inputStacks1 = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(ingredientSlot1));
			List<ItemStack> inputStacks2 = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(ingredientSlot2));
			List<ItemStack> inputStacks3 = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(ingredientSlot3));
			List<ItemStack> toImbueStacks = RandomThingsPlugin.stackHelper.toItemStackList(inputs.get(toImbueSlot));

			itemStacks.set(ingredientSlot1, inputStacks1);
			itemStacks.set(ingredientSlot2, inputStacks2);
			itemStacks.set(ingredientSlot3, inputStacks3);
			itemStacks.set(toImbueSlot, toImbueStacks);
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