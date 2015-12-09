package lumien.randomthings.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityBase extends TileEntity
{
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		writeDataToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		readDataFromNBT(compound);
	}

	@Override
	public final void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		if (writeNBTToDescriptionPacket())
		{
			readDataFromNBT(packet.getNbtCompound());
		}

		if (renderAfterData())
		{
			this.worldObj.markBlockForUpdate(this.pos);
		}
	}

	@Override
	public final Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		if (writeNBTToDescriptionPacket())
		{
			this.writeDataToNBT(nbtTag);
		}
		return new S35PacketUpdateTileEntity(this.pos, 1, nbtTag);
	}
	
	protected void syncTE()
	{
		this.worldObj.markBlockForUpdate(this.pos);
	}

	public abstract void writeDataToNBT(NBTTagCompound compound);

	public abstract void readDataFromNBT(NBTTagCompound compound);

	public boolean renderAfterData()
	{
		return false;
	}

	public boolean writeNBTToDescriptionPacket()
	{
		return true;
	}
}
