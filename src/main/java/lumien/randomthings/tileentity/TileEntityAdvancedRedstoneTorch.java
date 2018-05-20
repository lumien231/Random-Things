package lumien.randomthings.tileentity;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.ContainerSynced;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAdvancedRedstoneTorch extends TileEntityBase
{
	@ContainerSynced
	int signalStrengthOn = 15;

	@ContainerSynced
	int signalStrengthOff = 0;

	public TileEntityAdvancedRedstoneTorch()
	{

	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("signalStrengthOn", signalStrengthOn);
		compound.setInteger("signalStrengthOff", signalStrengthOff);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.signalStrengthOn = compound.getInteger("signalStrengthOn");
		this.signalStrengthOff = compound.getInteger("signalStrengthOff");
	}

	@Override
	public boolean syncAdditionalData()
	{
		return true;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock()) && newState.getBlock() != ModBlocks.advancedRedstoneTorchOff && newState.getBlock() != ModBlocks.advancedRedstoneTorchOn;
	}

	public int getSignalStrengthOn()
	{
		return signalStrengthOn;
	}

	public int getSignalStrengthOff()
	{
		return signalStrengthOff;
	}

	public void decreaseSignalStrengthOff(int amount)
	{
		signalStrengthOff = Math.max(0, signalStrengthOff - amount);

		syncTE();
		this.world.notifyNeighborsOfStateChange(pos, ModBlocks.advancedRedstoneTorchOn, false);
	}

	public void decreaseSignalStrengthOn(int amount)
	{
		signalStrengthOn = Math.max(0, signalStrengthOn - amount);

		syncTE();
		this.world.notifyNeighborsOfStateChange(pos, ModBlocks.advancedRedstoneTorchOn, false);
	}

	public void increaseSignalStrengthOff(int amount)
	{
		signalStrengthOff = Math.min(15, signalStrengthOff + amount);

		syncTE();
		this.world.notifyNeighborsOfStateChange(pos, ModBlocks.advancedRedstoneTorchOn, false);
	}

	public void increaseSignalStrengthOn(int amount)
	{
		signalStrengthOn = Math.min(15, signalStrengthOn + amount);

		syncTE();
		this.world.notifyNeighborsOfStateChange(pos, ModBlocks.advancedRedstoneTorchOn, false);
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}
}
