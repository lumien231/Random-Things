package lumien.randomthings.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SimpleTeleporter extends Teleporter
{
	public SimpleTeleporter(WorldServer worldIn)
	{
		super(worldIn);
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw)
	{

	}

	@Override
	public void removeStalePortalLocations(long p_85189_1_)
	{

	}
}
