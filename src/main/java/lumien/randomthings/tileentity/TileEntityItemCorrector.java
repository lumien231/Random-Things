package lumien.randomthings.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityItemCorrector extends TileEntityBase implements ITickable
{

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public void update()
	{
		List<EntityItem> itemList = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.up(), this.pos.up().add(1, 1, 1)));

		for (EntityItem ei : itemList)
		{
			if (Math.abs(ei.motionX) < Math.abs(ei.motionZ))
			{
				if (ei.posX != this.pos.getX() + 0.5)
				{
					ei.setPositionAndUpdate(this.pos.getX() + 0.5, ei.posY, ei.posZ);
				}
				
				if (ei.motionX != 0)
				{
					ei.motionX = 0;
				}
			}
			else if (Math.abs(ei.motionX) > Math.abs(ei.motionZ))
			{
				if (ei.posZ != this.pos.getZ() + 0.5)
				{
					ei.setPositionAndUpdate(ei.posX, ei.posY, this.pos.getZ() + 0.5);
				}
				
				if (ei.motionZ != 0)
				{
					ei.motionZ = 0;
				}
			}
		}
	}
}
