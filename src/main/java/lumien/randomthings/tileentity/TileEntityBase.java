package lumien.randomthings.tileentity;

import lumien.randomthings.lib.IRedstoneSensitive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityBase extends TileEntity
{
	private IItemHandler inventoryHandler;
	private boolean redstonePowered;

	protected void setItemHandler(IItemHandler handler)
	{
		this.inventoryHandler = handler;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		writeDataToNBT(compound);

		if (this.inventoryHandler instanceof ItemStackHandler)
		{
			NBTTagCompound inventoryCompound = ((ItemStackHandler)inventoryHandler).serializeNBT();
			compound.setTag("inventory", inventoryCompound);
		}

		if (this instanceof IRedstoneSensitive)
		{
			compound.setBoolean("redstonePowered", redstonePowered);
		}

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		readDataFromNBT(compound);

		if (this.inventoryHandler instanceof ItemStackHandler)
		{
			((ItemStackHandler)inventoryHandler).deserializeNBT(compound.getCompoundTag("inventory"));
		}

		if (this instanceof IRedstoneSensitive)
		{
			this.redstonePowered = compound.getBoolean("redstonePowered");
		}
	}

	@Override
	public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		if (writeNBTToDescriptionPacket())
		{
			readDataFromNBT(packet.getNbtCompound());
		}

		if (renderAfterData())
		{
			IBlockState state = this.worldObj.getBlockState(this.pos);
			this.worldObj.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound baseCompound = super.getUpdateTag();

		if (writeNBTToDescriptionPacket())
		{
			this.writeDataToNBT(baseCompound);
		}

		return baseCompound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		if (writeNBTToDescriptionPacket())
		{
			this.writeDataToNBT(nbtTag);
		}
		return new SPacketUpdateTileEntity(this.pos, 1, nbtTag);
	}

	public void syncTE()
	{
		IBlockState state = this.worldObj.getBlockState(this.pos);
		this.worldObj.notifyBlockUpdate(pos, state, state, 3);
	}

	public abstract void writeDataToNBT(NBTTagCompound compound);

	public abstract void readDataFromNBT(NBTTagCompound compound);

	public boolean renderAfterData()
	{
		return false;
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
	{
		boolean newPowered = this.worldObj.isBlockIndirectlyGettingPowered(this.pos) > 0;
		boolean changed = redstonePowered != newPowered;

		if (changed)
		{
			((IRedstoneSensitive)this).redstoneChange(redstonePowered, newPowered);
			
			this.redstonePowered = newPowered;
			
			this.markDirty();
		}
	}

	public boolean writeNBTToDescriptionPacket()
	{
		return true;
	}

	protected boolean isPoweredByRedstone()
	{
		return redstonePowered;
	}

	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
	{
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventoryHandler != null)
		{
			return (T) inventoryHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
	{
		return (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventoryHandler != null) || super.hasCapability(capability, facing);
	}
}
