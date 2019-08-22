package lumien.randomthings.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * IRTMessage
 */
public interface IRTMessage
{
	public void read(PacketBuffer pb);

	public void write(PacketBuffer pb);

	public default void enqueue(Supplier<Context> sup)
	{
		Context ctx = sup.get();
		ctx.enqueueWork(() -> handle(ctx));
	}

	public void handle(Context ctx);
}