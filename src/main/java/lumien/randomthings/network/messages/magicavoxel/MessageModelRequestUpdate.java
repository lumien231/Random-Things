package lumien.randomthings.network.messages.magicavoxel;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.handler.magicavoxel.ClientModelLibrary;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageModelRequestUpdate implements IRTMessage
{
	String modelName;

	int modelSize;
	int paletteSize;
	
	public void setData(String modelName,int modelSize,int paletteSize)
	{
		this.modelName = modelName;
		this.modelSize = modelSize;
		this.paletteSize = paletteSize;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.modelName = ByteBufUtils.readUTF8String(buf);
		this.modelSize = buf.readInt();
		this.paletteSize = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.modelName);
		buf.writeInt(this.modelSize);
		buf.writeInt(this.paletteSize);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ClientModelLibrary.getInstance().updateRequest(modelName, modelSize, paletteSize);
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
