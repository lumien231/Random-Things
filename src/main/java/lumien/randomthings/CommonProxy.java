package lumien.randomthings;

import lumien.randomthings.entitys.EntitySoul;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username) != null;
	}

	public void scheduleColor(Object o)
	{

	}
}
