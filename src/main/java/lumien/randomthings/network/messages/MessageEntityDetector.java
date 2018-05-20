package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageEntityDetector implements IRTMessage
{
	int buttonPressed;
	BlockPos pos;

	public MessageEntityDetector()
	{

	}

	public MessageEntityDetector(int buttonPressed, BlockPos pos)
	{
		this.buttonPressed = buttonPressed;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		buttonPressed = buf.readInt();

		pos = MessageUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(buttonPressed);
		MessageUtil.writeBlockPos(pos, buf);
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

				TileEntity te = serverHandler.player.world.getTileEntity(pos);

				if (te != null && te instanceof TileEntityEntityDetector && serverHandler.player.getDistanceSq(MessageEntityDetector.this.pos) < 64)
				{
					TileEntityEntityDetector entityDetector = (TileEntityEntityDetector) te;

					switch (buttonPressed)
					{
					case 0:
						entityDetector.setRangeX(entityDetector.getRangeX() - 1);
						break;
					case 1:
						entityDetector.setRangeX(entityDetector.getRangeX() + 1);
						break;

					case 2:
						entityDetector.setRangeY(entityDetector.getRangeY() - 1);
						break;
					case 3:
						entityDetector.setRangeY(entityDetector.getRangeY() + 1);
						break;

					case 4:
						entityDetector.setRangeZ(entityDetector.getRangeZ() - 1);
						break;
					case 5:
						entityDetector.setRangeZ(entityDetector.getRangeZ() + 1);
						break;

					case 6:
						entityDetector.cycleFilter();
						break;

					case 7:
						entityDetector.toggleInvert();
						break;
					case 8:
						entityDetector.toggleStrongOutput();
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
