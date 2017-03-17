package lumien.randomthings.config;

import lumien.randomthings.lib.ConfigOption;

public class Features
{
	public static boolean removeAirBubble = false;
	
	@ConfigOption(category = "VoxelProjector", name = "ClientModelSaving", comment = "Should the client save models received by the server to disk so that they don't have to be requested again later?")
	public static boolean MODEL_CLIENT_SAVING = true;
	
	@ConfigOption(category = "features",name = "ArtificialEndPortal")
	public static boolean ARTIFICIAL_END_PORTAL = true;
	
	@ConfigOption(category = "features",name = "EnderAnchorChunkloading", comment = "Should Ender Anchors keep the Chunk they are in loaded")
	public static boolean ENDER_ANCHOR_CHUNKLOADING = true;
	
	@ConfigOption(category = "visual", name = "FlatRunes", comment = "Replaces the noisy default rune texture with a flat version")
	public static boolean FLAT_RUNES = false;
}
