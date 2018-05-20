package lumien.randomthings.handler.compability.oc;

import java.util.UUID;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageNotification;
import lumien.randomthings.tileentity.TileEntityNotificationInterface;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DriverNotificationInterface extends DriverSidedTileEntity
{

	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side)
	{
		return new Environment((TileEntityNotificationInterface) world.getTileEntity(pos));
	}

	@Override
	public Class<?> getTileEntityClass()
	{
		return TileEntityNotificationInterface.class;
	}

	public static class Environment extends TileEntityEnvironment<TileEntityNotificationInterface>
	{
		public Environment(TileEntityNotificationInterface te)
		{
			super("notification_interface", te);
		}

		@Callback
		public Object[] notify(Context context, Arguments args)
		{
			String title = args.checkString(0);
			String description = args.checkString(1);

			String iconName = args.checkString(2);
			int iconMeta = args.checkInteger(3);

			ItemStack iconStack = new ItemStack(Item.getByNameOrId(iconName), 1, iconMeta);

			UUID owner = this.tileEntity.getOwner();

			if (owner != null)
			{
				EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(owner);

				if (player != null)
				{
					MessageNotification message = new MessageNotification(title, description, iconStack);

					PacketHandler.INSTANCE.sendTo(message, player);
				}
			}

			return new Object[] {};
		}
	}
}
