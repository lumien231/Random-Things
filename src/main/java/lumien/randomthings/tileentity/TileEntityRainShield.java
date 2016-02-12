package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import lumien.randomthings.config.Numbers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

public class TileEntityRainShield extends TileEntityBase
{
	enum STATE
	{
		INACTIVE, ACTIVATING, ACTIVE
	}

	public static Set<TileEntityRainShield> shields = Collections.newSetFromMap(new WeakHashMap());

	public static ConcurrentHashMap<BlockPos, Boolean> rainCache = new ConcurrentHashMap<BlockPos, Boolean>();

	STATE state;

	int currentRange;

	public TileEntityRainShield()
	{
		synchronized (shields)
		{
			shields.add(this);
		}

		state = STATE.INACTIVE;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{

	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{

	}
	
	@Override
	public boolean writeNBTToDescriptionPacket()
	{
		return false;
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	public void broken()
	{
		this.invalidate();
	}

	public static boolean shouldRain(World worldObj, BlockPos pos)
	{
		Boolean cachedValue;
		if ((cachedValue = rainCache.get(pos)) != null)
		{
			return cachedValue;
		}

		synchronized (shields)
		{
			for (TileEntityRainShield rainShield : shields)
			{
				if (rainShield.worldObj == worldObj && !rainShield.isInvalid() && rainShield.getPos().add(0, -rainShield.getPos().getY(), 0).distanceSq(pos) < (Numbers.RAIN_SHIELD_RANGE) * (Numbers.RAIN_SHIELD_RANGE))
				{
					rainCache.put(pos, Boolean.FALSE);
					return false;
				}
			}
		}

		rainCache.put(pos, Boolean.TRUE);
		return true;
	}
}
