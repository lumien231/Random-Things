package lumien.randomthings.network.messages.sync;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.tileentity.TileEntityBiomeRadar;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageBiomeRadarAntenna implements IRTMessage
{
	BlockPos pos;
	String[] antennaBiomes;

	public MessageBiomeRadarAntenna()
	{
		antennaBiomes = new String[4];
	}

	public MessageBiomeRadarAntenna(String[] antennaBiomes, BlockPos pos)
	{
		this.pos = pos;
		this.antennaBiomes = antennaBiomes;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = MessageUtil.readBlockPos(buf);

		int amount = buf.readInt();

		for (int i = 0; i < amount; i++)
		{
			antennaBiomes[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		MessageUtil.writeBlockPos(pos, buf);

		int amount = 0;

		for (int i = 0; i < antennaBiomes.length; i++)
		{
			if (antennaBiomes[i] != null)
			{
				amount++;
			}
		}

		buf.writeInt(amount);
		for (int i = 0; i < antennaBiomes.length; i++)
		{
			if (antennaBiomes[i] != null)
			{
				ByteBufUtils.writeUTF8String(buf, antennaBiomes[i]);
			}
		}
	}

	@Override
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{

			@Override
			public void run()
			{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(pos);

				if (te instanceof TileEntityBiomeRadar)
				{
					((TileEntityBiomeRadar) te).setAntennaBiomes(antennaBiomes);
				}
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
