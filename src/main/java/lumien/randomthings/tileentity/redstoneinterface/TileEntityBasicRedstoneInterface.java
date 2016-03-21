package lumien.randomthings.tileentity.redstoneinterface;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityBasicRedstoneInterface extends TileEntityRedstoneInterface implements SimpleComponent
{
	BlockPos target;

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		if (target != null)
		{
			compound.setInteger("targetX", target.getX());
			compound.setInteger("targetY", target.getY());
			compound.setInteger("targetZ", target.getZ());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
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
			IBlockState state = this.worldObj.getBlockState(this.pos);
			this.worldObj.notifyBlockUpdate(pos, state, state, 3);

			if (oldTarget != null)
			{
				IBlockState targetState = worldObj.getBlockState(oldTarget);
				targetState.getBlock().onNeighborBlockChange(worldObj, oldTarget, targetState, Blocks.redstone_block);
				worldObj.notifyNeighborsOfStateChange(oldTarget, Blocks.redstone_block);
			}

			if (this.target != null)
			{
				IBlockState targetState = worldObj.getBlockState(target);
				targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, Blocks.redstone_block);
				worldObj.notifyNeighborsOfStateChange(target, Blocks.redstone_block);
			}
		}
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (this.target != null)
		{
			IBlockState targetState = worldIn.getBlockState(target);
			targetState.getBlock().onNeighborBlockChange(worldIn, target, targetState, neighborBlock);
			worldIn.notifyNeighborsOfStateChange(target, neighborBlock);
		}
	}

	public void broken()
	{
		super.broken();

		if (this.target != null)
		{
			IBlockState targetState = worldObj.getBlockState(target);
			targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, Blocks.redstone_block);
			worldObj.notifyNeighborsOfStateChange(target, Blocks.redstone_block);
		}
	}

	public BlockPos getTarget()
	{
		return this.target;
	}

	@Override
	public String getComponentName()
	{
		return "redstoneInterface";
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] setTarget(Context context, Arguments args)
	{
		this.setTarget(new BlockPos(args.checkInteger(0), args.checkInteger(1), args.checkInteger(2)));
		return new Object[] {};
	}

	@Callback
	@Optional.Method(modid = "OpenComputers")
	public Object[] getTarget(Context context, Arguments args)
	{
		if (this.target == null)
		{
			return new Object[] {};
		}
		else
		{
			return new Object[] { target.getX(), target.getY(), target.getZ() };
		}
	}

	@Override
	protected boolean isTargeting(BlockPos pos)
	{
		return this.target != null && this.target.equals(pos);
	}
}
