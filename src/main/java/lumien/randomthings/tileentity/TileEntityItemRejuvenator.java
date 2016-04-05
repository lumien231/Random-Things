package lumien.randomthings.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityItemRejuvenator extends TileEntityBase implements ITickable
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
		if (!this.worldObj.isRemote)
		{
			List<EntityItem> itemList = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.up(), this.pos.up().add(1, 1, 1)));

			for (EntityItem ei : itemList)
			{
				ei.setAgeToCreativeDespawnTime();
			}
		}
	}

	
}
