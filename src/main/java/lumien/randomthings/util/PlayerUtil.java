package lumien.randomthings.util;

import net.minecraft.server.MinecraftServer;

public class PlayerUtil
{
	public static boolean isPlayerOnline(String username)
	{
		return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username)!=null;
	}
}
