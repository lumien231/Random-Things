package lumien.randomthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class ContainerVoxelProjector extends Container
{
	public ContainerVoxelProjector(EntityPlayer player, World world, int x, int y, int z)
	{
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
