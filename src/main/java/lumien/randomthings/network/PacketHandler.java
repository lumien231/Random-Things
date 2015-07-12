package lumien.randomthings.network;

import lumien.randomthings.network.messages.MessageAnalogEmitter;
import lumien.randomthings.network.messages.MessageChatDetector;
import lumien.randomthings.network.messages.MessageEnderLetter;
import lumien.randomthings.network.messages.MessageOnlineDetector;

public class PacketHandler
{
	public static final RandomThingsNetworkWrapper INSTANCE = new RandomThingsNetworkWrapper();

	public static void init()
	{
		INSTANCE.registerMessage(MessageOnlineDetector.class);
		INSTANCE.registerMessage(MessageChatDetector.class);
		INSTANCE.registerMessage(MessageAnalogEmitter.class);
		INSTANCE.registerMessage(MessageEnderLetter.class);
	}
}
