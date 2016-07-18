package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerTE;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageContainerSignal implements IRTMessage
{
	int signal;
	
	public MessageContainerSignal()
	{
		
	}
	
	public MessageContainerSignal(int signal)
	{
		this.signal = signal;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.signal = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(signal);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		EntityPlayerMP player = context.getServerHandler().playerEntity;
		
		if (player.openContainer instanceof ContainerTE)
		{
			((ContainerTE)player.openContainer).signal(signal);
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
