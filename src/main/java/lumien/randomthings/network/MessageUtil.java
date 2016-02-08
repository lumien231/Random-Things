package lumien.randomthings.network;

import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.asm.MCPNames;
import net.minecraft.network.Packet;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageUtil
{
	static Method sendMethod;
	static Method getInstanceMethod;

	static
	{
		try
		{
			Class playerInstanceClass = PlayerManager.class.getDeclaredClasses()[0];
			getInstanceMethod = PlayerManager.class.getDeclaredMethod(MCPNames.method("func_72690_a"), int.class, int.class, boolean.class);
			getInstanceMethod.setAccessible(true);



			sendMethod = playerInstanceClass.getDeclaredMethod(MCPNames.method("func_151251_a"), Packet.class);
			sendMethod.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


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

				PlayerManager playerManager = ((WorldServer) worldObj).getPlayerManager();

				Object playerInstance = getInstanceMethod.invoke(playerManager, c.xPosition, c.zPosition, false);
				sendMethod.invoke(playerInstance, PacketHandler.INSTANCE.getPacketFrom(message));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
