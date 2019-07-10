package lumien.randomthings.handler.spectrecoils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lumien.randomthings.asm.MCPNames;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpectreCoilHandler extends WorldSavedData
{
	static final String ID = "rtSpectreCoilHandler";

	static final int MAX_ENERGY = 1000000;

	Map<UUID, Integer> coilEntries;

	public SpectreCoilHandler()
	{
		this(ID);
	}

	public SpectreCoilHandler(String id)
	{
		super(id);

		coilEntries = new HashMap<UUID, Integer>();
	}

	public IEnergyStorage getStorage(UUID owner)
	{
		return new IEnergyStorage()
		{
			@Override
			public int receiveEnergy(int maxReceive, boolean simulate)
			{
				int currentEnergy = coilEntries.containsKey(owner) ? coilEntries.get(owner) : 0;

				int newEnergy = Math.min(MAX_ENERGY, currentEnergy + maxReceive);

				if (!simulate)
					coilEntries.put(owner, newEnergy);

				return newEnergy - currentEnergy;
			}

			@Override
			public int getMaxEnergyStored()
			{
				return MAX_ENERGY;
			}

			@Override
			public int getEnergyStored()
			{
				return coilEntries.containsKey(owner) ? coilEntries.get(owner) : 0;
			}

			@Override
			public int extractEnergy(int maxExtract, boolean simulate)
			{
				return 0;
			}

			@Override
			public boolean canReceive()
			{
				return true;
			}

			@Override
			public boolean canExtract()
			{
				return false;
			}
		};
	}

	public static SpectreCoilHandler get(World worldObj)
	{
		SpectreCoilHandler instance = (SpectreCoilHandler) worldObj.getMapStorage().getOrLoadData(SpectreCoilHandler.class, ID);
		if (instance == null)
		{
			instance = new SpectreCoilHandler();
			worldObj.getMapStorage().setData(ID, instance);
		}

		return instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("coilEntries", 10);

		this.coilEntries.clear();

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);

			UUID uuid = UUID.fromString(compound.getString("uuid"));
			int energy = compound.getInteger("energy");

			this.coilEntries.put(uuid, energy);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();

		for (Entry<UUID, Integer> entry : coilEntries.entrySet())
		{
			NBTTagCompound entryCompound = new NBTTagCompound();

			entryCompound.setString("uuid", entry.getKey().toString());
			entryCompound.setInteger("energy", entry.getValue());

			list.appendTag(entryCompound);
		}

		compound.setTag("coilEntries", list);

		return compound;
	}

	@Override
	public boolean isDirty()
	{
		return true;
	}

	public IEnergyStorage getStorageCoil(UUID owner)
	{
		return new IEnergyStorage()
		{
			@Override
			public int receiveEnergy(int maxReceive, boolean simulate)
			{
				int currentEnergy = coilEntries.containsKey(owner) ? coilEntries.get(owner) : 0;

				int newEnergy = Math.min(MAX_ENERGY, currentEnergy + maxReceive);

				if (!simulate)
					coilEntries.put(owner, newEnergy);

				return newEnergy - currentEnergy;
			}

			@Override
			public int getMaxEnergyStored()
			{
				return MAX_ENERGY;
			}

			@Override
			public int getEnergyStored()
			{
				return coilEntries.containsKey(owner) ? coilEntries.get(owner) : 0;
			}

			@Override
			public int extractEnergy(int maxExtract, boolean simulate)
			{
				int currentEnergy = coilEntries.containsKey(owner) ? coilEntries.get(owner) : 0;

				int newEnergy = Math.max(0, currentEnergy - maxExtract);

				if (!simulate)
					coilEntries.put(owner, newEnergy);

				return currentEnergy - newEnergy;
			}

			@Override
			public boolean canReceive()
			{
				return true;
			}

			@Override
			public boolean canExtract()
			{
				return true;
			}
		};
	}
}
