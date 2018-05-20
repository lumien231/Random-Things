package lumien.randomthings.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public interface IRTMessage extends IMessage
{
	public void onMessage(MessageContext context);

	public abstract Side getHandlingSide();
}
