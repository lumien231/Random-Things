package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSetBiome implements IRTMessage
{
	BlockPos pos;
	int biome;
	int dimension;

	public MessageSetBiome(BlockPos pos, int biome)
	{
		this.pos = pos;
		this.biome = biome;
	}

	public MessageSetBiome()
	{

	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = MessageUtil.readBlockPos(buf);
		this.biome = buf.readInt();
		this.dimension = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		MessageUtil.writeBlockPos(pos, buf);
		buf.writeInt(this.biome);
		buf.writeInt(this.dimension);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		Biome b = Biome.getBiome(biome);

		if (b != null && player != null && player.getEntityWorld().provider.getDimension() == dimension)
		{
			WorldUtil.setBiome(player.getEntityWorld(), pos, b);
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
