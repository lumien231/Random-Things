package lumien.randomthings.handler.compability.jei;

import lumien.randomthings.container.ContainerImbuingStation;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class RandomThingsPlugin implements IModPlugin
{
	private IItemRegistry itemRegistry;
	private IJeiHelpers jeiHelpers;
	
	public static String IMBUE_ID = "Imbuing";

	@Override
	public boolean isModLoaded()
	{
		return true;
	}

	@Override
	public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
	{
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry)
	{
		this.itemRegistry = itemRegistry;
	}

	@Override
	public void register(IModRegistry registry)
	{
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(
				new ImbuingRecipeCategory(guiHelper)
		);

		registry.addRecipeHandlers(
				new ImbuingRecipeHandler()
		);

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(ContainerImbuingStation.class, IMBUE_ID, 0, 4, 5, 36);

		registry.addRecipes(lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler.imbuingRecipes);
	}

	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
	{
		
	}
}
