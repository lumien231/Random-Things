package lumien.randomthings.network;

import lumien.randomthings.network.messages.MessageAdvancedItemCollector;
import lumien.randomthings.network.messages.MessageAnalogEmitter;
import lumien.randomthings.network.messages.MessageChatDetector;
import lumien.randomthings.network.messages.MessageEnderLetter;
import lumien.randomthings.network.messages.MessageEntityDetector;
import lumien.randomthings.network.messages.MessageItemFilter;
import lumien.randomthings.network.messages.MessageLightRedirector;
import lumien.randomthings.network.messages.MessageOnlineDetector;
import lumien.randomthings.network.messages.MessagePotionVaporizerParticles;
import lumien.randomthings.network.messages.MessageRedstoneRemote;
import lumien.randomthings.network.messages.MessageSoundRecorder;
import lumien.randomthings.network.messages.MessageVoxelProjector;

public class PacketHandler
{
	public static final RandomThingsNetworkWrapper INSTANCE = new RandomThingsNetworkWrapper();

	public static void init()
	{
		INSTANCE.registerMessage(MessageOnlineDetector.class);
		INSTANCE.registerMessage(MessageChatDetector.class);
		INSTANCE.registerMessage(MessageAnalogEmitter.class);
		INSTANCE.registerMessage(MessageEnderLetter.class);
		INSTANCE.registerMessage(MessageEntityDetector.class);
		INSTANCE.registerMessage(MessagePotionVaporizerParticles.class);
		INSTANCE.registerMessage(MessageVoxelProjector.class);
		INSTANCE.registerMessage(MessageAdvancedItemCollector.class);
		INSTANCE.registerMessage(MessageItemFilter.class);
		INSTANCE.registerMessage(MessageLightRedirector.class);
		INSTANCE.registerMessage(MessageSoundRecorder.class);
		INSTANCE.registerMessage(MessageRedstoneRemote.class);
	}
}
