package lumien.randomthings.network;

import java.util.EnumMap;

import io.netty.channel.ChannelFutureListener;
import lumien.randomthings.lib.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

public class RandomThingsNetworkWrapper
{
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private SimpleIndexedCodec packetCodec;

	RTMessageHandler mh;
	int discriminator;

	public RandomThingsNetworkWrapper()
	{
		packetCodec = new SimpleIndexedCodec();
		channels = NetworkRegistry.INSTANCE.newChannel(Reference.MOD_ID.toLowerCase(), packetCodec);
		mh = new RTMessageHandler();

		FMLEmbeddedChannel channel = channels.get(Side.SERVER);
		String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
		addServerHandlerAfter(channel, type, mh, IRTMessage.class);

		channel = channels.get(Side.CLIENT);
		type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
		addClientHandlerAfter(channel, type, mh, IRTMessage.class);

		discriminator = 0;
	}

	/**
	 * Register a message and it's associated handler. The message will have the
	 * supplied discriminator byte. The message handler will be registered on the
	 * supplied side (this is the side where you want the message to be processed
	 * and acted upon).
	 * 
	 * @param messageHandler
	 *            the message handler instance
	 * @param requestMessageType
	 *            the message type
	 * @param discriminator
	 *            a discriminator byte
	 * @param side
	 *            the side for the handler
	 */
	public <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> requestMessageType)
	{
		packetCodec.addDiscriminator(discriminator++, requestMessageType);
	}

	private <REQ extends IMessage, REPLY extends IMessage, NH extends INetHandler> void addServerHandlerAfter(FMLEmbeddedChannel channel, String type, IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestType)
	{
		SimpleChannelHandlerWrapper<REQ, REPLY> handler = getHandlerWrapper(messageHandler, Side.SERVER, requestType);
		channel.pipeline().addAfter(type, messageHandler.getClass().getName(), handler);
	}

	private <REQ extends IMessage, REPLY extends IMessage, NH extends INetHandler> void addClientHandlerAfter(FMLEmbeddedChannel channel, String type, IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestType)
	{
		SimpleChannelHandlerWrapper<REQ, REPLY> handler = getHandlerWrapper(messageHandler, Side.CLIENT, requestType);
		channel.pipeline().addAfter(type, messageHandler.getClass().getName(), handler);
	}

	private <REPLY extends IMessage, REQ extends IMessage> SimpleChannelHandlerWrapper<REQ, REPLY> getHandlerWrapper(IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Side side, Class<REQ> requestType)
	{
		return new SimpleChannelHandlerWrapper<>(messageHandler, side, requestType);
	}

	/**
	 * Construct a minecraft packet from the supplied message. Can be used where
	 * minecraft packets are required, such as
	 * {@link TileEntity#getDescriptionPacket}.
	 * 
	 * @param message
	 *            The message to translate into packet form
	 * @return A minecraft {@link Packet} suitable for use in minecraft APIs
	 */
	public Packet getPacketFrom(IMessage message)
	{
		return channels.get(Side.SERVER).generatePacketFrom(message);
	}

	/**
	 * Send this message to everyone. The {@link IMessageHandler} for this message
	 * type should be on the CLIENT side.
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToAll(IMessage message)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to the specified player. The {@link IMessageHandler} for
	 * this message type should be on the CLIENT side.
	 * 
	 * @param message
	 *            The message to send
	 * @param player
	 *            The player to send it to
	 */
	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to everyone within a certain range of a point. The
	 * {@link IMessageHandler} for this message type should be on the CLIENT side.
	 * 
	 * @param message
	 *            The message to send
	 * @param point
	 *            The {@link TargetPoint} around which to send
	 */
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to everyone within the supplied dimension. The
	 * {@link IMessageHandler} for this message type should be on the CLIENT side.
	 * 
	 * @param message
	 *            The message to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	public void sendToDimension(IMessage message, int dimensionId)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to the server. The {@link IMessageHandler} for this message
	 * type should be on the SERVER side.
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToServer(IMessage message)
	{
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
}
