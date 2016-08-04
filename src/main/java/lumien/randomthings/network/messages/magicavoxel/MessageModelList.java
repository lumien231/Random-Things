package lumien.randomthings.network.messages.magicavoxel;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import lumien.randomthings.client.gui.GuiVoxelProjector;
import lumien.randomthings.network.IRTMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageModelList implements IRTMessage
{
	List<String> modelList;

	public MessageModelList()
	{
		modelList = new ArrayList<String>();
	}
	
	public void addModel(String modelName)
	{
		this.modelList.add(modelName);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		int size = buf.readInt();

		for (int i = 0; i < size; i++)
		{
			modelList.add(ByteBufUtils.readUTF8String(buf));
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(modelList.size());

		for (String modelName : modelList)
		{
			ByteBufUtils.writeUTF8String(buf, modelName);
		}
	}

	@Override
	public void onMessage(MessageContext context)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				EntityPlayerSP player;
				if (Minecraft.getMinecraft().currentScreen instanceof GuiVoxelProjector)
				{
					GuiVoxelProjector gui = (GuiVoxelProjector) Minecraft.getMinecraft().currentScreen;
					gui.setModelList(MessageModelList.this.modelList);
				}
			}
		});
	}

	@Override
	public Side getHandlingSide()
	{
		return Side.CLIENT;
	}

}
