package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.block.BlockSpectreCoil;
import lumien.randomthings.block.BlockSpectreCoil.CoilType;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.handler.spectrecoils.SpectreCoilHandler;
import net.minecraft.block.state.IBlockState;
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

	CoilType coilType = CoilType.NORMAL;

	public TileEntitySpectreCoil(CoilType coilType)
	{
		this.coilType = coilType;
	}

	public TileEntitySpectreCoil()
	{

	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("coilType", coilType.ordinal());

		if (owner != null)
			compound.setString("owner", owner.toString());
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.coilType = CoilType.values()[compound.getInteger("coilType")];

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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		IBlockState state = this.world.getBlockState(this.pos);

		if (state.getBlock() instanceof BlockSpectreCoil) // Maybe i'm already unloaded
		{
			EnumFacing myFacing = state.getValue(BlockSpectreCoil.FACING).getOpposite();

			if (myFacing == facing && capability == CapabilityEnergy.ENERGY)
			{
				return true;
			}
		}

		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		IBlockState state = this.world.getBlockState(this.pos);
		if (state.getBlock() instanceof BlockSpectreCoil) // Maybe i'm already unloaded
		{
			EnumFacing myFacing = state.getValue(BlockSpectreCoil.FACING).getOpposite();

			if (myFacing == facing && capability == CapabilityEnergy.ENERGY)
			{
				return (T) new IEnergyStorage()
				{

					@Override
					public int receiveEnergy(int maxReceive, boolean simulate)
					{
						return 0;
					}

					@Override
					public int getMaxEnergyStored()
					{
						return 0;
					}

					@Override
					public int getEnergyStored()
					{
						return 0;
					}

					@Override
					public int extractEnergy(int maxExtract, boolean simulate)
					{
						return 0;
					}

					@Override
					public boolean canReceive()
					{
						return false;
					}

					@Override
					public boolean canExtract()
					{
						return false;
					}
				};
			}
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote && this.owner != null)
		{
			EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockSpectreCoil.FACING).getOpposite();

			TileEntity targetTe = this.world.getTileEntity(pos.offset(facing));

			if (targetTe != null)
			{
				IEnergyStorage targetStorage = null;
				if (targetTe.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
				{
					targetStorage = targetTe.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				}
				else if (targetTe.hasCapability(CapabilityEnergy.ENERGY, null))
				{
					targetStorage = targetTe.getCapability(CapabilityEnergy.ENERGY, null);
				}

				if (targetStorage != null && targetStorage.canReceive())
				{
					if (this.coilType == CoilType.NUMBER || this.coilType == CoilType.GENESIS)
					{
						int amount = coilType == CoilType.NUMBER ? Numbers.NUMBERED_SPECTRECOIL_ENERGY : 10000000;

						targetStorage.receiveEnergy(amount, false);
					}
					else
					{
						IEnergyStorage coilStorage = SpectreCoilHandler.get(this.world).getStorageCoil(this.owner);

						int rate = 1;

						if (coilType == CoilType.NORMAL)
						{
							rate = 1024;
						}
						else if (coilType == CoilType.REDSTONE)
						{
							rate = 4096;
						}
						else if (coilType == CoilType.ENDER)
						{
							rate = 20480;
						}

						int available = coilStorage.extractEnergy(rate, true);

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
}
