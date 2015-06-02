package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockAnalogEmitter;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		EnumFacing facing = (EnumFacing) state.getValue(BlockAnalogEmitter.FACING);
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
		this.worldObj.markBlockForUpdate(this.pos);
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
