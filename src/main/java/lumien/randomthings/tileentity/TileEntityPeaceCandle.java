package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPeaceCandle extends TileEntityBase
{
	public static Set<TileEntityPeaceCandle> candles = Collections.newSetFromMap(new WeakHashMap());

	public TileEntityPeaceCandle()
	{
		synchronized (candles)
		{
			candles.add(this);
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);

		this.invalidate();
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
	public void onChunkUnload()
	{
		this.invalidate();
	}
}
