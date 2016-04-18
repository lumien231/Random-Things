package lumien.randomthings.tileentity;

import java.util.List;

import lumien.randomthings.block.BlockItemCollector;
import lumien.randomthings.config.Numbers;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityItemCollector extends TileEntityBase implements ITickable
{
	int currentTickRate = 20;
	int counter = 0;

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
			counter++;
			if (counter >= currentTickRate)
			{
				counter = 0;

				List<EntityItem> entityItemList = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.add(-Numbers.ITEM_COLLECTOR_RANGE, -Numbers.ITEM_COLLECTOR_RANGE, -Numbers.ITEM_COLLECTOR_RANGE), this.pos.add(Numbers.ITEM_COLLECTOR_RANGE + 1, Numbers.ITEM_COLLECTOR_RANGE + 1, Numbers.ITEM_COLLECTOR_RANGE + 1)));

				if (entityItemList.isEmpty())
				{
					if (currentTickRate < 20)
					{
						currentTickRate++;
					}
				}
				else
				{
					if (currentTickRate > 1)
					{
						currentTickRate--;
					}

					EnumFacing facing = this.worldObj.getBlockState(this.pos).getValue(BlockItemCollector.FACING);
					TileEntity te = this.worldObj.getTileEntity(this.pos.offset(facing.getOpposite()));

					if (te != null && te instanceof IInventory)
					{
						IInventory inventory = (IInventory) te;
						for (EntityItem ei : entityItemList)
						{
							ItemStack left = TileEntityHopper.putStackInInventoryAllSlots(inventory, ei.getEntityItem(), facing);

							if (left == null || left.stackSize == 0)
							{
								ei.setDead();
							}
							else
							{
								ei.setEntityItemStack(left);
							}
						}
					}
				}
			}
		}
	}
}
