package lumien.randomthings.tileentity;

import lumien.randomthings.RandomThings;
import lumien.randomthings.config.Features;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class TileEntityEnderAnchor extends TileEntityBase implements ITickable
{
	Ticket chunkTicket;

	boolean firstTick = true;

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			if (firstTick)
			{
				firstTick = false;

				if (chunkTicket == null && Features.ENDER_ANCHOR_CHUNKLOADING)
				{
					chunkTicket = ForgeChunkManager.requestTicket(RandomThings.instance, this.worldObj, Type.NORMAL);
					if (chunkTicket != null)
					{
						ForgeChunkManager.forceChunk(chunkTicket, this.worldObj.getChunkFromBlockCoords(this.pos).getChunkCoordIntPair());
						chunkTicket.getModData().setInteger("posX", this.pos.getX());
						chunkTicket.getModData().setInteger("posY", this.pos.getY());
						chunkTicket.getModData().setInteger("posZ", this.pos.getZ());
					}
				}
			}
		}
	}

	public void setTicket(Ticket t)
	{
		this.chunkTicket = t;
	}

	public void discardTicket()
	{
		if (this.chunkTicket != null)
		{
			ForgeChunkManager.releaseTicket(this.chunkTicket);
		}
	}
}
