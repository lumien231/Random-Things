package lumien.randomthings.tileentity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityCreativePlayerInterface extends TileEntityPlayerInterface implements SimpleComponent
{
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName()
	{
		return "playerInterface";
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getPlayerUUID(Context context, Arguments args)
	{
		return new Object[] { this.playerUUID.toString() };
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getPlayerName(Context context, Arguments args)
	{
		if (this.playerUUID == null)
		{
			return new Object[] { "NOTCONNECTED" };
		}
		String name = UsernameCache.getLastKnownUsername(this.playerUUID);
		if (name == null)
		{
			return new Object[] { "NOCACHEDNAME" };
		}
		return new Object[] { name };
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setPlayerUUID(Context context, Arguments args)
	{
		this.setPlayerUUID(UUID.fromString(args.checkString(0)));
		return new Object[] {};
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setPlayerName(Context context, Arguments args)
	{
		GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args.checkString(0));
		if (profile != null)
		{
			this.setPlayerUUID(profile.getId());
		}
		else
		{
			throw new RuntimeException("That player does not exist");
		}
		return new Object[] {};
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] isCurrentlyConnected(Context context, Arguments args)
	{
		return new Object[] { this.playerEntity != null };
	}
}
