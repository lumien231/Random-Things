package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;

public class TileEntityRedstoneObserver extends TileEntityBase
{
	public static Set<TileEntityRedstoneObserver> loadedObservers = Collections.newSetFromMap(new WeakHashMap());
	BlockPos target;

	HashMap<EnumFacing, Integer> weakPower;
	HashMap<EnumFacing, Integer> strongPower;

	public TileEntityRedstoneObserver()
	{
		loadedObservers.add(this);

		weakPower = new HashMap<EnumFacing, Integer>();
		strongPower = new HashMap<EnumFacing, Integer>();
		updateRedstoneState();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		if (target != null)
		{
			compound.setInteger("targetX", target.getX());
			compound.setInteger("targetY", target.getY());
			compound.setInteger("targetZ", target.getZ());
		}

		NBTTagCompound weakPowerCompound = new NBTTagCompound();
		NBTTagCompound strongPowerCompound = new NBTTagCompound();

		for (EnumFacing facing : EnumFacing.values())
		{
			weakPowerCompound.setInteger(facing.ordinal() + "", weakPower.get(facing));
			strongPowerCompound.setInteger(facing.ordinal() + "", strongPower.get(facing));
		}

		compound.setTag("weakPowerCompound", weakPowerCompound);
		compound.setTag("strongPowerCompound", strongPowerCompound);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("targetX"))
		{
			target = new BlockPos(compound.getInteger("targetX"), compound.getInteger("targetY"), compound.getInteger("targetZ"));
		}

		NBTTagCompound weakPowerCompound = compound.getCompoundTag("weakPowerCompound");
		NBTTagCompound strongPowerCompound = compound.getCompoundTag("strongPowerCompound");

		for (EnumFacing facing : EnumFacing.values())
		{
			weakPower.put(facing, weakPowerCompound.getInteger(facing.ordinal() + ""));
			strongPower.put(facing, strongPowerCompound.getInteger(facing.ordinal() + ""));
		}
	}

	public void setTarget(BlockPos newTarget)
	{
		if (!newTarget.equals(target))
		{
			this.target = newTarget;

			updateRedstoneState();
		}
	}

	public static void notifyNeighbor(NeighborNotifyEvent event)
	{
		for (TileEntityRedstoneObserver observer : loadedObservers)
		{
			if (observer.getWorld() == event.getWorld() && !observer.isInvalid() && observer.getTarget() != null && observer.getTarget().equals(event.getPos()))
			{
				observer.updateRedstoneState();
			}
		}
	}

	static Set<TileEntityRedstoneObserver> observerSet = Collections.newSetFromMap(new WeakHashMap<TileEntityRedstoneObserver, Boolean>());
	private void updateRedstoneState()
	{
		if (observerSet.contains(this))
		{
			return;
		}
		observerSet.add(this);
		if (this.target == null)
		{
			for (EnumFacing facing : EnumFacing.values())
			{
				strongPower.put(facing, 0);
			}

			for (EnumFacing facing : EnumFacing.values())
			{
				weakPower.put(facing, 0);
			}
		}
		else
		{
			IBlockState targetState = this.worldObj.getBlockState(target);
			for (EnumFacing facing : EnumFacing.values())
			{
				strongPower.put(facing, targetState.getStrongPower(this.worldObj, target, facing));
				weakPower.put(facing, targetState.getWeakPower(this.worldObj, target, facing));
			}

			this.worldObj.notifyNeighborsOfStateChange(this.pos, ModBlocks.redstoneObserver);
		}
		
		observerSet.remove(this);
	}

	public void broken()
	{
		this.invalidate();
	}

	public BlockPos getTarget()
	{
		return this.target;
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return weakPower.get(side);
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return strongPower.get(side);
	}
}
