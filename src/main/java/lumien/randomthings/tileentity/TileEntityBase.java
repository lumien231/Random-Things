package lumien.randomthings.tileentity;

import java.util.HashMap;

import lumien.randomthings.lib.IOpable;
import lumien.randomthings.lib.IRedstoneSensitive;
import lumien.randomthings.lib.ISlotFilter;
import lumien.randomthings.lib.ItemHandlerWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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
	private IItemHandler publicInventoryHandler;

	private HashMap<Integer, ISlotFilter> slotFilter;

	private Runnable inventoryChangeListener;

	private boolean itemHandlerInternal = true;

	private boolean redstonePowered;
	
	private boolean op;
	
	public boolean isOp()
	{
		return op;
	}

	protected void setItemHandler(int slots)
	{
		this.inventoryHandler = new ItemStackHandler(slots)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slots);
				TileEntityBase.this.markDirty();

				if (inventoryChangeListener != null)
				{
					inventoryChangeListener.run();
				}
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
			{
				if (slotFilter != null && slotFilter.containsKey(slot))
				{
					if (!slotFilter.get(slot).isItemStackValid(stack))
					{
						return stack;
					}
				}

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	protected void setInventoryChangeListener(Runnable runnable)
	{
		this.inventoryChangeListener = runnable;
	}

	protected void addSlotFilter(int slot, ISlotFilter filter)
	{
		if (slotFilter == null)
		{
			slotFilter = new HashMap<Integer, ISlotFilter>();
		}

		slotFilter.put(slot, filter);
	}

	protected void setItemHandlerPublic(int[] insertSlots, int[] outputSlots)
	{
		this.itemHandlerInternal = false;
		this.publicInventoryHandler = new ItemHandlerWrapper(inventoryHandler, insertSlots, outputSlots);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		writeDataToNBT(compound, false);

		if (this.inventoryHandler instanceof ItemStackHandler)
		{
			NBTTagCompound inventoryCompound = ((ItemStackHandler) inventoryHandler).serializeNBT();
			compound.setTag("inventory", inventoryCompound);
		}
		
		if (this instanceof IOpable)
		{
			compound.setBoolean("op", op);
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

		readDataFromNBT(compound, false);

		if (this.inventoryHandler instanceof ItemStackHandler)
		{
			((ItemStackHandler) inventoryHandler).deserializeNBT(compound.getCompoundTag("inventory"));
		}

		if (this instanceof IRedstoneSensitive)
		{
			this.redstonePowered = compound.getBoolean("redstonePowered");
		}
		
		if (this instanceof IOpable)
		{
			this.op = compound.getBoolean("op");
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
	{
		handleUpdateTag(packet.getNbtCompound());

		if (renderAfterData())
		{
			IBlockState state = this.world.getBlockState(this.pos);
			this.world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		if (syncAdditionalData())
		{
			readDataFromNBT(tag, true);
		}
		
		if (this instanceof IRedstoneSensitive)
		{
			this.redstonePowered = tag.getBoolean("redstonePowered");
		}
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound baseCompound = super.getUpdateTag();

		if (this instanceof IRedstoneSensitive)
		{
			baseCompound.setBoolean("redstonePowered", redstonePowered);
		}

		if (syncAdditionalData())
		{
			this.writeDataToNBT(baseCompound, true);
		}

		return baseCompound;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	public void syncTE()
	{
		IBlockState state = this.world.getBlockState(this.pos);
		this.world.notifyBlockUpdate(pos, state, state, 3);
	}

	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{

	}

	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{

	}

	public boolean renderAfterData()
	{
		return false;
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (this instanceof IRedstoneSensitive)
		{
			boolean newPowered = this.world.isBlockIndirectlyGettingPowered(this.pos) > 0;
			boolean changed = redstonePowered != newPowered;

			if (changed)
			{
				this.redstonePowered = newPowered;
				
				((IRedstoneSensitive) this).redstoneChange(!newPowered, newPowered);

				this.markDirty();
				this.syncTE();
			}
		}
	}

	public boolean syncAdditionalData()
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
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !itemHandlerInternal)
		{
			return (T) publicInventoryHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing)
	{
		return (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && !itemHandlerInternal) || super.hasCapability(capability, facing);
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
	}

	public boolean isRedstonePowered()
	{
		return redstonePowered;
	}

	public IItemHandler getItemHandler()
	{
		return inventoryHandler;
	}

	public boolean toggleOp()
	{
		this.op = !op;
		
		return this.op;
	}
}
