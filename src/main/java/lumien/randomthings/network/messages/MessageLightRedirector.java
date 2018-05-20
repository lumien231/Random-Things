package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.block.BlockLightRedirector;
import lumien.randomthings.network.IRTMessage;
import lumien.randomthings.network.MessageUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageLightRedirector implements IRTMessage
{
	int dimension;
	BlockPos pos;

	public MessageLightRedirector()
	{

	}

	public MessageLightRedirector(int dimension, BlockPos pos)
	{
		this.dimension = dimension;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readInt();

		this.pos = MessageUtil.readBlockPos(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(dimension);

		MessageUtil.writeBlockPos(pos, buf);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		WorldClient worldObj = Minecraft.getMinecraft().world;

		if (worldObj != null && worldObj.provider.getDimension() == dimension && worldObj.isBlockLoaded(pos))
		{
			for (EnumFacing facing : EnumFacing.VALUES)
			{
				if (!(worldObj.getBlockState(pos.offset(facing)).getBlock() instanceof BlockLightRedirector))
				{
					IBlockState state = worldObj.getBlockState(pos.offset(facing));
					worldObj.notifyBlockUpdate(pos.offset(facing), state, state, 3);
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
