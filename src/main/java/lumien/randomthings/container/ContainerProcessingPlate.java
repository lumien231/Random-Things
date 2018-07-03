package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityProcessingPlate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerProcessingPlate extends ContainerTE<TileEntityProcessingPlate>
{

	public ContainerProcessingPlate(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
			case 0:
				this.te.rotateInsertFacing();
				break;
			case 1:
				this.te.rotateExtractFacing();
				break;
		}
		
	}

}
