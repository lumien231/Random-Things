package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageLightRedirector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityLightRedirector extends TileEntityBase
{
	public static Set<TileEntityLightRedirector> redirectorSet = Collections.newSetFromMap(new WeakHashMap());

	public Map<BlockPos, BlockPos> targets;
	
	public boolean established;

	public TileEntityLightRedirector()
	{
		synchronized (redirectorSet)
		{
			redirectorSet.add(this);
			targets = new HashMap<>();
		}
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	@Override
	public void onLoad()
	{
		super.onLoad();

		if (this.world.isRemote)
		{
			established = true;
			for (EnumFacing facing : EnumFacing.values())
			{
				this.world.markChunkDirty(this.pos.offset(facing), null);
			}
		}
	}

	public void broken()
	{
		this.invalidate();

		MessageLightRedirector message = new MessageLightRedirector(this.world.provider.getDimension(), pos);
		MessageUtil.sendToAllWatchingPos(world, pos, message);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
	}

}
