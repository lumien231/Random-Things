package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityAdvancedItemCollector;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAdvancedItemCollector implements IRTMessage
{
	int buttonPressed;
	BlockPos pos;

	public MessageAdvancedItemCollector()
	{

	}

	public MessageAdvancedItemCollector(int buttonPressed, BlockPos pos)
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

				if (te != null && te instanceof TileEntityAdvancedItemCollector && serverHandler.player.getDistanceSq(MessageAdvancedItemCollector.this.pos) < 64)
				{
					TileEntityAdvancedItemCollector advancedItemCollector = (TileEntityAdvancedItemCollector) te;

					switch (buttonPressed)
					{
					case 0:
						advancedItemCollector.setRangeX(advancedItemCollector.getRangeX() - 1);
						break;
					case 1:
						advancedItemCollector.setRangeX(advancedItemCollector.getRangeX() + 1);
						break;

					case 2:
						advancedItemCollector.setRangeY(advancedItemCollector.getRangeY() - 1);
						break;
					case 3:
						advancedItemCollector.setRangeY(advancedItemCollector.getRangeY() + 1);
						break;

					case 4:
						advancedItemCollector.setRangeZ(advancedItemCollector.getRangeZ() - 1);
						break;
					case 5:
						advancedItemCollector.setRangeZ(advancedItemCollector.getRangeZ() + 1);
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
