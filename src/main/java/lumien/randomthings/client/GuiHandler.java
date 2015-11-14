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
		POTION_VAPORIZER(GuiIds.POTION_VAPORIZER,"lumien.randomthings.client.gui.GuiPotionVaporizer","lumien.randomthings.container.ContainerPotionVaporizer"),ENTITY_DETECTOR(GuiIds.ENTITY_DETECTOR, "lumien.randomthings.client.gui.GuiEntityDetector", "lumien.randomthings.container.ContainerEntityDetector"), ENDER_LETTER(GuiIds.ENDER_LETTER, "lumien.randomthings.client.gui.GuiEnderLetter", "lumien.randomthings.container.ContainerEnderLetter"), ENDER_MAILBOX(GuiIds.ENDER_MAILBOX, "lumien.randomthings.client.gui.GuiEnderMailbox", "lumien.randomthings.container.ContainerEnderMailbox"), DYEING_MACHINE(GuiIds.DYEING_MACHINE, "lumien.randomthings.client.gui.GuiDyeingMachine", "lumien.randomthings.container.ContainerDyeingMachine"), ONLINE_DETECTOR(GuiIds.ONLINE_DETECTOR, "lumien.randomthings.client.gui.GuiOnlineDetector", "lumien.randomthings.container.ContainerEmptyContainer"), CHAT_DETECTOR(GuiIds.CHAT_DETECTOR, "lumien.randomthings.client.gui.GuiChatDetector", "lumien.randomthings.container.ContainerEmptyContainer"), CRAFTING_RECIPE(GuiIds.CRAFTING_RECIPE, "lumien.randomthings.client.gui.GuiCraftingRecipe", "lumien.randomthings.container.ContainerCraftingRecipe"), REDSTONE_INTERFACE(GuiIds.REDSTONE_INTERFACE, "lumien.randomthings.client.gui.GuiRedstoneInterface", "lumien.randomthings.container.ContainerEmptyContainer"), IMBUING_STATION(GuiIds.IMBUING_STATION, "lumien.randomthings.client.gui.GuiImbuingStation", "lumien.randomthings.container.ContainerImbuingStation"), ANALOG_EMITTER(GuiIds.ANALOG_EMITTER, "lumien.randomthings.client.gui.GuiAnalogEmitter", "lumien.randomthings.container.ContainerAnalogEmitter");

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
