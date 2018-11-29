package lumien.randomthings.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lumien.randomthings.lib.GuiIds;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	private enum GuiContainerConnection
	{
		NOTIFICATION_INTERFACE(GuiIds.NOTIFICATION_INTERFACE, "lumien.randomthings.client.gui.GuiNotificationInterface", "lumien.randomthings.container.ContainerNotificationInterface"), IRON_DROPPER(GuiIds.IRON_DROPPER, "lumien.randomthings.client.gui.GuiIronDropper", "lumien.randomthings.container.ContainerIronDropper"), REDSTONE_OBSERVER(GuiIds.REDSTONE_OBSERVER, "lumien.randomthings.client.gui.GuiRedstoneObserver", "lumien.randomthings.container.ContainerEmptyContainer"), FILTERED_SUPERLUBRICENT_PLATFORM(GuiIds.FILTERED_SUPERLUBRICENT_PLATFORM, "lumien.randomthings.client.gui.GuiFilteredSuperLubricentPlatform", "lumien.randomthings.container.ContainerFilteredSuperLubricentPlatform"), FILTERED_REDIRECTOR_PLATE(GuiIds.FILTERED_REDIRECTOR_PLATE, "lumien.randomthings.client.gui.GuiFilteredRedirectorPlate", "lumien.randomthings.container.ContainerFilteredRedirectorPlate"), REDSTONE_REMOTE_USE(GuiIds.REDSTONE_REMOTE_USE, "lumien.randomthings.client.gui.redstoneremote.GuiRedstoneRemoteUse", "lumien.randomthings.container.ContainerEmptyContainer"), REDSTONE_REMOTE_EDIT(GuiIds.REDSTONE_REMOTE_EDIT, "lumien.randomthings.client.gui.redstoneremote.GuiRedstoneRemoteEdit", "lumien.randomthings.container.ContainerRedstoneRemote"), ADVANCED_REDSTONE_INTERFACE(GuiIds.ADVANCED_REDSTONE_INTERFACE, "lumien.randomthings.client.gui.redstoneinterface.GuiAdvancedRedstoneInterface", "lumien.randomthings.container.redstoneinterface.ContainerAdvancedRedstoneInterface"), ADVANCED_ITEM_COLLECTOR(GuiIds.ADVANCED_ITEM_COLLECTOR, "lumien.randomthings.client.gui.GuiAdvancedItemCollector", "lumien.randomthings.container.ContainerAdvancedItemCollector"), ITEM_FILTER(GuiIds.ITEM_FILTER, "lumien.randomthings.client.gui.GuiItemFilter", "lumien.randomthings.container.ContainerItemFilter"), VOXEL_PROJECTOR(GuiIds.VOXEL_PROJECTOR, "lumien.randomthings.client.gui.GuiVoxelProjector", "lumien.randomthings.container.ContainerVoxelProjector"), POTION_VAPORIZER(GuiIds.POTION_VAPORIZER, "lumien.randomthings.client.gui.GuiPotionVaporizer", "lumien.randomthings.container.ContainerPotionVaporizer"), ENTITY_DETECTOR(GuiIds.ENTITY_DETECTOR, "lumien.randomthings.client.gui.GuiEntityDetector", "lumien.randomthings.container.ContainerEntityDetector"), ENDER_LETTER(GuiIds.ENDER_LETTER, "lumien.randomthings.client.gui.GuiEnderLetter", "lumien.randomthings.container.ContainerEnderLetter"), ENDER_MAILBOX(GuiIds.ENDER_MAILBOX, "lumien.randomthings.client.gui.GuiEnderMailbox", "lumien.randomthings.container.ContainerEnderMailbox"), DYEING_MACHINE(GuiIds.DYEING_MACHINE, "lumien.randomthings.client.gui.GuiDyeingMachine", "lumien.randomthings.container.ContainerDyeingMachine"), ONLINE_DETECTOR(GuiIds.ONLINE_DETECTOR, "lumien.randomthings.client.gui.GuiOnlineDetector", "lumien.randomthings.container.ContainerEmptyContainer"), CHAT_DETECTOR(GuiIds.CHAT_DETECTOR, "lumien.randomthings.client.gui.GuiChatDetector", "lumien.randomthings.container.ContainerEmptyContainer"), CRAFTING_RECIPE(GuiIds.CRAFTING_RECIPE, "lumien.randomthings.client.gui.GuiCraftingRecipe", "lumien.randomthings.container.ContainerCraftingRecipe"), BASIC_REDSTONE_INTERFACE(GuiIds.BASIC_REDSTONE_INTERFACE, "lumien.randomthings.client.gui.redstoneinterface.GuiBasicRedstoneInterface", "lumien.randomthings.container.ContainerEmptyContainer"), IMBUING_STATION(GuiIds.IMBUING_STATION, "lumien.randomthings.client.gui.GuiImbuingStation", "lumien.randomthings.container.ContainerImbuingStation"), ANALOG_EMITTER(GuiIds.ANALOG_EMITTER, "lumien.randomthings.client.gui.GuiAnalogEmitter", "lumien.randomthings.container.ContainerAnalogEmitter"), INVENTORY_TESTER(GuiIds.INVENTORY_TESTER, "lumien.randomthings.client.gui.GuiInventoryTester", "lumien.randomthings.container.ContainerInventoryTester"), GLOBAL_CHAT_DETECTOR(GuiIds.GLOBAL_CHAT_DETECTOR, "lumien.randomthings.client.gui.GuiGlobalChatDetector", "lumien.randomthings.container.ContainerGlobalChatDetector"), BLOCK_DESTABILIZER(GuiIds.BLOCK_DESTABILIZER, "lumien.randomthings.client.gui.GuiBlockDestabilizer", "lumien.randomthings.container.ContainerBlockDestabilizer"), ADVANCED_REDSTONE_REPEATER(GuiIds.ADVANCED_REDSTONE_REPEATER, "lumien.randomthings.client.gui.GuiAdvancedRedstoneRepeater", "lumien.randomthings.container.ContainerAdvancedRedstoneRepeater"), SOUND_RECORDER(GuiIds.SOUND_RECORDER, "lumien.randomthings.client.gui.GuiSoundRecorder", "lumien.randomthings.container.ContainerSoundRecorder"), PORTABLE_SOUND_DAMPENER(GuiIds.PORTABLE_SOUND_DAMPENER, "lumien.randomthings.client.gui.GuiPortableSoundDampener", "lumien.randomthings.container.ContainerPortableSoundDampener"), SOUND_DAMPENER(GuiIds.SOUND_DAMPENER, "lumien.randomthings.client.gui.GuiSoundDampener", "lumien.randomthings.container.ContainerSoundDampener"), ADVANCED_REDSTONE_TORCH(GuiIds.ADVANCED_REDSTONE_TORCH, "lumien.randomthings.client.gui.GuiAdvancedRedstoneTorch", "lumien.randomthings.container.ContainerAdvancedRedstoneTorch"), PROCESSING_PLATE(GuiIds.PROCESSING_PLATE, "lumien.randomthings.client.gui.GuiProcessingPlate", "lumien.randomthings.container.ContainerProcessingPlate"), EXTRACTION_PLATE(GuiIds.EXTRACTION_PLATE, "lumien.randomthings.client.gui.GuiExtractionPlate", "lumien.randomthings.container.ContainerExtractionPlate"), CHUNK_ANALYZER(GuiIds.CHUNK_ANALYZER, "lumien.randomthings.client.gui.GuiChunkAnalyzer", "lumien.randomthings.container.ContainerChunkAnalyzer"), IGNITER(GuiIds.IGNITER, "lumien.randomthings.client.gui.GuiIgniter", "lumien.randomthings.container.ContainerIgniter");

		int guiID;
		String guiClass;
		String containerClass;

		private GuiContainerConnection(int guiID, String guiClass, String containerClass)
		{
			this.guiID = guiID;
			this.guiClass = guiClass;
			this.containerClass = containerClass;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		for (GuiContainerConnection registered : GuiContainerConnection.values())
		{
			if (ID == registered.guiID)
			{
				Class containerClass = null;
				try
				{
					containerClass = Class.forName(registered.containerClass);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}

				if (containerClass != null)
				{
					Constructor constructor = null;
					try
					{
						constructor = containerClass.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class);
					}
					catch (NoSuchMethodException e)
					{
						e.printStackTrace();
					}
					catch (SecurityException e)
					{
						e.printStackTrace();
					}

					if (constructor != null)
					{
						try
						{
							Container container = (Container) constructor.newInstance(player, world, x, y, z);
							return container;
						}
						catch (InstantiationException e)
						{
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		for (GuiContainerConnection registered : GuiContainerConnection.values())
		{
			if (ID == registered.guiID)
			{
				Class guiClass = null;
				try
				{
					guiClass = Class.forName(registered.guiClass);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}

				if (guiClass != null)
				{
					Constructor constructor = null;
					try
					{
						constructor = guiClass.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class);
					}
					catch (NoSuchMethodException e)
					{
						e.printStackTrace();
					}
					catch (SecurityException e)
					{
						e.printStackTrace();
					}

					if (constructor != null)
					{
						try
						{
							GuiContainer guiContainer = (GuiContainer) constructor.newInstance(player, world, x, y, z);
							return guiContainer;
						}
						catch (InstantiationException e)
						{
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
						catch (IllegalArgumentException e)
						{
							e.printStackTrace();
						}
						catch (InvocationTargetException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}

}
