package lumien.randomthings.network;

import lumien.randomthings.network.messages.MessageChatDetector;
import lumien.randomthings.network.messages.MessageOnlineDetector;

public class PacketHandler
{
	public static final RandomThingsNetworkWrapper INSTANCE = new RandomThingsNetworkWrapper();

	public static void init()
	{
		INSTANCE.registerMessage(MessageOnlineDetector.class);
		INSTANCE.registerMessage(MessageChatDetector.class);
	}
}
