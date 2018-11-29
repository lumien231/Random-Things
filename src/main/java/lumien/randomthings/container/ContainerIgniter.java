package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityBlockDestabilizer;
import lumien.randomthings.tileentity.TileEntityIgniter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerIgniter extends ContainerTE<TileEntityIgniter>
{
	public ContainerIgniter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
			case 0:
				te.rotateMode();
				break;
		}
	}
}
