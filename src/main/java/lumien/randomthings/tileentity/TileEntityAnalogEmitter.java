package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockAnalogEmitter;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAnalogEmitter extends TileEntityBase
{
	boolean powering;
	public int emitLevel;

	public TileEntityAnalogEmitter()
	{
		emitLevel = 1;
		powering = false;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("emitLevel", emitLevel);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.emitLevel = compound.getInteger("emitLevel");
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		EnumFacing facing = state.getValue(BlockAnalogEmitter.FACING);
		boolean input = worldIn.getRedstonePower(pos.offset(facing), facing.getOpposite()) > 0;
		if (input != powering)
		{
			powering = input;
			worldIn.notifyNeighborsOfStateChange(pos, ModBlocks.analogEmitter, false);
		}
	}

	public int getOutput()
	{
		if (powering)
		{
			return emitLevel;
		}
		else
		{
			return 0;
		}
	}

	public void setLevel(int level)
	{
		this.emitLevel = level;

		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);

		EnumFacing[] aenumfacing = EnumFacing.values();
		int i = aenumfacing.length;

		for (int j = 0; j < i; ++j)
		{
			EnumFacing enumfacing = aenumfacing[j];
			world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this.blockType, false);
		}

		world.notifyNeighborsOfStateChange(pos, this.blockType, false);
	}
}
