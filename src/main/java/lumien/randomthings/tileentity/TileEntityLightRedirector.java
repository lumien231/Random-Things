package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.block.BlockLightRedirector;
import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageLightRedirector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityLightRedirector extends TileEntityBase
{
	public static Set<TileEntityLightRedirector> redirectorSet = Collections.newSetFromMap(new WeakHashMap());

	public Map<BlockPos, BlockPos> targets;

	HashMap<EnumFacing, Boolean> enabledMap;

	public boolean established;

	public TileEntityLightRedirector()
	{
		enabledMap = new HashMap<EnumFacing, Boolean>();
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			enabledMap.put(facing, true);
		}

		synchronized (redirectorSet)
		{
			redirectorSet.add(this);
			targets = new HashMap<>();
		}
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		super.onDataPacket(net, packet);

		synchronized (redirectorSet)
		{
			this.targets.clear();
		}

		for (EnumFacing facing : EnumFacing.values())
		{
			IBlockState state = this.world.getBlockState(this.pos.offset(facing));
			this.world.notifyBlockUpdate(pos.offset(facing), state, state, 3);
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
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		for (int i = 0; i < EnumFacing.VALUES.length; i++)
		{
			compound.setBoolean("enabled" + i, enabledMap.get(EnumFacing.VALUES[i]));
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		for (int i = 0; i < EnumFacing.VALUES.length; i++)
		{
			if (compound.hasKey("enabled" + i))
			{
				enabledMap.put(EnumFacing.VALUES[i], compound.getBoolean("enabled" + i));
			}
		}
	}

	public IBlockState makeState(IBlockState state)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			state = state.withProperty(BlockLightRedirector.enabledProperties[facing.ordinal()], enabledMap.get(facing));
		}

		return state;
	}

	public boolean isEnabled(EnumFacing facing)
	{
		return enabledMap.get(facing);
	}

	public void toggleSide(EnumFacing facing)
	{
		this.enabledMap.put(facing, !this.enabledMap.get(facing));

		syncTE();
	}

}
