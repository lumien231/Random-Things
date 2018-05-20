package lumien.randomthings.container;

import lumien.randomthings.handler.magicavoxel.ServerModelLibrary;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.magicavoxel.MessageModelList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.world.World;

public class ContainerVoxelProjector extends Container
{
	public ContainerVoxelProjector(EntityPlayer player, World world, int x, int y, int z)
	{

	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);

		if (listener instanceof EntityPlayerMP)
		{
			MessageModelList listMessage = new MessageModelList();
			for (String modelName : ServerModelLibrary.getInstance().getModelList())
			{
				listMessage.addModel(modelName);
			}

			PacketHandler.INSTANCE.sendTo(listMessage, (EntityPlayerMP) listener);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
