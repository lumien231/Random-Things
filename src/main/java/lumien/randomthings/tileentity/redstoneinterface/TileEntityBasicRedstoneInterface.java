package lumien.randomthings.tileentity.redstoneinterface;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TileEntityBasicRedstoneInterface extends TileEntityRedstoneInterface
{
	BlockPos target;

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		super.writeDataToNBT(compound, sync);

		if (target != null)
		{
			compound.setInteger("targetX", target.getX());
			compound.setInteger("targetY", target.getY());
			compound.setInteger("targetZ", target.getZ());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		super.readDataFromNBT(compound, sync);

		if (compound.hasKey("targetX"))
		{
			target = new BlockPos(compound.getInteger("targetX"), compound.getInteger("targetY"), compound.getInteger("targetZ"));
		}
	}

	public void setTarget(BlockPos newTarget)
	{
		if (!newTarget.equals(target))
		{
			BlockPos oldTarget = this.target;

			this.target = newTarget;
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(pos, state, state, 3);

			if (!this.world.isRemote)
			{
				if (oldTarget != null)
				{
					IBlockState targetState = world.getBlockState(oldTarget);
					targetState.neighborChanged(world, oldTarget, Blocks.REDSTONE_BLOCK, this.pos); // TODO
																									// DANGEROUS
					world.notifyNeighborsOfStateChange(oldTarget, Blocks.REDSTONE_BLOCK, false);
				}

				if (this.target != null)
				{
					IBlockState targetState = world.getBlockState(target);
					targetState.neighborChanged(world, target, Blocks.REDSTONE_BLOCK, this.pos); // TODO
																									// DANGEROUS
					world.notifyNeighborsOfStateChange(target, Blocks.REDSTONE_BLOCK, false);
				}
			}
		}
	}

	public BlockPos getTarget()
	{
		return this.target;
	}

	@Override
	protected boolean isTargeting(BlockPos pos)
	{
		return this.target != null && this.target.equals(pos);
	}

	@Override
	protected void notifyTargets(Block neighborBlock)
	{
		if (this.target != null)
		{
			IBlockState targetState = world.getBlockState(target);
			targetState.neighborChanged(world, target, neighborBlock, this.pos);
			world.notifyNeighborsOfStateChange(target, neighborBlock, false);
		}
	}
}
