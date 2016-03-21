package lumien.randomthings.tileentity.redstoneinterface;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityRedstoneInterface extends TileEntityBase
{
	public static Set<TileEntityRedstoneInterface> interfaces = Collections.newSetFromMap(new WeakHashMap());

	public TileEntityRedstoneInterface()
	{
		synchronized (interfaces)
		{
			interfaces.add(this);
		}
	}
	
	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	static HashSet<BlockPos> checkedWeakPositions = new HashSet<BlockPos>();

	public static synchronized int getRedstonePower(World blockWorld, BlockPos pos, EnumFacing facing)
	{
		if (checkedWeakPositions.contains(pos))
		{
			return 0;
		}
		checkedWeakPositions.add(pos);

		int totalPower = 0;

		BlockPos checkingBlock = pos.offset(facing.getOpposite());
		synchronized (interfaces)
		{
			for (TileEntityRedstoneInterface redstoneInterface : interfaces)
			{
				if (!redstoneInterface.isInvalid() && redstoneInterface.worldObj == blockWorld && redstoneInterface.isTargeting(checkingBlock))
				{
					int remotePower = redstoneInterface.worldObj.getRedstonePower(redstoneInterface.pos.offset(facing), facing);
					checkedWeakPositions.remove(pos);

					if (remotePower > totalPower)
					{
						totalPower = remotePower;
					}
				}
			}
		}

		checkedWeakPositions.remove(pos);
		return totalPower;
	}

	static HashSet<BlockPos> checkedStrongPositions = new HashSet<BlockPos>();

	public static int getStrongPower(World blockWorld, BlockPos pos, EnumFacing facing)
	{
		if (checkedStrongPositions.contains(pos))
		{
			return 0;
		}
		checkedStrongPositions.add(pos);

		int totalPower = 0;

		BlockPos checkingBlock = pos.offset(facing.getOpposite());

		synchronized (interfaces)
		{
			for (TileEntityRedstoneInterface redstoneInterface : interfaces)
			{
				if (!redstoneInterface.isInvalid() && redstoneInterface.worldObj == blockWorld && redstoneInterface.isTargeting(checkingBlock))
				{
					int remotePower = redstoneInterface.worldObj.getStrongPower(redstoneInterface.pos.offset(facing), facing);
					checkedStrongPositions.remove(pos);

					if (remotePower > totalPower)
					{
						totalPower = remotePower;
					}
				}
			}
		}

		checkedStrongPositions.remove(pos);
		return totalPower;
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{

	}

	public void broken()
	{
		this.invalidate();
	}

	protected abstract boolean isTargeting(BlockPos pos);
}
