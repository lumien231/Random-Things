package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityOnlineDetector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageOnlineDetector implements IRTMessage
{
	String username;

	BlockPos pos;

	public MessageOnlineDetector()
	{

	}

	public MessageOnlineDetector(String username, BlockPos pos)
	{
		this.username = username;
		this.pos = pos;
	}

	@Override
	public void toBytes(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, username);

		MessageUtil.writeBlockPos(pos, buffer);
	}

	@Override
	public void fromBytes(ByteBuf buffer)
	{
		username = ByteBufUtils.readUTF8String(buffer);

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
				if (te != null && te instanceof TileEntityOnlineDetector && pos.distanceSq(player.getPosition()) < 100)
				{
					TileEntityOnlineDetector od = (TileEntityOnlineDetector) worldObj.getTileEntity(pos);
					od.setUsername(username);
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
