package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.handler.spectreilluminator.SpectreIlluminationClientHandler;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSpectreIllumination implements IRTMessage
{
	int dimension;
	long chunkLong;

	boolean illuminated;

	public MessageSpectreIllumination()
	{
	}

	public MessageSpectreIllumination(int dimension, long chunkLong, boolean illuminated)
	{
		this.dimension = dimension;
		this.chunkLong = chunkLong;
		this.illuminated = illuminated;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readInt();
		this.chunkLong = buf.readLong();
		this.illuminated = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(dimension);
		buf.writeLong(chunkLong);
		buf.writeBoolean(illuminated);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = FMLClientHandler.instance().getWorldClient();
			
			if (world.provider.getDimension() == dimension)
			{
				SpectreIlluminationClientHandler.setIlluminated(chunkLong, illuminated);
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
