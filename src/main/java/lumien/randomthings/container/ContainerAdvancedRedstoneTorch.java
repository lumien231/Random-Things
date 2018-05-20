package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerAdvancedRedstoneTorch extends ContainerTE<TileEntityAdvancedRedstoneTorch>
{
	public ContainerAdvancedRedstoneTorch(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
		case 0:
			te.decreaseSignalStrengthOff(1);
			break;
		case 1:
			te.increaseSignalStrengthOff(1);
			break;
		case 2:
			te.decreaseSignalStrengthOn(1);
			break;
		case 3:
			te.increaseSignalStrengthOn(1);
			break;
		case 4:
			te.decreaseSignalStrengthOff(4);
			break;
		case 5:
			te.increaseSignalStrengthOff(4);
			break;
		case 6:
			te.decreaseSignalStrengthOn(4);
			break;
		case 7:
			te.increaseSignalStrengthOn(4);
			break;
		}
	}
}
