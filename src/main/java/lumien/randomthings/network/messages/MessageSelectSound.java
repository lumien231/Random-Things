package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerSoundRecorder;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSelectSound implements IRTMessage
{
	String selectedSound;

	public MessageSelectSound()
	{

	}

	public MessageSelectSound(String selectedSound)
	{
		this.selectedSound = selectedSound;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.selectedSound = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, selectedSound);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{

			@Override
			public void run()
			{
				EntityPlayerMP player = context.getServerHandler().player;

				if (selectedSound != null && player != null && player.openContainer instanceof ContainerSoundRecorder)
				{
					ContainerSoundRecorder csr = (ContainerSoundRecorder) player.openContainer;

					csr.outputSound(selectedSound);
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
