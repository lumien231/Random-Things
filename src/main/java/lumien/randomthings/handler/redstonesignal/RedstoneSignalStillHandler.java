package lumien.randomthings.handler.redstonesignal;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RedstoneSignalStillHandler extends WorldSavedData {
    public static final String ID = "RTRedstoneSignalStillHandler";

    List<RedstoneSignalStill> RedstoneSignalStills;

    public RedstoneSignalStillHandler(String name)
    {
        super(ID);

        RedstoneSignalStills = new ArrayList<>();
    }

    public RedstoneSignalStillHandler()
    {
        this(ID);
    }

    @Override
    public boolean isDirty()
    {
        return true;
    }

    public static RedstoneSignalStillHandler getHandler()
    {
        World overWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();

        RedstoneSignalStillHandler handler = (RedstoneSignalStillHandler) overWorld.getMapStorage().getOrLoadData(RedstoneSignalStillHandler.class, ID);

        if (handler == null)
        {
            handler = new RedstoneSignalStillHandler();

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

    public  synchronized  boolean switchSignal(World worldObj, BlockPos pos, int strength){
        if(worldObj.isBlockLoaded(pos)) //if block is uploaded 
        {
            RedstoneSignalStill RedstoneSignalStillNow = isPowered(worldObj,pos);  // get class RedstoneSignalStill according to pos in list if has
            if(RedstoneSignalStillNow!=null) //if has block in list
            {
                RedstoneSignalStillNow.setPowered(false); // set isPowered false, make tick() return true and remove it from list  
            }
            else //if there is no block in list
            {
                RedstoneSignalStills.add(new RedstoneSignalStill(worldObj.provider.getDimension(), pos, strength)); //new one and add it to list
                updatePosition(worldObj,pos); //update 
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
        Iterator<RedstoneSignalStill> iterator = RedstoneSignalStills.iterator();

        while (iterator.hasNext())
        {
            RedstoneSignalStill rs = iterator.next();
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
        for (RedstoneSignalStill rs : RedstoneSignalStills)
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

    public synchronized RedstoneSignalStill isPowered(World worldObj, BlockPos pos){
        int dimension = worldObj.provider.getDimension();
        for (RedstoneSignalStill rs : RedstoneSignalStills)
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
        NBTTagList nbtSignalList = nbt.getTagList("RedstoneSignalStills", 10);

        for (int i = 0; i < nbtSignalList.tagCount(); i++)
        {
            NBTTagCompound signalCompound = nbtSignalList.getCompoundTagAt(i);

            RedstoneSignalStill rs = new RedstoneSignalStill();
            rs.readFromNBT(signalCompound);

            this.RedstoneSignalStills.add(rs);
        }
    }

    @Override
    public synchronized NBTTagCompound writeToNBT(NBTTagCompound nbt)  //execute period 45 seconds
    {
        NBTTagList nbtSignalList = new NBTTagList();

        for (RedstoneSignalStill rs : RedstoneSignalStills) // deal every write action came to being in 45 seconds
        {
            NBTTagCompound signalCompound = new NBTTagCompound();
            rs.writeToNBT(signalCompound);
            nbtSignalList.appendTag(signalCompound);
        }

        nbt.setTag("RedstoneSignalStills", nbtSignalList);
        return nbt;
    }
}
