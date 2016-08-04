package lumien.randomthings.network.messages.magicavoxel;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.handler.magicavoxel.ServerModelLibrary;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageModelRequest implements IRTMessage
{
	String modelName;

	public MessageModelRequest()
	{
	}
	
	public MessageModelRequest(String modelName)
	{
		this.modelName = modelName;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.modelName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, modelName);
	}

	@Override
	public void onMessage(final MessageContext context)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		server.addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ServerModelLibrary.getInstance().requestModel(context.getServerHandler(),modelName);
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
