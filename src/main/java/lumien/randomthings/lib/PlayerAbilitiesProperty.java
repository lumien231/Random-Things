package lumien.randomthings.lib;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerAbilitiesProperty implements IExtendedEntityProperties
{
	public static final String KEY = "RTAbilities";
	
	boolean immortal;
	
	public PlayerAbilitiesProperty()
	{
		this.immortal = false;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		compound.setBoolean("immortal", immortal);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		immortal = compound.getBoolean("immortal");
	}

	@Override
	public void init(Entity entity, World world)
	{
		
	}

	public boolean isImmortal()
	{
		return immortal;
	}

	public void setImmortal(boolean immortal)
	{
		this.immortal = immortal;
	}

}
