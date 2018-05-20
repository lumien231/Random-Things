package lumien.randomthings.network.messages;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.notifications.NotificationToast;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNotification implements IRTMessage
{
	String title;
	String body;
	ItemStack icon;

	public MessageNotification(String title, String body, ItemStack icon)
	{
		this.title = title;
		this.body = body;
		this.icon = icon;
	}

	public MessageNotification()
	{
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.title = ByteBufUtils.readUTF8String(buf);
		this.body = ByteBufUtils.readUTF8String(buf);
		this.icon = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, title);
		ByteBufUtils.writeUTF8String(buf, body);
		ByteBufUtils.writeItemStack(buf, icon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().getToastGui().add(new NotificationToast(title, body, icon));
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
