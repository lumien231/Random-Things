package lumien.randomthings.network;

import net.minecraft.util.BlockPos;
import io.netty.buffer.ByteBuf;

public class MessageUtil
{
	public static void writeBlockPos(BlockPos pos,ByteBuf buffer)
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
		
		return new BlockPos(x,y,z);
	}
}
