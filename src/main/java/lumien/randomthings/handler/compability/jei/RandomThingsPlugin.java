package lumien.randomthings.handler.compability.jei;

import lumien.randomthings.client.gui.GuiImbuingStation;
import lumien.randomthings.container.ContainerImbuingStation;
import lumien.randomthings.handler.compability.jei.anvil.AnvilRecipeCategory;
import lumien.randomthings.handler.compability.jei.anvil.AnvilRecipeHandler;
import lumien.randomthings.handler.compability.jei.imbuing.ImbuingRecipeCategory;
import lumien.randomthings.handler.compability.jei.imbuing.ImbuingRecipeHandler;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.inventory.ContainerRepair;

@JEIPlugin
public class RandomThingsPlugin implements IModPlugin
{
	private IItemRegistry itemRegistry;
	private IJeiHelpers jeiHelpers;
	public static IStackHelper stackHelper;

	public static String IMBUE_ID = "Imbuing";
	public static String ANVIL_ID = "AnvilCraftingRT";

	@Override
	public void register(IModRegistry registry)
	{
		this.itemRegistry = registry.getItemRegistry();
		this.jeiHelpers = registry.getJeiHelpers();
		RandomThingsPlugin.stackHelper = jeiHelpers.getStackHelper();
		
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(new ImbuingRecipeCategory(guiHelper), new AnvilRecipeCategory(guiHelper));

		registry.addRecipeHandlers(new ImbuingRecipeHandler(), new AnvilRecipeHandler());

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

		recipeTransferRegistry.addRecipeTransferHandler(ContainerImbuingStation.class, IMBUE_ID, 0, 4, 5, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerRepair.class, ANVIL_ID, 0, 2, 3, 36);

		registry.addRecipes(lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler.imbuingRecipes);
		registry.addRecipes(lumien.randomthings.recipes.anvil.AnvilRecipeHandler.getAllRecipes());

		registry.addRecipeClickArea(GuiImbuingStation.class, 99, 54, 22, 16, IMBUE_ID);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		
	}
}
