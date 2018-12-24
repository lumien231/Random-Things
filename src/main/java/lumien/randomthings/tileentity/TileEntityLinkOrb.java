package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLinkOrb extends TileEntityBase
{
	public static Set<TileEntityLinkOrb> orbs = Collections.newSetFromMap(new WeakHashMap());

	UUID owner;

	public TileEntityLinkOrb()
	{
		orbs.add(this);
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		if (!sync && owner != null)
		{
			compound.setString("owner", owner.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (!sync && compound.hasKey("owner"))
		{
			this.owner = UUID.fromString(compound.getString("owner"));
		}
	}

	public UUID getOwner()
	{
		return owner;
	}

	public void setOwner(UUID uuid)
	{
		this.owner = uuid;
	}
}
