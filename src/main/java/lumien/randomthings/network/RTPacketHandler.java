package lumien.randomthings.network;

import lumien.randomthings.network.messages.ContainerSignalMessage;
import lumien.randomthings.network.messages.VisualEffectMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.PacketTarget;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * RTPacketHandler
 */
public class RTPacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("randomthings", "main_channel")).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();

	public static void register()
	{
		int disc = 0;

		register(disc++, ContainerSignalMessage.class);
		register(disc++, VisualEffectMessage.class);
	}

	public static void sendToServer(IRTMessage message)
	{
		HANDLER.sendToServer(message);
	}

	public static void sendToAllNear(IRTMessage message, World world, BlockPos pos, float radius)
	{
		PacketTarget target = PacketDistributor.NEAR.with(() -> new TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, world.getDimension().getType()));
		HANDLER.send(target, message);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void register(int id, Class messageClass)
	{
		HANDLER.registerMessage(id, messageClass, (msg, pb) -> msg.write(pb), (pb) -> {
			IRTMessage instance = null;
			try
			{
				instance = (IRTMessage) messageClass.newInstance();
				instance.read(pb);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}

			return instance;
		}, (msg, ctx) -> msg.enqueue(ctx));
	}
}