package lumien.randomthings.tileentity;

import java.util.HashMap;
import java.util.HashSet;

import lumien.randomthings.block.BlockInventoryRerouter;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityInventoryRerouter extends TileEntityBase
{
	HashMap<EnumFacing, EnumFacing> facingMap;

	public TileEntityInventoryRerouter()
	{
		facingMap = new HashMap<>();

		for (EnumFacing f : EnumFacing.VALUES)
		{
			facingMap.put(f, f);
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList facingList = new NBTTagList();

		for (int i = 0; i < EnumFacing.VALUES.length; i++)
		{
			EnumFacing facing = EnumFacing.VALUES[i];
			EnumFacing override = facingMap.get(facing);

			facingList.appendTag(new NBTTagInt(override == null ? -1 : override.getIndex()));
		}

		compound.setTag("facingList", facingList);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		NBTTagList facingList = compound.getTagList("facingList", (byte) 3);

		for (int i = 0; i < facingList.tagCount(); i++)
		{
			EnumFacing facing = EnumFacing.VALUES[i];

			int overrideIndex = facingList.getIntAt(i);

			EnumFacing override = overrideIndex == -1 ? null : EnumFacing.VALUES[overrideIndex];
			facingMap.put(facing, override);
		}
	}

	static HashSet<TileEntityInventoryRerouter> circleSet = new HashSet<>();

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (circleSet.contains(this))
		{
			return null;
		}

		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			IBlockState myState = this.world.getBlockState(this.pos);

			if (myState.getValue(BlockInventoryRerouter.FACING) != facing)
			{
				EnumFacing override = facingMap.get(facing);

				if (override == null)
				{
					return null;
				}

				EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockInventoryRerouter.FACING);
				TileEntity facingTE = this.world.getTileEntity(this.pos.offset(myFacing));

				if (facingTE == null)
				{
					return null;
				}

				circleSet.add(this);
				T resultCap = facingTE.getCapability(capability, override);
				circleSet.remove(this);

				return resultCap;
			}
		}

		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (circleSet.contains(this))
		{
			return false;
		}

		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			IBlockState myState = this.world.getBlockState(this.pos);

			if (myState.getValue(BlockInventoryRerouter.FACING) != facing)
			{
				EnumFacing override = facingMap.get(facing);

				if (override == null)
				{
					return false;
				}

				EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockInventoryRerouter.FACING);
				TileEntity facingTE = this.world.getTileEntity(this.pos.offset(myFacing));

				if (facingTE == null)
				{
					return false;
				}

				circleSet.add(this);
				boolean resultBool = facingTE.hasCapability(capability, override);
				circleSet.remove(this);

				return resultBool;
			}
		}
		return super.hasCapability(capability, facing);
	}

	public void rotateFacing(EnumFacing facing)
	{
		EnumFacing current = facingMap.get(facing);

		EnumFacing newFacing;
		if (current == null)
		{
			newFacing = EnumFacing.VALUES[0];
		}
		else if (current == EnumFacing.EAST)
		{
			newFacing = null;
		}
		else
		{
			newFacing = EnumFacing.VALUES[current.getIndex() + 1];
		}

		this.facingMap.put(facing, newFacing);

		this.syncTE();
		this.world.notifyNeighborsOfStateChange(this.pos, ModBlocks.inventoryRerouter, false);
	}

	public HashMap<EnumFacing, EnumFacing> getFacingMap()
	{
		return facingMap;
	}
}
