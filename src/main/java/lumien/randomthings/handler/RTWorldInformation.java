package lumien.randomthings.handler;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("enderDragonDefeated", enderDragonDefeated);

		return nbt;
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
		WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		if (world != null)
		{
			WorldSavedData handler = world.getMapStorage().getOrLoadData(RTWorldInformation.class, ID);
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
