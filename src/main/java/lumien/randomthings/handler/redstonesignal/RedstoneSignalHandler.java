package lumien.randomthings.handler.redstonesignal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RedstoneSignalHandler extends WorldSavedData
{
	public static final String ID = "RTRedstoneSignalHandler";

	List<RedstoneSignal> redstoneSignals;

	public RedstoneSignalHandler(String name)
	{
		super(ID);

		redstoneSignals = new ArrayList<RedstoneSignal>();
	}

	public RedstoneSignalHandler()
	{
		this(ID);
	}
	
	@Override
	public boolean isDirty()
	{
		return true;
	}

	public static RedstoneSignalHandler getHandler()
	{
		World overWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();

		RedstoneSignalHandler handler = (RedstoneSignalHandler) overWorld.getMapStorage().getOrLoadData(RedstoneSignalHandler.class, ID);

		if (handler == null)
		{
			handler = new RedstoneSignalHandler();
			
			overWorld.getMapStorage().setData(ID, handler);
		}
		
		return handler;
	}

	private void updatePosition(World worldObj, BlockPos pos)
	{
		IBlockState targetState = worldObj.getBlockState(pos);
		targetState.neighborChanged(worldObj, pos,  Blocks.REDSTONE_BLOCK);
		worldObj.notifyNeighborsOfStateChange(pos, Blocks.REDSTONE_BLOCK);
	}

	public synchronized boolean addSignal(World worldObj, BlockPos pos, int duration, int strength)
	{
		if (worldObj.isBlockLoaded(pos))
		{
			redstoneSignals.add(new RedstoneSignal(worldObj.provider.getDimension(), pos, duration, strength));

			updatePosition(worldObj, pos);
			return true;
		}
		else
		{
			return false;
		}
	}

	public synchronized void tick()
	{
		Iterator<RedstoneSignal> iterator = redstoneSignals.iterator();

		while (iterator.hasNext())
		{
			RedstoneSignal rs = iterator.next();
			World signalWorld = DimensionManager.getWorld(rs.getDimension());

			if (signalWorld != null && signalWorld.isBlockLoaded(rs.getPosition()))
			{
				if (rs.tick())
				{
					iterator.remove();

					updatePosition(signalWorld, rs.getPosition());
				}
			}
		}
	}

	public synchronized int getStrongPower(World worldObj, BlockPos pos,EnumFacing facing)
	{
		pos = pos.offset(facing.getOpposite());
		int dimension = worldObj.provider.getDimension();
		for (RedstoneSignal rs : redstoneSignals)
		{
			if (rs.getDimension() == dimension)
			{
				if (rs.getPosition().equals(pos))
				{
					return rs.getRedstoneStrength();
				}
			}
		}

		return 0;
	}

	@Override
	public synchronized void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList nbtSignalList = nbt.getTagList("redstoneSignals", 10);

		for (int i = 0; i < nbtSignalList.tagCount(); i++)
		{
			NBTTagCompound signalCompound = nbtSignalList.getCompoundTagAt(i);

			RedstoneSignal rs = new RedstoneSignal();
			rs.readFromNBT(signalCompound);

			this.redstoneSignals.add(rs);
		}
	}

	@Override
	public synchronized NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList nbtSignalList = new NBTTagList();

		for (RedstoneSignal rs : redstoneSignals)
		{
			NBTTagCompound signalCompound = new NBTTagCompound();

			rs.writeToNBT(signalCompound);

			nbtSignalList.appendTag(signalCompound);
		}

		nbt.setTag("redstoneSignals", nbtSignalList);
		
		return nbt;
	}

}
