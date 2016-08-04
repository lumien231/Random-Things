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
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setInteger("emitLevel", emitLevel);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.emitLevel = compound.getInteger("emitLevel");
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
	{
		EnumFacing facing = state.getValue(BlockAnalogEmitter.FACING);
		boolean input = worldIn.getRedstonePower(pos.offset(facing), facing.getOpposite()) > 0;
		if (input != powering)
		{
			powering = input;
			worldIn.notifyNeighborsOfStateChange(pos, ModBlocks.analogEmitter);
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
		
		IBlockState state = this.worldObj.getBlockState(this.pos);
		this.worldObj.notifyBlockUpdate(pos, state, state, 3);
		
		EnumFacing[] aenumfacing = EnumFacing.values();
		int i = aenumfacing.length;

		for (int j = 0; j < i; ++j)
		{
			EnumFacing enumfacing = aenumfacing[j];
			worldObj.notifyNeighborsOfStateChange(pos.offset(enumfacing), this.blockType);
		}

		worldObj.notifyNeighborsOfStateChange(pos, this.blockType);
	}
}
