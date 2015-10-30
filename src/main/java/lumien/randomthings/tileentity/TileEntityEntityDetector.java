package lumien.randomthings.tileentity;

import java.util.List;

import com.google.common.base.Predicate;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.TileEntityEntityDetector.FILTER;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class TileEntityEntityDetector extends TileEntityBase implements IUpdatePlayerListBox
{
	boolean powered;

	int rangeX = 5;
	int rangeY = 5;
	int rangeZ = 5;
	
	boolean invert;

	static final int MAX_RANGE = 10;

	FILTER filter = FILTER.ALL;

	public enum FILTER
	{
		ALL("all", Entity.class), LIVING("living", EntityLivingBase.class), ANIMAL("animal", IAnimals.class), MONSTER("monster", IMob.class), PLAYER("player", EntityPlayer.class), ITEMS("item",EntityItem.class);

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

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			boolean newPowered = checkSupposedPowereredState();

			if (newPowered != powered)
			{
				powered = newPowered;
				this.worldObj.markBlockForUpdate(pos);
				this.worldObj.notifyNeighborsOfStateChange(pos, ModBlocks.entityDetector);
			}
		}
	}

	public void cycleFilter()
	{
		int index = filter.ordinal();

		index++;

		if (index < filter.values().length)
		{
			filter = FILTER.values()[index];
		}
		else
		{
			filter = FILTER.values()[0];
		}

		this.worldObj.markBlockForUpdate(this.pos);
	}

	private boolean checkSupposedPowereredState()
	{
		if (filter.filterClass != null)
		{
			List<Entity> entityList = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1)).expand(rangeX, rangeY, rangeZ),new Predicate<Entity>()
			{

				@Override
				public boolean apply(Entity input)
				{
					return !invert == filter.filterClass.isAssignableFrom(input.getClass());
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

		this.worldObj.markBlockForUpdate(pos);
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

		this.worldObj.markBlockForUpdate(pos);
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

		this.worldObj.markBlockForUpdate(pos);
	}
	
	public void toggleInvert()
	{
		invert = !invert;
		
		this.worldObj.markBlockForUpdate(pos);
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
