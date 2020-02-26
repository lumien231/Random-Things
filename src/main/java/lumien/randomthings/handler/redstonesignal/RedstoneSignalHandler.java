package lumien.randomthings.handler.redstonesignal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RedstoneSignalHandler extends WorldSavedData
{
	public static final String ID = "RTRedstoneSignalHandler";

	List<RedstoneSignal> redstoneSignals;

	public RedstoneSignalHandler(String name)
	{
		super(ID);

		redstoneSignals = new ArrayList<>();
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
		targetState.neighborChanged(worldObj, pos, Blocks.REDSTONE_BLOCK, pos); // TODO DANGEROUS;
		worldObj.notifyNeighborsOfStateChange(pos, Blocks.REDSTONE_BLOCK, false);
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
	public  synchronized  boolean switchSignal(World worldObj, BlockPos pos, int strength){
		if(worldObj.isBlockLoaded(pos))
		{
			RedstoneSignal redstoneSignalNow = isPowered(worldObj,pos);
			if(redstoneSignalNow!=null)
			{
				redstoneSignalNow.setPowered(false);
			}
			else
			{
				redstoneSignals.add(new RedstoneSignal(worldObj.provider.getDimension(), pos, strength));
				updatePosition(worldObj,pos);
			}
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
				if (rs.tick()) //if redstone duration run out
				{
					iterator.remove(); // remove event from redstoneSinals
					updatePosition(signalWorld, rs.getPosition()); //update neighbor redstone signal
				}
			}
		}
	}

	public synchronized int getStrongPower(World worldObj, BlockPos pos, EnumFacing facing)
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

	public synchronized RedstoneSignal isPowered(World worldObj, BlockPos pos){
		int dimension = worldObj.provider.getDimension();
		for (RedstoneSignal rs : redstoneSignals)
		{
			if (rs.getDimension() == dimension)
			{
				if (rs.getPosition().equals(pos))
				{
					if(rs.isPowered()){
						return rs;
					}
				}
			}
		}
		return null;
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
	public synchronized NBTTagCompound writeToNBT(NBTTagCompound nbt)  //execute period 45 seconds
	{
		NBTTagList nbtSignalList = new NBTTagList();

		for (RedstoneSignal rs : redstoneSignals) // deal every write action came to being in 45 seconds
		{
			NBTTagCompound signalCompound = new NBTTagCompound();
			rs.writeToNBT(signalCompound);
			nbtSignalList.appendTag(signalCompound);
		}

		nbt.setTag("redstoneSignals", nbtSignalList);
		return nbt;
	}

}
