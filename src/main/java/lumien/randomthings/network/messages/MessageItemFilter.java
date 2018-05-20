package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerItemFilter;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageItemFilter implements IRTMessage
{
	int buttonPressed;

	public MessageItemFilter()
	{

	}

	public MessageItemFilter(int buttonPressed)
	{
		this.buttonPressed = buttonPressed;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		buttonPressed = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(buttonPressed);
	}

	@Override
	public void onMessage(final MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				NetHandlerPlayServer serverHandler = context.getServerHandler();

				Container container = context.getServerHandler().player.openContainer;

				if (container != null && container instanceof ContainerItemFilter)
				{
					ContainerItemFilter itemFilterContainer = (ContainerItemFilter) container;

					switch (buttonPressed)
					{
					case 0:
						itemFilterContainer.repres.toggleMetadata();
						break;
					case 1:
						itemFilterContainer.repres.toggleOreDict();
						break;
					case 2:
						itemFilterContainer.repres.toggleNBT();
						break;
					case 3:
						itemFilterContainer.repres.toggleListType();
						break;
					}
				}
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
