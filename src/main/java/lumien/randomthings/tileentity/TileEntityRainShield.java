package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import lumien.randomthings.config.Numbers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRainShield extends TileEntityBase
{
	enum STATE
	{
		INACTIVE, ACTIVATING, ACTIVE
	}

	public static Set<TileEntityRainShield> shields = Collections.newSetFromMap(new WeakHashMap());

	public static ConcurrentHashMap<BlockPos, Boolean> rainCache = new ConcurrentHashMap<>();

	STATE state;

	int currentRange;

	boolean active = true;

	public TileEntityRainShield()
	{
		synchronized (shields)
		{
			shields.add(this);
		}

		state = STATE.INACTIVE;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setBoolean("active", active);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		if (!compound.hasKey("active"))
		{
			this.active = true;
		}
		else
		{
			this.active = compound.getBoolean("active");
		}
	}

	@Override
	public boolean syncAdditionalData()
	{
		return true;
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		this.invalidate();
	}

	public static boolean shouldRain(World world, BlockPos pos)
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
				if (rainShield.active && rainShield.world == world && !rainShield.isInvalid() && rainShield.getPos().add(0, -rainShield.getPos().getY(), 0).distanceSq(pos) < (Numbers.RAIN_SHIELD_RANGE) * (Numbers.RAIN_SHIELD_RANGE))
				{
					rainCache.put(pos, Boolean.FALSE);
					return false;
				}
			}
		}

		rainCache.put(pos, Boolean.TRUE);
		return true;
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.active = !(worldIn.isBlockIndirectlyGettingPowered(pos) > 0);
	}

	@Override
	public void neighborChanged(IBlockState state2, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		boolean desiredState = !(worldIn.isBlockIndirectlyGettingPowered(pos) > 0);

		if (desiredState != this.active)
		{
			this.active = desiredState;
			syncTE();
		}
	}
}
