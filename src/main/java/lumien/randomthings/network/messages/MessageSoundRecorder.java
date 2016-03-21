package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSoundRecorder implements IRTMessage
{
	String soundName;

	public MessageSoundRecorder()
	{
	}

	public MessageSoundRecorder(String soundName)
	{
		this.soundName = soundName;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.soundName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, soundName);
	}

	@Override
	public void onMessage(MessageContext context)
	{
		if (this.soundName != null && context.getServerHandler().playerEntity != null)
		{
			ItemStack equipped;

			if ((equipped = context.getServerHandler().playerEntity.getHeldItemMainhand()) != null && equipped.getItem() == ModItems.soundRecorder)
			{
				NBTTagCompound dataCompound = equipped.getSubCompound("soundData", true);

				NBTTagList soundList = dataCompound.getTagList("soundList", 8);

				for (int i = 0; i < soundList.tagCount(); i++)
				{
					String savedSoundName = soundList.getStringTagAt(i);

					if (savedSoundName.equals(this.soundName))
					{
						return;
					}
				}

				soundList.appendTag(new NBTTagString(this.soundName));

				if (soundList.tagCount() >= 9)
				{
					soundList.removeTag(0);
				}

				dataCompound.setTag("soundList", soundList);
			}
		}
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.SERVER;
	}

}
