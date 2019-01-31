package lumien.randomthings.network;

import lumien.randomthings.network.messages.MessageAdvancedItemCollector;
import lumien.randomthings.network.messages.MessageAnalogEmitter;
import lumien.randomthings.network.messages.MessageChatDetector;
import lumien.randomthings.network.messages.MessageChunkAnalyzer;
import lumien.randomthings.network.messages.MessageContainerSignal;
import lumien.randomthings.network.messages.MessageEclipsedClock;
import lumien.randomthings.network.messages.MessageEnderLetter;
import lumien.randomthings.network.messages.MessageEntityDetector;
import lumien.randomthings.network.messages.MessageFlooParticles;
import lumien.randomthings.network.messages.MessageFlooToken;
import lumien.randomthings.network.messages.MessageGlobalChatDetector;
import lumien.randomthings.network.messages.MessageItemFilter;
import lumien.randomthings.network.messages.MessageLightRedirector;
import lumien.randomthings.network.messages.MessageSpectreIllumination;
import lumien.randomthings.network.messages.MessageNotification;
import lumien.randomthings.network.messages.MessageNotificationInterface;
import lumien.randomthings.network.messages.MessageOnlineDetector;
import lumien.randomthings.network.messages.MessagePlayedSound;
import lumien.randomthings.network.messages.MessagePotionVaporizerParticles;
import lumien.randomthings.network.messages.MessageRedstoneRemote;
import lumien.randomthings.network.messages.MessageSelectSound;
import lumien.randomthings.network.messages.MessageSetBiome;
import lumien.randomthings.network.messages.MessageVoxelProjector;
import lumien.randomthings.network.messages.magicavoxel.MessageModelData;
import lumien.randomthings.network.messages.magicavoxel.MessageModelList;
import lumien.randomthings.network.messages.magicavoxel.MessageModelRequest;
import lumien.randomthings.network.messages.magicavoxel.MessageModelRequestUpdate;
import lumien.randomthings.network.messages.sync.MessageBiomeRadarAntenna;

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
		INSTANCE.registerMessage(MessageRedstoneRemote.class);
		INSTANCE.registerMessage(MessageModelData.class);
		INSTANCE.registerMessage(MessageModelRequest.class);
		INSTANCE.registerMessage(MessageModelRequestUpdate.class);
		INSTANCE.registerMessage(MessageModelList.class);
		INSTANCE.registerMessage(MessageBiomeRadarAntenna.class);
		INSTANCE.registerMessage(MessageContainerSignal.class);
		INSTANCE.registerMessage(MessageSetBiome.class);
		INSTANCE.registerMessage(MessageNotification.class);
		INSTANCE.registerMessage(MessageNotificationInterface.class);
		INSTANCE.registerMessage(MessageFlooParticles.class);
		INSTANCE.registerMessage(MessageFlooToken.class);
		INSTANCE.registerMessage(MessageGlobalChatDetector.class);
		INSTANCE.registerMessage(MessagePlayedSound.class);
		INSTANCE.registerMessage(MessageSelectSound.class);
		INSTANCE.registerMessage(MessageChunkAnalyzer.class);
		INSTANCE.registerMessage(MessageSpectreIllumination.class);
		INSTANCE.registerMessage(MessageEclipsedClock.class);
	}
}
