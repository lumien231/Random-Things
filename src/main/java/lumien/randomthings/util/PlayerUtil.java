package lumien.randomthings.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lumien.randomthings.RandomThings;
import lumien.randomthings.asm.MCPNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerUtil
{
	static Method capturePosition;

	static
	{
		try
		{
			capturePosition = NetHandlerPlayServer.class.getDeclaredMethod(MCPNames.method("func_184342_d"));
			capturePosition.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void capturePosition(NetHandlerPlayServer connection)
	{
		try
		{
			capturePosition.invoke(connection);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isPlayerOnline(String username)
	{
		return RandomThings.proxy.isPlayerOnline(username);
	}

	public static void teleportPlayerToDimension(EntityPlayerMP player, int dimension)
	{
		boolean comingFromEnd = player.dimension == 1;

		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().transferPlayerToDimension(player, dimension, new SimpleTeleporter(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension)));

		// If a player is teleported from the end certain logic elements are ignored in
		// transferPlayerToDimension
		if (comingFromEnd)
		{
			double d0 = MathHelper.clamp((int) player.posX, -29999872, 29999872);
			double d1 = MathHelper.clamp((int) player.posZ, -29999872, 29999872);

			if (player.isEntityAlive())
			{
				player.setLocationAndAngles(d0, player.posY, d1, player.rotationYaw, player.rotationPitch);
				player.world.spawnEntity(player);
				player.world.updateEntityWithOptionalForce(player, false);
			}
		}

		player.addExperienceLevel(0);
		player.setPlayerHealthUpdated();
	}

	public static boolean removeExperiencerFromPlayer(EntityPlayer player, int amount)
	{
		int currentExperience = player.experienceTotal;

		if (amount > currentExperience)
		{
			return false;
		}
		else
		{
			player.experienceTotal -= amount;
			while (amount > 0)
			{
				int amountInBar = (int) (Math.floor(player.experience * player.xpBarCap()));

				if (amountInBar >= amount)
				{
					player.experience -= 1F / player.xpBarCap() * amount;
					amount = 0;
				}
				else
				{
					amount -= amountInBar;
					player.experienceLevel -= 1;
					player.experience = 1;
				}
			}

			return true;
		}
	}
}
