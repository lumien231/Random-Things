package lumien.randomthings.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RTMessageHandler implements IMessageHandler<IRTMessage, IRTMessage>
{
	@Override
	public IRTMessage onMessage(IRTMessage message, MessageContext ctx)
	{
		if (message.getHandlingSide() == ctx.side)
		{
			message.onMessage(ctx);
		}

		return null;
	}
}
