package lumien.randomthings.handler.compability;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIRandomThingsConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		ImbuingStationRecipeHandler handler = new ImbuingStationRecipeHandler();
		API.registerRecipeHandler(handler);
		API.registerUsageHandler(handler);
	}

	@Override
	public String getName()
	{
		return "Random Things NEI Plugin";
	}

	@Override
	public String getVersion()
	{
		return "1.0";
	}

}
