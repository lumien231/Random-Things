package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.item.ItemSoundRecorder;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePlayedSound implements IRTMessage
{
	String soundName;
	int recorderSlot;

	public MessagePlayedSound()
	{

	}

	public MessagePlayedSound(String soundName, int recorderSlot)
	{
		this.soundName = soundName;
		this.recorderSlot = recorderSlot;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.soundName = ByteBufUtils.readUTF8String(buf);
		this.recorderSlot = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, soundName);
		buf.writeInt(recorderSlot);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityPlayerMP player = context.getServerHandler().player;

				if (player != null && recorderSlot >= 0 && recorderSlot < player.inventory.getSizeInventory())
				{
					ItemStack recorderStack = player.inventory.getStackInSlot(recorderSlot);

					if (!recorderStack.isEmpty() && recorderStack.getItem() == ModItems.soundRecorder)
					{
						ItemSoundRecorder.recordSound(recorderStack, soundName);
					}
				}
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
