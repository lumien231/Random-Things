package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.block.BlockSpectreCoil;
import lumien.randomthings.handler.spectrecoils.SpectreCoilHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntitySpectreCoil extends TileEntityBase implements ITickable
{
	UUID owner;

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (owner != null)
			compound.setString("owner", owner.toString());
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (compound.hasKey("owner"))
			this.owner = UUID.fromString(compound.getString("owner"));
	}

	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}

	public void setOwner(UUID id)
	{
		this.owner = id;
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote && this.owner != null)
		{
			EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockSpectreCoil.FACING).getOpposite();

			TileEntity targetTe = this.world.getTileEntity(pos.offset(facing));

			if (targetTe != null && targetTe.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
			{
				IEnergyStorage targetStorage = targetTe.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());

				if (targetStorage.canReceive())
				{
					IEnergyStorage coilStorage = SpectreCoilHandler.get(this.world).getStorageCoil(this.owner);

					int available = coilStorage.extractEnergy(1024, true);

					int remaining = available - targetStorage.receiveEnergy(available, false);
					
					if (remaining != available)
					{
						coilStorage.extractEnergy(available - remaining, false);
					}
				}
			}
		}
	}
}
