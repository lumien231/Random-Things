package lumien.randomthings.handler.redstonesignal;

import lumien.randomthings.util.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class RedstoneSignal
{
	private int dimension;
	private BlockPos position;

	private int duration;
	private int age;

	private int redstoneStrength;

	public RedstoneSignal()
	{

	}

	public RedstoneSignal(int dimension, BlockPos position, int duration, int redstoneStrength)
	{
		this.dimension = dimension;
		this.position = position;
		this.duration = duration;
		this.redstoneStrength = redstoneStrength;

		this.age = 0;
	}

	public boolean tick()
	{
		this.age++;

		if (this.age >= this.duration)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("dimension", dimension);
		NBTUtil.writeBlockPosToNBT(compound, "position", position);
		compound.setInteger("redstoneStrength", redstoneStrength);
		compound.setInteger("duration", duration);
		compound.setInteger("age", age);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.dimension = compound.getInteger("dimension");
		this.position = NBTUtil.readBlockPosFromNBT(compound, "position");
		this.redstoneStrength = compound.getInteger("redstoneStrength");
		this.duration = compound.getInteger("duration");
		this.age = compound.getInteger("age");
	}

	public int getDimension()
	{
		return dimension;
	}

	public BlockPos getPosition()
	{
		return position;
	}

	public int getRedstoneStrength()
	{
		return redstoneStrength;
	}
}
