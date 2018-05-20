package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageChatDetector implements IRTMessage
{
	String chatMessage;
	boolean consume;

	BlockPos pos;

	public MessageChatDetector()
	{

	}

	public MessageChatDetector(String chatMessage, boolean consume, BlockPos pos)
	{
		this.chatMessage = chatMessage;
		this.pos = pos;
		this.consume = consume;
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, chatMessage);
		buffer.writeBoolean(consume);

		MessageUtil.writeBlockPos(pos, buffer);
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		chatMessage = ByteBufUtils.readUTF8String(buffer);
		consume = buffer.readBoolean();

		pos = MessageUtil.readBlockPos(buffer);
	}

	@Override
	public void onMessage(final MessageContext ctx)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				World worldObj = player.world;
				TileEntity te = worldObj.getTileEntity(pos);
				if (te != null && te instanceof TileEntityChatDetector && pos.distanceSq(player.getPosition()) < 100)
				{
					TileEntityChatDetector od = (TileEntityChatDetector) worldObj.getTileEntity(pos);
					od.setChatMessage(chatMessage);
					od.setConsume(consume);
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
