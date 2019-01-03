package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Features
{
	public static boolean removeAirBubble = false;

	@ConfigOption(category = "VoxelProjector", name = "ClientModelSaving", comment = "Should the client save models received by the server to disk so that they don't have to be requested again later?")
	public static boolean MODEL_CLIENT_SAVING = true;

	@ConfigOption(category = "features", name = "ArtificialEndPortal")
	public static boolean ARTIFICIAL_END_PORTAL = true;

	@ConfigOption(category = "features", name = "EnderAnchorChunkloading", comment = "Should Ender Anchors keep the Chunk they are in loaded")
	public static boolean ENDER_ANCHOR_CHUNKLOADING = true;

	@ConfigOption(category = "visual", name = "FlatRunes", comment = "Replaces the noisy default rune texture with a flat version")
	public static boolean FLAT_RUNES = false;

	@ConfigOption(category = "visual", name = "HideCoordinates", comment = "When set to true the coordinates a position filter / portkey point to won't be displayed in its tooltip.")
	public static boolean HIDE_CORDS = false;
	
	@ConfigOption(category = "features", name = "GoldenEgg", comment = "Should there be an Golden Egg in every Bean Pod?")
	public static boolean GOLDEN_EGG = true;
	
	@ConfigOption(category = "features", name = "MagneticEnchantment", comment = "Whether the magnetic enchantment should be available.")
	public static boolean MAGNETIC_ENCHANTMENT = true;
}
