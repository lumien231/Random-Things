package lumien.randomthings.tileentity;

import java.util.List;

import lumien.randomthings.block.BlockItemCollector;
import lumien.randomthings.config.Numbers;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileEntityItemCollector extends TileEntityBase implements ITickable
{
	int currentTickRate = 20;
	int counter = 0;

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			counter++;
			if (counter >= currentTickRate)
			{
				counter = 0;

				List<EntityItem> entityItemList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.add(-Numbers.ITEM_COLLECTOR_RANGE, -Numbers.ITEM_COLLECTOR_RANGE, -Numbers.ITEM_COLLECTOR_RANGE), this.pos.add(Numbers.ITEM_COLLECTOR_RANGE + 1, Numbers.ITEM_COLLECTOR_RANGE + 1, Numbers.ITEM_COLLECTOR_RANGE + 1)), EntitySelectors.IS_ALIVE);
				boolean didSomething = false;

				if (!entityItemList.isEmpty())
				{
					EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockItemCollector.FACING);
					TileEntity te = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

					if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
					{
						IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
						for (EntityItem ei : entityItemList)
						{
							if (!ei.isDead)
							{
								ItemStack original = ei.getItem().copy();
								ItemStack left = ItemHandlerHelper.insertItemStacked(itemHandler, original, false);

								if (left.getCount() < original.getCount())
								{
									didSomething = true;
								}

								if (left.isEmpty() || left.getCount() == 0)
								{
									ei.setDead();
								}
								else
								{
									ei.setItem(left);
								}
							}
						}
					}
				}

				if (!didSomething)
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
				}
			}
		}
	}
}
