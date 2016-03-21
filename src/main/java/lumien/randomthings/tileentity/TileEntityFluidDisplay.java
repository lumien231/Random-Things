package lumien.randomthings.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityFluidDisplay extends TileEntityBase
{
	String fluidName;
	boolean flowing;

	public TileEntityFluidDisplay()
	{
		this.fluidName = "";
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setString("fluidName", fluidName);
		compound.setBoolean("flowing", flowing);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.fluidName = compound.getString("fluidName");
		this.flowing = compound.getBoolean("flowing");
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	public void setFluidName(String fluidName)
	{
		this.fluidName = fluidName;
	}

	public void toggleFlowing()
	{
		flowing = !flowing;
		this.markDirty();
		syncTE();
	}

	public String getFluid()
	{
		return fluidName;
	}

	public boolean flowing()
	{
		return flowing;
	}
}
