package lumien.randomthings.client.screen;

import lumien.randomthings.container.ModContainerTypes;
import net.minecraft.client.gui.ScreenManager;

/**
 * ModScreens
 */
public class ModScreens
{

	public static void register()
	{
		ScreenManager.registerFactory(ModContainerTypes.ADVANCED_REDSTONE_TORCH, AdvancedRedstoneTorchScreen::new);
	}
}