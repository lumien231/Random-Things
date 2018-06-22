package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.handler.spectrecoils.SpectreCoilHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntitySpectreEnergyInjector extends TileEntityBase
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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return super.hasCapability(capability, facing) || capability == CapabilityEnergy.ENERGY && owner != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability == CapabilityEnergy.ENERGY)
		{
			return (T) SpectreCoilHandler.get(this.world).getStorage(this.owner);
		}

		return super.getCapability(capability, facing);
	}

	public void setOwner(UUID id)
	{
		this.owner = id;
	}
}
