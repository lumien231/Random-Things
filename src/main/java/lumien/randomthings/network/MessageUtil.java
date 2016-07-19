package lumien.randomthings.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageUtil
{
	public static void writeBlockPos(BlockPos pos, ByteBuf buffer)
	{
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
	}

	public static BlockPos readBlockPos(ByteBuf buffer)
	{
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();

		return new BlockPos(x, y, z);
	}

	public static void sendToAllWatchingPos(World worldObj, BlockPos pos, IMessage message)
	{
		if (worldObj.isBlockLoaded(pos))
		{
			try
			{
				Chunk c = worldObj.getChunkFromBlockCoords(pos);

				PlayerChunkMap playerManager = ((WorldServer) worldObj).getPlayerChunkMap();

				PlayerChunkMapEntry playerInstance = playerManager.getEntry(c.xPosition, c.zPosition);
				if (playerInstance != null)
				{
					playerInstance.sendPacket(PacketHandler.INSTANCE.getPacketFrom(message));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
