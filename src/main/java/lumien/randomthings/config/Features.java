package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Features
{
	public static boolean removeAirBubble = false;
	
	@ConfigOption(category = "VoxelProjector", name = "ClientModelSaving", comment = "Should the client save models received by the server to disk so that they don't have to be requested again later?")
	public static boolean MODEL_CLIENT_SAVING = true;
}
