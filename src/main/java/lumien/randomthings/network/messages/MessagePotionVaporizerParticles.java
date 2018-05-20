package lumien.randomthings.network.messages;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePotionVaporizerParticles implements IRTMessage
{
	List<BlockPos> affectedBlocks;
	int color;

	public MessagePotionVaporizerParticles(List<BlockPos> affectedBlocks, int color)
	{
		this.affectedBlocks = affectedBlocks;
		this.color = color;
	}

	public MessagePotionVaporizerParticles()
	{
		this.affectedBlocks = new ArrayList<>();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		color = buf.readInt();
		int length = buf.readInt();

		for (int i = 0; i < length; i++)
		{
			affectedBlocks.add(MessageUtil.readBlockPos(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(color);
		buf.writeInt(affectedBlocks.size());

		for (BlockPos pos : affectedBlocks)
		{
			MessageUtil.writeBlockPos(pos, buf);
		}
	}

	@Override
	public void onMessage(MessageContext context)
	{
		if (Minecraft.getMinecraft().world != null)
		{
			Color c = new Color(color);
			for (BlockPos pos : affectedBlocks)
			{
				Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 1D / 255D * c.getRed(), 1D / 255D * c.getGreen(), 1D / 255D * c.getBlue(), 0);
			}
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
