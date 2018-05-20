package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityNotificationInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageNotificationInterface implements IRTMessage
{
	String newTitle;
	String newDescription;

	BlockPos tePos;

	public MessageNotificationInterface(String newTitle, String newDescription, BlockPos tePos)
	{
		this.newTitle = newTitle;
		this.newDescription = newDescription;
		this.tePos = tePos;
	}

	public MessageNotificationInterface()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.newTitle = ByteBufUtils.readUTF8String(buf);
		this.newDescription = ByteBufUtils.readUTF8String(buf);

		this.tePos = MessageUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, newTitle);
		ByteBufUtils.writeUTF8String(buf, newDescription);

		MessageUtil.writeBlockPos(tePos, buf);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		World world = context.getServerHandler().player.world;

		if (world != null)
		{
			TileEntity te = world.getTileEntity(tePos);

			if (te instanceof TileEntityNotificationInterface)
			{
				TileEntityNotificationInterface notInterface = (TileEntityNotificationInterface) te;
				notInterface.setData(newTitle, newDescription);
			}
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
