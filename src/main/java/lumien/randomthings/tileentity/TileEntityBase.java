package lumien.randomthings.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		if (writeNBTToDescriptionPacket())
		{
			readDataFromNBT(packet.getNbtCompound());
		}

		if (renderAfterData())
		{
			IBlockState state = this.worldObj.getBlockState(this.pos);
			this.worldObj.notifyBlockUpdate(pos, state, state, 3);
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
		return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
	}
	
	public void syncTE()
	{
		IBlockState state = this.worldObj.getBlockState(this.pos);
		this.worldObj.notifyBlockUpdate(pos, state, state, 3);
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
