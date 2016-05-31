package lumien.randomthings.network.messages.magicavoxel;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.handler.magicavoxel.ClientModelLibrary;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageModelData implements IRTMessage
{
	String modelName;
	int dataAmount;
	byte[] data;

	public MessageModelData()
	{
	}

	public MessageModelData(String modelName, int dataAmount, byte[] data)
	{
		this.modelName = modelName;
		this.dataAmount = dataAmount;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.modelName = ByteBufUtils.readUTF8String(buf);
		this.dataAmount = buf.readInt();
		this.data = new byte[dataAmount];
		buf.readBytes(data);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, modelName);
		buf.writeInt(dataAmount);
		buf.writeBytes(data);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ClientModelLibrary.getInstance().addModelData(modelName, data);
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
