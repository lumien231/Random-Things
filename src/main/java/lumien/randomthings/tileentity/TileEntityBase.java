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
		readDataFromNBT(packet.getNbtCompound());
	}

	@Override
	public final Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeDataToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.pos, 1, nbtTag);
	}
	
	public abstract void writeDataToNBT(NBTTagCompound compound);
	
	public abstract void readDataFromNBT(NBTTagCompound compound);
}
