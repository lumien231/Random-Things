package lumien.randomthings.handler.compability.oc;

import java.util.Arrays;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import lumien.randomthings.tileentity.TileEntityOnlineDetector;
import lumien.randomthings.util.PlayerUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DriverOnlineDetector extends DriverSidedTileEntity
{

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side)
	{
		return new Environment((TileEntityOnlineDetector) world.getTileEntity(pos));
	}

	@Override
	public Class<?> getTileEntityClass()
	{
		return TileEntityOnlineDetector.class;
	}

	public static class Environment extends TileEntityEnvironment<TileEntityOnlineDetector>
	{
		public Environment(TileEntityOnlineDetector te)
		{
			super("online_detector", te);
		}

		@Callback
		public Object[] isPlayerOnline(Context context, Arguments args)
		{
			return new Object[] { PlayerUtil.isPlayerOnline(args.checkString(0)) };
		}

		@Callback
		public Object[] getPlayerList(Context context, Arguments args)
		{
			return new Object[] { Arrays.asList(FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames()) };
		}
	}
}
