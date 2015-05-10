package lumien.randomthings.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IRTMessage extends IMessage
{
	public void onMessage(MessageContext context);
}
