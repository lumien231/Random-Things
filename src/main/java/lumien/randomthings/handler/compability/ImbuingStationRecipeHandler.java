package lumien.randomthings.handler.compability;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lumien.randomthings.client.gui.GuiImbuingStation;
import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class ImbuingStationRecipeHandler extends TemplateRecipeHandler
{
	static Gui gui = new Gui();

	public class CachedImbuingRecipe extends CachedRecipe
	{
		ArrayList<PositionedStack> ingredients;
		PositionedStack toImbue;
		PositionedStack result;

		public CachedImbuingRecipe(ArrayList<ItemStack> ingredients, ItemStack toImbue, ItemStack result)
		{
			this.ingredients = new ArrayList<PositionedStack>();
			this.toImbue = new PositionedStack(toImbue, 48 + 176 / 2 - 129 / 2, 48);
			this.result = new PositionedStack(result, 93 + 176 / 2 - 129 / 2, 48);

			for (int i = 0; i < ingredients.size(); i++)
			{
				int x = 0, y = 0;
				switch (i)
				{
					case 0:
						x = 48 + 176 / 2 - 129 / 2;
						y = 3;
						break;
					case 1:
						x = 3 + 176 / 2 - 129 / 2;
						y = 48;
						break;
					case 2:
						x = 48 + 176 / 2 - 129 / 2;
						y = 93;
						break;
				}
				this.ingredients.add(new PositionedStack(ingredients.get(i), x, y));

			}
		}

		@Override
		public PositionedStack getIngredient()
		{
			return toImbue;
		}

		@Override
		public PositionedStack getResult()
		{
			return result;
		}

		@Override
		public List<PositionedStack> getOtherStacks()
		{
			return ingredients;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (!(obj instanceof CachedImbuingRecipe))
				return false;
			CachedImbuingRecipe recipe2 = (CachedImbuingRecipe) obj;

			return result.item.isItemEqual(recipe2.result.item);
		}
	}

	public static final HashSet<CachedImbuingRecipe> aimbuings = new HashSet<CachedImbuingRecipe>();

	@Override
	public int recipiesPerPage()
	{
		return 1;
	}

	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		for (ImbuingRecipe recipe : ImbuingRecipeHandler.imbuingRecipes)
		{
			if (recipe.getResult().isItemEqual(result))
			{
				arecipes.add(new CachedImbuingRecipe(recipe.getIngredients(), recipe.toImbue(), recipe.getResult()));
			}
		}
	}

	@Override
	public void drawExtras(int recipe)
	{
		drawProgressBar(82 - 32 + 176 / 2 - 129 / 2, 28 - 6, 176, 0, 12, 26, 12, 1);
		drawProgressBar(54 - 32 + 176 / 2 - 129 / 2, 56 - 6, 189, 0, 26, 12, 12, 0);
		drawProgressBar(82 - 32 + 176 / 2 - 129 / 2, 71 - 6, 176, 24, 12, 24, 12, 3);

		drawProgressBar(99 - 32 + 176 / 2 - 129 / 2, 54 - 6, 189, 13, 24, 16, 200, 0);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if (outputId.equals("imbuing") && getClass() == ImbuingStationRecipeHandler.class)
		{
			for (ImbuingRecipe recipe : ImbuingRecipeHandler.imbuingRecipes)
			{
				arecipes.add(new CachedImbuingRecipe(recipe.getIngredients(), recipe.toImbue(), recipe.getResult()));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		for (ImbuingRecipe recipe : ImbuingRecipeHandler.imbuingRecipes)
		{
			if (recipe.containsAsIngredient(ingredient))
			{
				arecipes.add(new CachedImbuingRecipe(recipe.getIngredients(), recipe.toImbue(), recipe.getResult()));
			}
		}
	}

	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(99 - 6, 54 - 6 - 3, 22, 16), "imbuing"));
	}

	@Override
	public String getRecipeName()
	{
		return "Imbuing Station";
	}

	@Override
	public void drawBackground(int recipe)
	{
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture((new ResourceLocation(getGuiTexture())));
		gui.drawTexturedModalRect(176 / 2 - 129 / 2, 0, 32, 6, 129, 114);
	}

	@Override
	public String getOverlayIdentifier()
	{
		return "imbuing";
	}

	@Override
	public String getGuiTexture()
	{
		return "randomthings:textures/gui/imbuingStation.png";
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiImbuingStation.class;
	}

}
