package lumien.randomthings.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityItemSealer extends TileEntityBase implements ITickable
{
	public static DataParameter<Integer> sealTime = EntityDataManager.createKey(EntityItem.class, DataSerializers.VARINT);

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
		if (!this.world.isRemote)
		{
			List<EntityItem> itemList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.up(), this.pos.up().add(1, 1, 1)));

			for (EntityItem ei : itemList)
			{
				ei.setPickupDelay(20 * 30);
			}
		}
	}

}
