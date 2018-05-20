package lumien.randomthings.tileentity;

import java.util.List;

import lumien.randomthings.block.BlockItemCollector;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.item.ItemItemFilter;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileEntityAdvancedItemCollector extends TileEntityBase implements ITickable, IInventoryChangedListener
{
	int currentTickRate = 20;
	int counter = 0;

	int rangeX = 5;
	int rangeY = 5;
	int rangeZ = 5;

	InventoryBasic filterInventory;

	ItemItemFilter.ItemFilterRepresentation filterRepres;

	public TileEntityAdvancedItemCollector()
	{
		filterInventory = new InventoryBasic("tile.advancedItemCollector", false, 1)
		{
			@Override
			public int getInventoryStackLimit()
			{
				return 1;
			}
		};
		filterInventory.addInventoryChangeListener(this);
	}

	public IInventory getInventory()
	{
		return filterInventory;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("rangeX", rangeX);
		compound.setInteger("rangeY", rangeY);
		compound.setInteger("rangeZ", rangeZ);

		NBTTagCompound inventoryCompound = new NBTTagCompound();
		InventoryUtil.writeInventoryToCompound(inventoryCompound, filterInventory);
		compound.setTag("inventory", inventoryCompound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		rangeX = compound.getInteger("rangeX");
		rangeY = compound.getInteger("rangeY");
		rangeZ = compound.getInteger("rangeZ");

		NBTTagCompound inventoryCompound = compound.getCompoundTag("inventory");

		if (inventoryCompound != null)
		{
			InventoryUtil.readInventoryFromCompound(inventoryCompound, filterInventory);
		}
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote)
		{
			counter++;
			if (counter >= currentTickRate)
			{
				counter = 0;

				List<EntityItem> entityItemList = this.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(this.pos.add(-rangeX, -rangeY, -rangeZ), this.pos.add(rangeX + 1, rangeY + 1, rangeZ + 1)), EntitySelectors.IS_ALIVE);
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
							if (!ei.isDead && (filterRepres == null || filterRepres.matchesItemStack(ei.getItem())))
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

	public int getRangeX()
	{
		return rangeX;
	}

	public void setRangeX(int rangeX)
	{
		this.rangeX = rangeX;

		if (this.rangeX < 0)
		{
			this.rangeX = 0;
		}
		else if (this.rangeX > Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE)
		{
			this.rangeX = Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}

	public int getRangeY()
	{
		return rangeY;
	}

	public void setRangeY(int rangeY)
	{
		this.rangeY = rangeY;

		if (this.rangeY < 0)
		{
			this.rangeY = 0;
		}
		else if (this.rangeY > Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE)
		{
			this.rangeY = Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}

	public int getRangeZ()
	{
		return rangeZ;
	}

	public void setRangeZ(int rangeZ)
	{
		this.rangeZ = rangeZ;

		if (this.rangeZ < 0)
		{
			this.rangeZ = 0;
		}
		else if (this.rangeZ > Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE)
		{
			this.rangeZ = Numbers.ADVANCED_ITEM_COLLECTOR_MAX_RANGE;
		}

		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public void onInventoryChanged(IInventory p_76316_1_)
	{
		ItemStack filterStack = p_76316_1_.getStackInSlot(0);

		if (filterStack.isEmpty())
		{
			this.filterRepres = null;
		}
		else
		{
			this.filterRepres = ItemItemFilter.ItemFilterRepresentation.readFromItemStack(filterStack);
		}
	}
}
