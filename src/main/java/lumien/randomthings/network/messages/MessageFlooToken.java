package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.particles.ParticleFlooFlame;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFlooToken implements IRTMessage
{
	int dimension;
	double posX;
	double posY;
	double posZ;

	public MessageFlooToken()
	{

	}

	public MessageFlooToken(int dimension, double posX, double posY, double posZ)
	{
		this.dimension = dimension;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readInt();
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.dimension);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageContext context)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;

		if (player != null)
		{
			World world = player.world;

			if (world.provider.getDimension() == dimension)
			{
				for (float modX = -1; modX <= 1; modX += 0.05)
				{
					for (float modZ = -1; modZ <= 1; modZ += 0.05)
					{
						ParticleFlooFlame particle = new ParticleFlooFlame(world, posX + modX + (Math.random() * 0.1 - 0.05), posY - 1, posZ + modZ + (Math.random() * 0.1 - 0.05), 0, Math.random() * 0.3 + 0.1, 0);

						Minecraft.getMinecraft().effectRenderer.addEffect(particle);
					}
				}
			}
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
