package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityRedstoneInterface extends TileEntityBase implements SimpleComponent
{
	public static Set<TileEntityRedstoneInterface> interfaces = Collections.newSetFromMap(new WeakHashMap());

	BlockPos target;

	public TileEntityRedstoneInterface()
	{
		synchronized (interfaces)
		{
			interfaces.add(this);
		}
	}

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

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	public void setTarget(BlockPos newTarget)
	{
		if (!newTarget.equals(target))
		{
			BlockPos oldTarget = this.target;

			this.target = newTarget;
			this.worldObj.markBlockForUpdate(this.pos);

			if (oldTarget != null)
			{
				IBlockState targetState = worldObj.getBlockState(oldTarget);
				targetState.getBlock().onNeighborBlockChange(worldObj, oldTarget, targetState, this.blockType);
				worldObj.notifyNeighborsOfStateChange(oldTarget, targetState.getBlock());
			}

			if (this.target != null)
			{
				IBlockState targetState = worldObj.getBlockState(target);
				targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, this.blockType);
				worldObj.notifyNeighborsOfStateChange(target, targetState.getBlock());
			}
		}
	}

	static HashSet<BlockPos> checkedPositions = new HashSet<BlockPos>();

	public static synchronized int getRedstonePower(World blockWorld, BlockPos pos, EnumFacing facing)
	{
		if (checkedPositions.contains(pos))
		{
			return 0;
		}
		checkedPositions.add(pos);

		BlockPos checkingBlock = pos.offset(facing.getOpposite());
		synchronized (interfaces)
		{
			for (TileEntityRedstoneInterface redstoneInterface : interfaces)
			{
				if (!redstoneInterface.isInvalid() && redstoneInterface.worldObj == blockWorld && redstoneInterface.target != null)
				{
					if (redstoneInterface.target.equals(pos))
					{
						int remotePower = redstoneInterface.worldObj.getRedstonePower(redstoneInterface.pos, facing);
						checkedPositions.remove(pos);
						return remotePower;
					}
					else if (redstoneInterface.target.equals(checkingBlock))
					{
						int remotePower = redstoneInterface.worldObj.getRedstonePower(redstoneInterface.pos.offset(facing), facing);

						checkedPositions.remove(pos);
						return remotePower;
					}
				}
			}
		}
		checkedPositions.remove(pos);
		return 0;
	}

	public static int getStrongPower(BlockPos pos, EnumFacing facing)
	{
		return 0;
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (this.target != null)
		{
			IBlockState targetState = worldIn.getBlockState(target);
			targetState.getBlock().onNeighborBlockChange(worldIn, target, targetState, neighborBlock);
			worldIn.notifyNeighborsOfStateChange(target, targetState.getBlock());
		}
	}

	public void broken()
	{
		if (this.target != null)
		{
			this.invalidate();
			IBlockState targetState = worldObj.getBlockState(target);
			targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, this.blockType);
			worldObj.notifyNeighborsOfStateChange(target, targetState.getBlock());
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
}
