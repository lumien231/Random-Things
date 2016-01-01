package lumien.randomthings.handler.compability.jei;

import javax.annotation.Nonnull;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.util.StackUtil;
import mezz.jei.util.Translator;

public class ImbuingRecipeCategory implements IRecipeCategory
{

	private static final int ingredientSlot1 = 0;
	private static final int ingredientSlot2 = 1;
	private static final int ingredientSlot3 = 2;
	private static final int toImbueSlot = 3;
	private static final int outputSlot = 4; // for display only

	private static final int outputSlotX = 125;
	private static final int outputSlotY = 54;

	@Nonnull
	private final IDrawable background;

	@Nonnull
	private final IDrawable slotDrawable;

	@Nonnull
	private final String localizedName;

	@Nonnull
	private final IDrawableAnimated arrow;

	@Nonnull
	private final IDrawableAnimated bubbles;


	public ImbuingRecipeCategory(IGuiHelper guiHelper)
	{
		ResourceLocation location = new ResourceLocation("randomthings:textures/gui/imbuingStation.png");
		background = guiHelper.createDrawable(location, 55, 15, 64, 56, 0, 0, 0, 40);
		localizedName = Translator.translateToLocal("tile.imbuingStation");

		IDrawableStatic imbueArrow = guiHelper.createDrawable(location, 176, 0, 9, 28);
		arrow = guiHelper.createAnimatedDrawable(imbueArrow, 400, IDrawableAnimated.StartDirection.LEFT, false);

		IDrawableStatic brewBubblesDrawable = guiHelper.createDrawable(location, 185, 0, 12, 29);
		bubbles = guiHelper.createAnimatedDrawable(brewBubblesDrawable, 20, IDrawableAnimated.StartDirection.BOTTOM, false);

		slotDrawable = guiHelper.getSlotDrawable();
	}

	@Nonnull
	@Override
	public String getUid()
	{
		return "Imbuing";
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
		slotDrawable.draw(minecraft, outputSlotX, outputSlotY);
	}

	@Override
	public void drawAnimations(Minecraft minecraft)
	{
		bubbles.draw(minecraft, 10, 0);
		arrow.draw(minecraft, 189, 13);
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
	{
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(ingredientSlot1, true, 80, 9);
		itemStacks.init(ingredientSlot2, true, 35, 54);
		itemStacks.init(ingredientSlot3, true, 80, 99);
		itemStacks.init(toImbueSlot, true, 80, 54);
		itemStacks.init(outputSlot, false, outputSlotX, outputSlotY);

		if (recipeWrapper instanceof ImbuingRecipeWrapper)
		{
			List inputs = recipeWrapper.getInputs();
			List<ItemStack> inputStacks1 = StackUtil.toItemStackList(inputs.get(ingredientSlot1));
			List<ItemStack> inputStacks2 = StackUtil.toItemStackList(inputs.get(ingredientSlot2));
			List<ItemStack> inputStacks3 = StackUtil.toItemStackList(inputs.get(ingredientSlot3));
			List<ItemStack> toImbueStacks = StackUtil.toItemStackList(inputs.get(toImbueSlot));

			itemStacks.setFromRecipe(ingredientSlot1, inputStacks1);
			itemStacks.setFromRecipe(ingredientSlot2, inputStacks2);
			itemStacks.setFromRecipe(ingredientSlot3, inputStacks3);
			itemStacks.setFromRecipe(toImbueSlot, toImbueStacks);
			itemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
		}
	}
}