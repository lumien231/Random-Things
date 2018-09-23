package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.container.ContainerChunkAnalyzer;
import lumien.randomthings.item.ItemChunkAnalyzer;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageChunkAnalyzer implements IRTMessage
{
	public enum ACTION
	{
		START, STOP;
	}
	
	public MessageChunkAnalyzer()
	{
		
	}
	
	public MessageChunkAnalyzer(ACTION action)
	{
		this.action = action;
	}

	ACTION action;

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.action = ACTION.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(action.ordinal());
	}

	@Override
	public void onMessage(MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			EntityPlayer player = context.getServerHandler().player;

			ItemStack held = player.getHeldItemMainhand();
			
			if (player != null && !held.isEmpty() && held.getItem() instanceof ItemChunkAnalyzer && player.openContainer instanceof ContainerChunkAnalyzer)
			{
				if (held.getSubCompound("result")!=null)
				{
					held.getTagCompound().removeTag("result");
					player.inventoryContainer.detectAndSendChanges();
				}
				
				((ContainerChunkAnalyzer) player.openContainer).startScanning();
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
