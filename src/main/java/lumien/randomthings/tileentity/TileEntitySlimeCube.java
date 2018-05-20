package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import lumien.randomthings.lib.IRedstoneSensitive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySlimeCube extends TileEntityBase implements IRedstoneSensitive
{
	public static Set<TileEntitySlimeCube> cubes = Collections.newSetFromMap(new WeakHashMap());

	public TileEntitySlimeCube()
	{
		synchronized (cubes)
		{
			cubes.add(this);
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		this.invalidate();
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}
}
