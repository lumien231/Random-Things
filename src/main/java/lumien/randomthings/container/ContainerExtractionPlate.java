package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityExtractionPlate;
import lumien.randomthings.tileentity.TileEntityProcessingPlate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerExtractionPlate extends ContainerTE<TileEntityExtractionPlate>
{

	public ContainerExtractionPlate(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		this.te.rotateExtractFacing();
	}

}
