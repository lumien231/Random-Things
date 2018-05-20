package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityBlockDestabilizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerBlockDestabilizer extends ContainerTE<TileEntityBlockDestabilizer>
{
	public ContainerBlockDestabilizer(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
		case 0:
			te.toggleLazy();
			break;
		case 1:
			te.toggleFuzzy();
			break;
		case 2:
			te.resetLazy();
			break;
		}
	}
}
