package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.entitys.EntityEclipsedClock;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageEclipsedClock implements IRTMessage
{
	int entityID;

	public MessageEclipsedClock()
	{
	}

	public MessageEclipsedClock(int entityID)
	{
		this.entityID = entityID;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityID);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				World world = Minecraft.getMinecraft().world;

				Entity e;
				if (world != null && (e = world.getEntityByID(entityID)) instanceof EntityEclipsedClock)
				{
					EntityEclipsedClock clock = (EntityEclipsedClock) e;
					
					clock.triggerAnimation();
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
