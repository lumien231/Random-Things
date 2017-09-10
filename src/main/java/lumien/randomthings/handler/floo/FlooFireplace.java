package lumien.randomthings.handler.floo;

import java.util.UUID;

import lumien.randomthings.util.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class FlooFireplace
{
	UUID creatorPlayerUUID;

	UUID masterUUID;

	String name;
	BlockPos lastKnownPosition;

	public FlooFireplace()
	{
	}

	public FlooFireplace(UUID creatorPlayerUUID, UUID masterUUID, String name, BlockPos position)
	{
		this.creatorPlayerUUID = creatorPlayerUUID;
		this.masterUUID = masterUUID;
		this.name = name;
		this.lastKnownPosition = position;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		if (creatorPlayerUUID != null)
		{
			compound.setTag("creatorPlayerUUID", net.minecraft.nbt.NBTUtil.createUUIDTag(creatorPlayerUUID));
		}

		compound.setTag("masterUUID", net.minecraft.nbt.NBTUtil.createUUIDTag(masterUUID));

		if (name != null)
		{
			compound.setString("name", name);
		}

		NBTUtil.writeBlockPosToNBT(compound, "position", lastKnownPosition);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("creatorPlayerUUID"))
		{
			this.creatorPlayerUUID = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("creatorPlayerUUID"));
		}

		this.masterUUID = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("masterUUID"));

		if (compound.hasKey("name"))
		{
			this.name = compound.getString("name");
		}

		this.lastKnownPosition = NBTUtil.readBlockPosFromNBT(compound, "position");
	}

	public String getName()
	{
		return name;
	}

	public BlockPos getLastKnownPosition()
	{
		return lastKnownPosition;
	}

	public UUID getMasterUUID()
	{
		return masterUUID;
	}

	public void setPos(BlockPos pos)
	{
		this.lastKnownPosition = pos;
	}

	public UUID getCreatorUUID()
	{
		return creatorPlayerUUID;
	}
}
