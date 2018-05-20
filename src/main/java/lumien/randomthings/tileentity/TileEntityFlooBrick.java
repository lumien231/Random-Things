package lumien.randomthings.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lumien.randomthings.handler.floo.FlooNetworkHandler;
import lumien.randomthings.util.NBTUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityFlooBrick extends TileEntityBase
{
	boolean amMaster;

	// Master Properties
	UUID uuid;
	EnumFacing facing = EnumFacing.WEST;
	List<BlockPos> children = new ArrayList<BlockPos>();

	// Children Properties
	UUID masterUUID;

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!amMaster)
		{
			if (masterUUID != null)
			{
				FlooNetworkHandler handler = FlooNetworkHandler.get(worldIn);
				TileEntity masterTE = handler.getFirePlaceTE(worldIn, masterUUID);

				if (masterTE instanceof TileEntityFlooBrick)
				{
					((TileEntityFlooBrick) masterTE).breakBlock(worldIn, masterTE.getPos(), state);
				}
			}
		}
		else
		{
			for (BlockPos children : children)
			{
				worldIn.setBlockState(children, Blocks.BRICK_BLOCK.getDefaultState());
			}

			FlooNetworkHandler.get(worldIn).brokenMaster(worldIn, pos, this);

			worldIn.setBlockState(this.pos, Blocks.BRICK_BLOCK.getDefaultState());
		}
	}

	@Override
	public void onLoad()
	{
		if (!this.world.isRemote && uuid != null)
		{
			FlooNetworkHandler.get(this.world).updatePosition(uuid, pos);
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setBoolean("amMaster", amMaster);

		if (amMaster)
		{
			compound.setTag("uuid", net.minecraft.nbt.NBTUtil.createUUIDTag(uuid));

			compound.setInteger("facing", facing.ordinal());

			NBTTagList childrenTagList = new NBTTagList();

			for (BlockPos pos : children)
			{
				NBTTagCompound childrenCompound = new NBTTagCompound();
				NBTUtil.writeBlockPosToNBT(childrenCompound, "pos", pos);

				childrenTagList.appendTag(childrenCompound);
			}

			compound.setTag("children", childrenTagList);
		}
		else
		{
			if (masterUUID != null)
				compound.setTag("masterUUID", net.minecraft.nbt.NBTUtil.createUUIDTag(this.masterUUID));
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.amMaster = compound.getBoolean("amMaster");

		if (amMaster)
		{
			this.uuid = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));

			this.facing = EnumFacing.VALUES[compound.getInteger("facing")];

			NBTTagList childrenTagList = compound.getTagList("children", 10);

			for (int i = 0; i < childrenTagList.tagCount(); i++)
			{
				NBTTagCompound childrenCompound = childrenTagList.getCompoundTagAt(i);

				children.add(NBTUtil.readBlockPosFromNBT(childrenCompound, "pos"));
			}
		}
		else
		{
			if (compound.hasKey("masterUUID"))
				this.masterUUID = net.minecraft.nbt.NBTUtil.getUUIDFromTag(compound.getCompoundTag("masterUUID"));
		}
	}

	public UUID getFirePlaceUid()
	{
		return amMaster ? this.uuid : masterUUID;
	}

	public EnumFacing getFacing()
	{
		return facing;
	}

	public boolean isMaster()
	{
		return amMaster;
	}

	public UUID getUid()
	{
		return uuid;
	}

	public void initToChild(UUID masterUUID)
	{
		this.amMaster = false;
		this.masterUUID = masterUUID;
	}

	public void initToMaster(List<BlockPos> brickList, EnumFacing teleportFacing, UUID uuid)
	{
		this.uuid = uuid;
		this.amMaster = true;
		this.children = brickList;
		this.facing = teleportFacing;
	}

	public List<BlockPos> getChildren()
	{
		return children;
	}
}
