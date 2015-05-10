package lumien.randomthings.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class WorldUtil
{
	public static void setEntityPosition(Entity e, double posX, double posY, double posZ)
	{
		if (e instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) e;
			player.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
		}
		else
		{
			e.setPositionAndUpdate(posX, posY, posZ);
		}
	}
}
