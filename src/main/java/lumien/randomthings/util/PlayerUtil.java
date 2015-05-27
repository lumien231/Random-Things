package lumien.randomthings.util;

import lumien.randomthings.RandomThings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerUtil
{
	public static boolean isPlayerOnline(String username)
	{
		return RandomThings.proxy.isPlayerOnline(username);
	}
	
	public static void teleportPlayerToDimension(EntityPlayerMP player,int dimension)
	{
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) player, dimension);
        
        player.removeExperienceLevel(0);
        player.setPlayerHealthUpdated();
	}
}
