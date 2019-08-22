package lumien.randomthings.network.messages;

import io.netty.buffer.Unpooled;
import lumien.randomthings.container.ISignalContainer;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * ContainerSignalMessage
 */
public class ContainerSignalMessage implements IRTMessage
{
	int id;
	PacketBuffer data;

	public ContainerSignalMessage(int id, PacketBuffer data)
	{
		this.id = id;
		this.data = data;
	}

	public ContainerSignalMessage()
	{

	}

	@Override
	public void read(PacketBuffer pb)
	{
		this.id = pb.readInt();

		byte[] byteArray = pb.readByteArray();
		this.data = new PacketBuffer(Unpooled.copiedBuffer(byteArray));
	}

	@Override
	public void write(PacketBuffer pb)
	{
		pb.writeInt(id);
		pb.writeByteArray(data.array());
	}

	@Override
	public void handle(Context context)
	{
		ServerPlayerEntity player = context.getSender();

		Container container = player.openContainer;

		if (container instanceof ISignalContainer)
		{
			((ISignalContainer) container).handle(id, data);
		}
	}


}