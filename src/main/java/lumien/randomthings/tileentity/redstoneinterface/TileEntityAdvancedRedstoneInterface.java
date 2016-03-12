package lumien.randomthings.tileentity.redstoneinterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import lumien.randomthings.item.ItemPositionFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.NBTUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityAdvancedRedstoneInterface extends TileEntityRedstoneInterface implements IInvBasic
{
	InventoryBasic positionInventory = new InventoryBasic("Advanced Redstone Interface", false, 9);

	HashSet<BlockPos> targets;

	public TileEntityAdvancedRedstoneInterface()
	{
		targets = new HashSet<BlockPos>();
		positionInventory.addInventoryChangeListener(this);
	}
	
	public Set<BlockPos> getTargets()
	{
		return targets;
	}

	@Override
	protected boolean isTargeting(BlockPos pos)
	{
		return targets.contains(pos);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		NBTTagList nbtTargetList = new NBTTagList();

		for (BlockPos pos : targets)
		{
			NBTTagCompound targetCompound = new NBTTagCompound();

			NBTUtil.writeBlockPosToNBT(targetCompound, "target", pos);
			nbtTargetList.appendTag(targetCompound);
		}

		compound.setTag("targets", nbtTargetList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		NBTTagList nbtTargetList = compound.getTagList("targets", 10);

		if (nbtTargetList != null)
		{
			for (int i = 0; i < nbtTargetList.tagCount(); i++)
			{
				NBTTagCompound targetCompound = nbtTargetList.getCompoundTagAt(i);

				this.targets.add(NBTUtil.readBlockPosFromNBT(targetCompound, "target"));
			}
		}
	}

	@Override
	public void onInventoryChanged(InventoryBasic inventory)
	{
		HashSet<BlockPos> newTargets = new HashSet<BlockPos>();

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);

			BlockPos target;
			if (stack != null && stack.getItem() == ModItems.positionFilter && (target = ItemPositionFilter.getPosition(stack)) != null)
			{
				newTargets.add(target);
			}
		}

		HashSet<BlockPos> changedPositions = new HashSet<BlockPos>();

		for (BlockPos target : newTargets)
		{
			if (!targets.contains(target))
			{
				changedPositions.add(target); // Added
			}
		}

		for (BlockPos oldTarget : targets)
		{
			if (!newTargets.contains(oldTarget))
			{
				changedPositions.add(oldTarget); // Added
			}
		}

		this.targets = newTargets;
		this.worldObj.markBlockForUpdate(this.pos);

		for (BlockPos changedPos : changedPositions)
		{
			IBlockState targetState = worldObj.getBlockState(changedPos);
			targetState.getBlock().onNeighborBlockChange(worldObj, changedPos, targetState, this.blockType);
			worldObj.notifyNeighborsOfStateChange(changedPos, targetState.getBlock());
		}
	}

	public IInventory getTargetInventory()
	{
		return positionInventory;
	}
	
	public void broken()
	{
		super.broken();

		for (BlockPos target : targets)
		{
			if (target != null)
			{
				IBlockState targetState = worldObj.getBlockState(target);
				targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, this.blockType);
				worldObj.notifyNeighborsOfStateChange(target, targetState.getBlock());
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		for (BlockPos target : targets)
		{
			if (target != null)
			{
				IBlockState targetState = worldObj.getBlockState(target);
				targetState.getBlock().onNeighborBlockChange(worldObj, target, targetState, neighborBlock);
				worldObj.notifyNeighborsOfStateChange(target, targetState.getBlock());
			}
		}
	}
}
