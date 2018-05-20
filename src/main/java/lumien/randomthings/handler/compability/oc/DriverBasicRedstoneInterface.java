package lumien.randomthings.handler.compability.oc;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DriverBasicRedstoneInterface extends DriverSidedTileEntity
{

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side)
	{
		return new Environment((TileEntityBasicRedstoneInterface) world.getTileEntity(pos));
	}

	@Override
	public Class<?> getTileEntityClass()
	{
		return TileEntityBasicRedstoneInterface.class;
	}

	public static class Environment extends TileEntityEnvironment<TileEntityBasicRedstoneInterface>
	{
		public Environment(TileEntityBasicRedstoneInterface te)
		{
			super("basic_redstone_interface", te);
		}

		@Callback
		public Object[] setTarget(Context context, Arguments args)
		{
			tileEntity.setTarget(new BlockPos(args.checkInteger(0), args.checkInteger(1), args.checkInteger(2)));
			return new Object[] {};
		}

		@Callback
		public Object[] getTarget(Context context, Arguments args)
		{
			BlockPos target = tileEntity.getTarget();
			if (target == null)
			{
				return new Object[] {};
			}
			else
			{
				return new Object[] { target.getX(), target.getY(), target.getZ() };
			}
		}
	}
}
