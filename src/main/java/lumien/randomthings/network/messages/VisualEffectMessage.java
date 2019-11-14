package lumien.randomthings.network.messages;

import java.util.function.Consumer;

import lumien.randomthings.client.vfx.EFFECT;
import lumien.randomthings.client.vfx.VFXHandler;
import lumien.randomthings.client.vfx.VisualEffect;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class VisualEffectMessage  implements IRTMessage
{
	EFFECT type;
	Consumer<PacketBuffer> parameter;
	
	VisualEffect effect;
	
	public VisualEffectMessage()
	{
		
	}
	
	public VisualEffectMessage(EFFECT type, Consumer<PacketBuffer> parameter) {
		this.type = type;
		this.parameter = parameter;
	}

	@Override
	public void read(PacketBuffer pb)
	{
		this.type = EFFECT.values()[pb.readInt()];
		try
		{
			this.effect = this.type.getEffectClass().newInstance();
			this.effect.readData(pb);
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void write(PacketBuffer pb)
	{
		pb.writeInt(this.type.ordinal());
		this.parameter.accept(pb);
	}

	@Override
	public void handle(Context ctx)
	{
		ctx.setPacketHandled(true);
		ctx.enqueueWork(() -> {
			VFXHandler.INSTANCE.addEffect(this.effect);
		});
	}
	
}
