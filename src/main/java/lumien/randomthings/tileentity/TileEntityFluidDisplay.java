package lumien.randomthings.tileentity;

import lumien.randomthings.util.RandomUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFluidDisplay extends TileEntityBase
{
	FluidStack fluidStack;
	boolean flowing;
	Rotation rotation;

	public TileEntityFluidDisplay()
	{
		this.rotation = Rotation.NONE;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagCompound fluidCompound = new NBTTagCompound();

		if (fluidStack != null)
		{
			fluidStack.writeToNBT(fluidCompound);
		}

		compound.setTag("fluidStack", fluidCompound);
		compound.setBoolean("flowing", flowing);
		compound.setInteger("rotation", rotation.ordinal());
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (compound.hasKey("fluidStack"))
		{
			this.fluidStack = FluidStack.loadFluidStackFromNBT(compound.getCompoundTag("fluidStack"));
		}
		else
		{
			this.fluidStack = new FluidStack(FluidRegistry.WATER, 1000);
		}
		this.flowing = compound.getBoolean("flowing");
		this.rotation = Rotation.values()[compound.getInteger("rotation")];
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	public void toggleFlowing()
	{
		flowing = !flowing;
		this.markDirty();
		syncTE();
	}

	public boolean flowing()
	{
		return flowing;
	}

	public FluidStack getFluidStack()
	{
		return fluidStack;
	}

	public void setFluidStack(FluidStack fluidStack)
	{
		this.fluidStack = fluidStack;
	}

	public Rotation getRotation()
	{
		return rotation;
	}

	public void cycleRotation()
	{
		this.rotation = RandomUtil.rotateEnum(rotation);
		this.markDirty();
		syncTE();
	}
}
