package lumien.randomthings.container;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ContainerTE<E extends TileEntity> extends Container
{
	E te;

	List<Field> fieldList;
	List<Object> valueList;

	public ContainerTE(EntityPlayer player, World world, int x, int y, int z)
	{
		this.te = (E) world.getTileEntity(new BlockPos(x, y, z));

		this.fieldList = new ArrayList<Field>();
		this.valueList = new ArrayList<Object>();

		ArrayList<Field> classFieldList = SyncHandler.fieldMap.get(te.getClass());

		if (classFieldList != null)
		{
			for (Field f : classFieldList)
			{
				fieldList.add(f);
				valueList.add(null);
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return te != null && playerIn.worldObj.getTileEntity(te.getPos()) == te && playerIn.getDistanceSq(this.te.getPos().getX() + 0.5D, this.te.getPos().getY() + 0.5D, this.te.getPos().getZ() + 0.5D) <= 64.0D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int identifier, int value)
	{
		Field f = fieldList.get(identifier);

		Class type = f.getType();
		if (type == int.class)
		{
			try
			{
				f.set(te, value);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		else if (type == boolean.class)
		{
			try
			{
				f.set(te, value == 1 ? true : false);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		else if (Enum.class.isAssignableFrom(type))
		{
			try
			{
				f.set(te, type.getEnumConstants()[value]);
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icrafting = this.listeners.get(i);

			for (int ident = 0; ident < valueList.size(); ident++)
			{
				Field f = fieldList.get(ident);
				Object oldValue = valueList.get(ident);
				try
				{
					Object newValue = f.get(te);

					if (!newValue.equals(oldValue))
					{
						valueList.set(ident, newValue);

						int intValue = 0;

						Class type = f.getType();
						if (type == int.class)
						{
							intValue = f.getInt(te);
						}
						else if (type == boolean.class)
						{
							intValue = f.getBoolean(te) ? 1 : 0;
						}
						else if (Enum.class.isAssignableFrom(type))
						{
							intValue = ((Enum) newValue).ordinal();
						}

						icrafting.sendProgressBarUpdate(this, ident, intValue);
					}
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public abstract void signal(int signal);
}
