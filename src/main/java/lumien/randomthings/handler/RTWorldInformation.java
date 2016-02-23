package lumien.randomthings.handler;

import lumien.randomthings.handler.spectre.SpectreHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class RTWorldInformation extends WorldSavedData
{
	public static final String ID = "RTWorldInfo";

	private boolean enderDragonDefeated = false;

	public RTWorldInformation(String name)
	{
		super(name);
	}

	public RTWorldInformation()
	{
		this(ID);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.enderDragonDefeated = nbt.getBoolean("enderDragonDefeated");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("enderDragonDefeated", enderDragonDefeated);
	}
	
	public boolean isDragonDefeated()
	{
		return enderDragonDefeated;
	}

	public void setEnderDragonDefeated(boolean defeated)
	{
		if (defeated != enderDragonDefeated)
		{
			enderDragonDefeated = defeated;
			this.markDirty();
		}
	}

	public static RTWorldInformation getInstance()
	{
		WorldServer world = MinecraftServer.getServer().worldServerForDimension(0);
		if (world != null)
		{
			WorldSavedData handler = world.getMapStorage().loadData(RTWorldInformation.class, ID);
			if (handler == null)
			{
				handler = new RTWorldInformation();
				world.getMapStorage().setData(ID, handler);
			}

			return (RTWorldInformation) handler;
		}

		return null;
	}
}
