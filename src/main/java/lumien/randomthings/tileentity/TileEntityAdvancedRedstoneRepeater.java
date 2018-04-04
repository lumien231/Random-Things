package lumien.randomthings.tileentity;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.ContainerSynced;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAdvancedRedstoneRepeater extends TileEntityBase
{
	@ContainerSynced
	int turnOnDelay = 20;

	@ContainerSynced
	int turnOffDelay = 20;

	public TileEntityAdvancedRedstoneRepeater()
	{

	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("turnOnDelay", turnOnDelay);
		compound.setInteger("turnOffDelay", turnOffDelay);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.turnOnDelay = compound.getInteger("turnOnDelay");
		this.turnOffDelay = compound.getInteger("turnOffDelay");
	}

	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock()) && newState.getBlock() != ModBlocks.unpoweredAdvancedRedstoneRepeater && newState.getBlock() != ModBlocks.poweredAdvancedRedstoneRepeater;
	}

	public int getTurnOnDelay()
	{
		return turnOnDelay;
	}

	public int getTurnOffDelay()
	{
		return turnOffDelay;
	}

	public void decreaseTurnOnDelay(int amount)
	{
		turnOnDelay = Math.max(2, turnOnDelay - amount);
	}

	public void increaseTurnOnDelay(int amount)
	{
		turnOnDelay = Math.min(10000, turnOnDelay + amount);
	}

	public void decreaseTurnOffDelay(int amount)
	{
		turnOffDelay = Math.max(2, turnOffDelay - amount);
	}

	public void increaseTurnOffDelay(int amount)
	{
		turnOffDelay = Math.min(10000, turnOffDelay + amount);
	}
}
