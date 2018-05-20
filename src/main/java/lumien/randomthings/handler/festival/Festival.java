package lumien.randomthings.handler.festival;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class Festival implements INBTSerializable<NBTTagCompound>
{

	enum STATE
	{
		SCHEDULED, ACTIVE;
	}

	int dimensionID;
	List<BlockPos> doorPositions;

	STATE state;

	public Festival()
	{
		doorPositions = new ArrayList<BlockPos>();
		state = STATE.SCHEDULED;
	}

	public void addDoorPos(BlockPos pos)
	{
		doorPositions.add(pos);
	}

	public List<BlockPos> getDoorPositions()
	{
		return doorPositions;
	}

	public STATE getState()
	{
		return state;
	}

	public void setActive()
	{
		this.state = STATE.ACTIVE;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList nbtList = new NBTTagList();

		for (BlockPos p : doorPositions)
		{
			NBTTagCompound posCompound = new NBTTagCompound();

			posCompound.setInteger("posX", p.getX());
			posCompound.setInteger("posY", p.getY());
			posCompound.setInteger("posZ", p.getZ());

			nbtList.appendTag(posCompound);
		}

		compound.setTag("doorPositions", nbtList);
		compound.setInteger("state", state.ordinal());

		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		NBTTagList tagList = nbt.getTagList("doorPositions", 10);

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound comp = tagList.getCompoundTagAt(i);

			BlockPos pos = new BlockPos(comp.getInteger("posX"), comp.getInteger("posY"), comp.getInteger("posZ"));

			this.doorPositions.add(pos);
		}

		this.state = STATE.values()[nbt.getInteger("state")];
	}
}
