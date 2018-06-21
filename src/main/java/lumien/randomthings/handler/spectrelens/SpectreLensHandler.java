package lumien.randomthings.handler.spectrelens;

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
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpectreLensHandler extends WorldSavedData
{
	static final String ID = "rtSpectreLensHandler";

	Map<UUID, LensEntry> lensEntries;

	public SpectreLensHandler()
	{
		this(ID);
	}

	public SpectreLensHandler(String id)
	{
		super(id);

		lensEntries = new HashMap<UUID, LensEntry>();
	}

	public static SpectreLensHandler get(World worldObj)
	{
		SpectreLensHandler instance = (SpectreLensHandler) worldObj.getPerWorldStorage().getOrLoadData(SpectreLensHandler.class, ID);
		if (instance == null)
		{
			instance = new SpectreLensHandler();
			worldObj.getPerWorldStorage().setData(ID, instance);
		}

		return instance;
	}

	public void removeLens(UUID uuid)
	{
		lensEntries.remove(uuid);
	}

	public void addLens(UUID uuid, int levels, int primary, int secondary)
	{
		LensEntry entry;

		if (lensEntries.containsKey(uuid))
		{
			entry = lensEntries.get(uuid);
			entry.setPrimary(primary);
			entry.setSecondary(secondary);
		}
		else
		{
			entry = new LensEntry(levels, primary, secondary);
			lensEntries.put(uuid, entry);
		}
	}

	public void tick(World worldObj)
	{
		if (worldObj.getTotalWorldTime() % 60 == 0)
		{
			for (UUID uuid : lensEntries.keySet())
			{
				EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);

				if (player != null && player.world == worldObj)
				{
					LensEntry entry = lensEntries.get(uuid);
					
					int amplifier = entry.primary == entry.secondary ? 1 : 0;

					if (entry.primary != -1)
					{
						Potion potion = Potion.REGISTRY.getObjectById(entry.primary);

						if (potion != null)
						{
							player.addPotionEffect(new PotionEffect(potion, 20 * 10, amplifier, true, true));
						}
					}

					if (entry.secondary != -1)
					{
						Potion potion = Potion.REGISTRY.getObjectById(entry.secondary);

						if (potion != null)
						{
							player.addPotionEffect(new PotionEffect(potion, 20 * 10, amplifier, true, true));
						}
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("lensEntries", 10);

		this.lensEntries.clear();
		
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			
			UUID uuid = UUID.fromString(compound.getString("uuid"));
			
			LensEntry entry = new LensEntry();
			entry.readFromNBT(compound);
			
			this.lensEntries.put(uuid, entry);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();

		for (Entry<UUID, LensEntry> entry : lensEntries.entrySet())
		{
			NBTTagCompound entryCompound = new NBTTagCompound();

			entry.getValue().writeToNBT(entryCompound);
			entryCompound.setString("uuid", entry.getKey().toString());

			list.appendTag(entryCompound);
		}

		compound.setTag("lensEntries", list);

		return compound;
	}

	@Override
	public boolean isDirty()
	{
		return true;
	}

	static class LensEntry
	{
		int levels;

		int primary;
		int secondary;
		
		public LensEntry()
		{
			
		}

		public LensEntry(int levels, int primary, int secondary)
		{
			super();
			this.levels = levels;
			this.primary = primary;
			this.secondary = secondary;
		}
		
		public void readFromNBT(NBTTagCompound compound)
		{
			this.levels = compound.getInteger("levels");
			this.primary = compound.getInteger("primary");
			this.secondary = compound.getInteger("secondary");
		}

		public void writeToNBT(NBTTagCompound compound)
		{
			compound.setInteger("levels", levels);
			compound.setInteger("primary", primary);
			compound.setInteger("secondary", secondary);
		}

		public int getPrimary()
		{
			return primary;
		}

		public void setPrimary(int primary)
		{
			this.primary = primary;
		}

		public int getSecondary()
		{
			return secondary;
		}

		public void setSecondary(int secondary)
		{
			this.secondary = secondary;
		}

		public int getLevels()
		{
			return levels;
		}

		public void setLevels(int levels)
		{
			this.levels = levels;
		}
	}
}
