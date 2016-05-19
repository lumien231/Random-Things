package lumien.randomthings.tileentity;

import java.util.List;

import com.google.common.base.Predicate;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemEntityFilter;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityEntityDetector extends TileEntityBase implements ITickable
{
	boolean powered;

	int rangeX = 5;
	int rangeY = 5;
	int rangeZ = 5;

	boolean invert;

	static final int MAX_RANGE = 10;

	FILTER filter = FILTER.ALL;

	InventoryBasic filterInventory;

	public TileEntityEntityDetector()
	{
		filterInventory = new InventoryBasic("tile.entityDetector", false, 1);
	}

	public enum FILTER
	{
		ALL("all", Entity.class), LIVING("living", EntityLivingBase.class), ANIMAL("animal", IAnimals.class), MONSTER("monster", IMob.class), PLAYER("player", EntityPlayer.class), ITEMS("item", EntityItem.class), CUSTOM("custom", null);

		String languageKey;
		Class filterClass;

		private FILTER(String languageKey, Class filterClass)
		{
			this.languageKey = "gui.entityDetector.filter." + languageKey;
			this.filterClass = filterClass;
		}

		public String getLanguageKey()
		{
			return languageKey;
		}
	}

	public IInventory getInventory()
	{
		return filterInventory;
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			boolean newPowered = checkSupposedPowereredState();

			if (newPowered != powered)
			{
				powered = newPowered;
				this.syncTE();
				this.worldObj.notifyNeighborsOfStateChange(pos, ModBlocks.entityDetector);
			}
		}
	}

	public void cycleFilter()
	{
		int index = filter.ordinal();

		index++;

		if (index < FILTER.values().length)
		{
			filter = FILTER.values()[index];
		}
		else
		{
			filter = FILTER.values()[0];
		}

		syncTE();
	}

	private boolean checkSupposedPowereredState()
	{
		Class filterClass = filter.filterClass;

		if (filterClass == null)
		{
			ItemStack filter;
			if ((filter = filterInventory.getStackInSlot(0)) != null)
			{
				filterClass = ItemEntityFilter.getEntityClass(filter);
			}
		}

		if (filterClass != null)
		{
			final Class finalFilterClass = filterClass;
			List<Entity> entityList = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1)).expand(rangeX, rangeY, rangeZ), new Predicate<Entity>()
			{
				@Override
				public boolean apply(Entity input)
				{
					return !invert == finalFilterClass.isAssignableFrom(input.getClass());
				}
			});
			return entityList != null && entityList.size() > 0;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("powered", powered);

		compound.setInteger("rangeX", rangeX);
		compound.setInteger("rangeY", rangeY);
		compound.setInteger("rangeZ", rangeZ);

		compound.setInteger("filter", filter.ordinal());
		compound.setBoolean("invert", invert);

		NBTTagCompound inventoryCompound = new NBTTagCompound();
		InventoryUtil.writeInventoryToCompound(inventoryCompound, filterInventory);
		compound.setTag("inventory", inventoryCompound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		powered = compound.getBoolean("powered");

		rangeX = compound.getInteger("rangeX");
		rangeY = compound.getInteger("rangeY");
		rangeZ = compound.getInteger("rangeZ");

		filter = FILTER.values()[compound.getInteger("filter")];
		invert = compound.getBoolean("invert");

		NBTTagCompound inventoryCompound = compound.getCompoundTag("inventory");

		if (inventoryCompound != null)
		{
			InventoryUtil.readInventoryFromCompound(inventoryCompound, filterInventory);
		}
	}

	public boolean isPowered()
	{
		return powered;
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
		else if (this.rangeX > MAX_RANGE)
		{
			this.rangeX = MAX_RANGE;
		}

		this.syncTE();
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
		else if (this.rangeY > MAX_RANGE)
		{
			this.rangeY = MAX_RANGE;
		}

		this.syncTE();
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
		else if (this.rangeZ > MAX_RANGE)
		{
			this.rangeZ = MAX_RANGE;
		}

		this.syncTE();
	}

	public void toggleInvert()
	{
		invert = !invert;

		this.syncTE();
	}

	public boolean invert()
	{
		return invert;
	}

	public FILTER getFilter()
	{
		return filter;
	}
}
