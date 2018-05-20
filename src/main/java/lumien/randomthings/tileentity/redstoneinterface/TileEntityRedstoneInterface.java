package lumien.randomthings.tileentity.redstoneinterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityRedstoneInterface extends TileEntityBase implements ITickable
{
	public static Set<TileEntityRedstoneInterface> interfaces = Collections.newSetFromMap(new WeakHashMap());

	public static Object lock = new Object();

	HashMap<EnumFacing, Integer> weakPower;
	HashMap<EnumFacing, Integer> strongPower;

	boolean firstTick = true;

	public TileEntityRedstoneInterface()
	{
		synchronized (lock)
		{
			interfaces.add(this);
		}

		weakPower = new HashMap<>();
		strongPower = new HashMap<>();

		for (EnumFacing facing : EnumFacing.values())
		{
			strongPower.put(facing, -1);
		}

		for (EnumFacing facing : EnumFacing.values())
		{
			weakPower.put(facing, -1);
		}
	}

	@Override
	public void update()
	{
		if (firstTick)
		{
			firstTick = false;

			if (weakPower.get(EnumFacing.DOWN) == -1)
			{
				updateRedstoneState(Blocks.REDSTONE_BLOCK);
			}
		}
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
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
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagCompound weakPowerCompound = compound.getCompoundTag("weakPowerCompound");
		NBTTagCompound strongPowerCompound = compound.getCompoundTag("strongPowerCompound");

		for (EnumFacing facing : EnumFacing.values())
		{
			weakPower.put(facing, weakPowerCompound.getInteger(facing.ordinal() + ""));
			strongPower.put(facing, strongPowerCompound.getInteger(facing.ordinal() + ""));
		}
	}

	static HashSet<BlockPos> checkedWeakPositions = new HashSet<>();

	public static int getRedstonePower(World blockWorld, BlockPos pos, EnumFacing facing)
	{
		synchronized (lock)
		{
			if (checkedWeakPositions.contains(pos))
			{
				return 0;
			}
			checkedWeakPositions.add(pos);

			int totalPower = 0;

			BlockPos checkingBlock = pos.offset(facing.getOpposite());

			ArrayList<TileEntityRedstoneInterface> interfaces = new ArrayList<>();
			interfaces.addAll(TileEntityRedstoneInterface.interfaces);

			for (TileEntityRedstoneInterface redstoneInterface : interfaces)
			{
				if (!redstoneInterface.isInvalid() && redstoneInterface.world == blockWorld && redstoneInterface.isTargeting(checkingBlock))
				{
					int remotePower = redstoneInterface.weakPower.get(facing);
					checkedWeakPositions.remove(pos);

					if (remotePower > totalPower)
					{
						totalPower = remotePower;
					}
				}
			}

			checkedWeakPositions.remove(pos);
			return totalPower;
		}
	}

	static HashSet<BlockPos> checkedStrongPositions = new HashSet<>();

	public static int getStrongPower(World blockWorld, BlockPos pos, EnumFacing facing)
	{
		synchronized (lock)
		{
			if (checkedStrongPositions.contains(pos))
			{
				return 0;
			}
			checkedStrongPositions.add(pos);

			int totalPower = 0;

			BlockPos checkingBlock = pos.offset(facing.getOpposite());

			ArrayList<TileEntityRedstoneInterface> interfaces = new ArrayList<>();
			interfaces.addAll(TileEntityRedstoneInterface.interfaces);

			for (TileEntityRedstoneInterface redstoneInterface : interfaces)
			{
				if (!redstoneInterface.isInvalid() && redstoneInterface.world == blockWorld && redstoneInterface.isTargeting(checkingBlock))
				{
					int remotePower = redstoneInterface.strongPower.get(facing);
					checkedStrongPositions.remove(pos);

					if (remotePower > totalPower)
					{
						totalPower = remotePower;
					}
				}
			}

			checkedStrongPositions.remove(pos);
			return totalPower;
		}
	}

	protected abstract void notifyTargets(Block neighborBlock);

	static HashSet<BlockPos> notifiedPositions = new HashSet<>();

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (notifiedPositions.contains(this.pos))
		{
			return;
		}

		notifiedPositions.add(this.pos);

		updateRedstoneState(neighborBlock);

		notifiedPositions.remove(this.pos);
	}

	private void updateRedstoneState(Block neighbor)
	{
		boolean changed = false;
		for (EnumFacing facing : EnumFacing.values())
		{
			int oldStrong = strongPower.get(facing);
			int newStrong;
			strongPower.put(facing, (newStrong = world.getStrongPower(this.pos.offset(facing), facing)));
			if (oldStrong != newStrong)
			{
				changed = true;
			}

			int oldWeak = weakPower.get(facing);
			int newWeak;
			weakPower.put(facing, (newWeak = world.getRedstonePower(this.pos.offset(facing), facing)));

			if (oldWeak != newWeak)
			{
				changed = true;
			}
		}

		if (changed)
		{
			notifyTargets(neighbor);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		this.invalidate();

		notifyTargets(Blocks.REDSTONE_BLOCK);
	}

	protected abstract boolean isTargeting(BlockPos pos);
}
