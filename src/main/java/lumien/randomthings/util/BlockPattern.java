package lumien.randomthings.util;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPattern
{
	public class BlockInfo
	{
		IBlockState state;

		BlockPos mod;

		private BlockInfo(IBlockState state, BlockPos mod)
		{
			this.state = state;

			this.mod = mod;
		}

		public BlockPos getMod()
		{
			return mod;
		}

		public IBlockState getState()
		{
			return state;
		}
	}

	ArrayList<BlockInfo> blockInfos;

	public BlockPattern()
	{
		blockInfos = new ArrayList<>();
	}

	public ArrayList<BlockInfo> getBlockInfo()
	{
		return blockInfos;
	}

	public void addBlock(IBlockState state, int modX, int modY, int modZ)
	{
		this.blockInfos.add(new BlockInfo(state, new BlockPos(modX, modY, modZ)));
	}

	public void place(World worldObj, BlockPos pos, int flag)
	{
		for (BlockInfo info : blockInfos)
		{
			worldObj.setBlockState(pos.add(info.mod), info.state, flag);
		}
	}
}
