package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneRepeater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerAdvancedRedstoneRepeater extends ContainerTE<TileEntityAdvancedRedstoneRepeater>
{
	public ContainerAdvancedRedstoneRepeater(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
		case 0:
			te.decreaseTurnOffDelay(1);
			break;
		case 1:
			te.increaseTurnOffDelay(1);
			break;
		case 2:
			te.decreaseTurnOnDelay(1);
			break;
		case 3:
			te.increaseTurnOnDelay(1);
			break;
		case 4:
			te.decreaseTurnOffDelay(10);
			break;
		case 5:
			te.increaseTurnOffDelay(10);
			break;
		case 6:
			te.decreaseTurnOnDelay(10);
			break;
		case 7:
			te.increaseTurnOnDelay(10);
			break;
		case 8:
			te.decreaseTurnOffDelay(100);
			break;
		case 9:
			te.increaseTurnOffDelay(100);
			break;
		case 10:
			te.decreaseTurnOnDelay(100);
			break;
		case 11:
			te.increaseTurnOnDelay(100);
			break;
		case 12:
			te.decreaseTurnOffDelay(1000);
			break;
		case 13:
			te.increaseTurnOffDelay(1000);
			break;
		case 14:
			te.decreaseTurnOnDelay(1000);
			break;
		case 15:
			te.increaseTurnOnDelay(1000);
			break;
		}
	}
}
