package lumien.randomthings;

import lumien.randomthings.entitys.EntitySoul;
import net.minecraft.server.MinecraftServer;

public class CommonProxy
{	
	public void registerModels()
	{

	}

	public void renderRedstoneInterfaceStuff(float partialTicks)
	{

	}

	public void registerRenderers()
	{
	}

	public boolean canBeCollidedWith(EntitySoul soul)
	{
		return false;
	}

	public boolean isPlayerOnline(String username)
	{
		return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username) != null;
	}
}
