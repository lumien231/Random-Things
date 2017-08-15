package lumien.randomthings.network.messages;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.particles.ParticleFlooFlame;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageFlooParticles implements IRTMessage
{
	List<BlockPos> brickPositions;

	public MessageFlooParticles()
	{
	}

	public MessageFlooParticles(List<BlockPos> brickPositions)
	{
		this.brickPositions = brickPositions;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int size = buf.readInt();
		brickPositions = new ArrayList<BlockPos>(size);

		for (int i = 0; i < size; i++)
		{
			brickPositions.add(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(brickPositions.size());
		for (BlockPos p : brickPositions)
		{
			buf.writeInt(p.getX());
			buf.writeInt(p.getY());
			buf.writeInt(p.getZ());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageContext context)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;

		if (player != null)
		{
			World world = player.world;

			ParticleFlame.Factory factory = new ParticleFlame.Factory();

			for (BlockPos p : brickPositions)
			{
				for (int i = 0; i < 50; i++)
				{
					Particle particle = new ParticleFlooFlame(world, p.getX() + Math.random(), p.getY() + 1 + Math.random(), p.getZ() + Math.random(), 0, Math.random() * 0.1, 0);

					Minecraft.getMinecraft().effectRenderer.addEffect(particle);
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
