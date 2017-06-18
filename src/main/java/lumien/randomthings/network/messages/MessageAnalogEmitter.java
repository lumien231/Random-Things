package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerAnalogEmitter;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityAnalogEmitter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAnalogEmitter implements IRTMessage
{
	BlockPos pos;
	int level;

	public MessageAnalogEmitter(BlockPos pos, int level)
	{
		this.pos = pos;
		this.level = level;
	}

	public MessageAnalogEmitter()
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = MessageUtil.readBlockPos(buf);
		level = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		MessageUtil.writeBlockPos(pos, buf);
		buf.writeInt(level);
	}

	@Override
	public void onMessage(final MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				if (level > 0 && level < 16)
				{
					if (context.netHandler instanceof NetHandlerPlayServer)
					{
						NetHandlerPlayServer handler = (NetHandlerPlayServer) context.netHandler;

						EntityPlayerMP player = handler.player;

						if (player != null && player.openContainer != null && player.openContainer instanceof ContainerAnalogEmitter)
						{
							TileEntity te = player.world.getTileEntity(pos);

							if (te != null && te instanceof TileEntityAnalogEmitter)
							{
								TileEntityAnalogEmitter analogEmitter = (TileEntityAnalogEmitter) te;

								analogEmitter.setLevel(level);
							}
						}
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
