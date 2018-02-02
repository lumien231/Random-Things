package lumien.randomthings.handler.compability.oc;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import lumien.randomthings.tileentity.TileEntityCreativePlayerInterface;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DriverCreativePlayerInterface extends DriverSidedTileEntity
{

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side)
	{
		return new Environment((TileEntityCreativePlayerInterface) world.getTileEntity(pos));
	}

	@Override
	public Class<?> getTileEntityClass()
	{
		return TileEntityCreativePlayerInterface.class;
	}

	public static class Environment extends TileEntityEnvironment<TileEntityCreativePlayerInterface>
	{
		public Environment(TileEntityCreativePlayerInterface te)
		{
			super("creative_player_interface", te);
		}

		@Callback
		public Object[] getPlayerUUID(Context context, Arguments args)
		{
			return new Object[] { tileEntity.getPlayerUUID().toString() };
		}

		@Callback
		public Object[] getPlayerName(Context context, Arguments args)
		{
			if (tileEntity.getPlayerUUID() == null)
			{
				return new Object[] { "NOTCONNECTED" };
			}
			String name = UsernameCache.getLastKnownUsername(tileEntity.getPlayerUUID());
			if (name == null)
			{
				return new Object[] { "NOCACHEDNAME" };
			}
			return new Object[] { name };
		}

		@Callback
		public Object[] setPlayerUUID(Context context, Arguments args)
		{
			tileEntity.setPlayerUUID(UUID.fromString(args.checkString(0)));
			return new Object[] {};
		}

		@Callback
		public Object[] setPlayerName(Context context, Arguments args)
		{
			GameProfile profile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(args.checkString(0));
			if (profile != null)
			{
				tileEntity.setPlayerUUID(profile.getId());
			}
			else
			{
				throw new RuntimeException("That player does not exist");
			}
			return new Object[] {};
		}

		@Callback
		public Object[] isCurrentlyConnected(Context context, Arguments args)
		{
			return new Object[] { tileEntity.isCurrentlyConnected() };
		}
	}
}
