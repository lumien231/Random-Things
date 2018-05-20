package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.item.ItemEnderLetter;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageEnderLetter implements IRTMessage
{
	String receiver;

	public MessageEnderLetter()
	{

	}

	public MessageEnderLetter(String receiver)
	{
		this.receiver = receiver;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.receiver = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, receiver);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		if (context.netHandler instanceof NetHandlerPlayServer)
		{
			NetHandlerPlayServer serverHandler = (NetHandlerPlayServer) context.netHandler;

			if (serverHandler.player != null)
			{
				ItemStack equipped = serverHandler.player.getHeldItemMainhand();

				if (equipped != null && equipped.getItem() instanceof ItemEnderLetter)
				{
					if (equipped.getTagCompound() == null || !equipped.getTagCompound().getBoolean("received"))
					{
						if (equipped.getTagCompound() == null)
						{
							equipped.setTagCompound(new NBTTagCompound());
						}

						equipped.getTagCompound().setString("receiver", receiver);
					}
				}
			}
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
