package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockItemProjector;
import lumien.randomthings.entitys.EntityProjectedItem;
import lumien.randomthings.lib.ContainerSynced;
import lumien.randomthings.util.RandomUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityItemProjector extends TileEntityBase implements ITickable
{
	public enum SELECTION_MODE
	{
		FIRST, LAST, BIGGEST;
	}

	@ContainerSynced
	SELECTION_MODE selectionMode = SELECTION_MODE.LAST;


	int cooldownTimer;

	public TileEntityItemProjector()
	{
		this.setItemHandler(new ItemStackHandler(9)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				TileEntityItemProjector.this.markDirty();
			}
		});
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setInteger("cooldownTimer", cooldownTimer);
		compound.setInteger("selectionMode", selectionMode.ordinal());
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.cooldownTimer = compound.getInteger("cooldownTimer");
		this.selectionMode = SELECTION_MODE.values()[compound.getInteger("selectionMode")];
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			cooldownTimer++;

			if (cooldownTimer >= 10)
			{
				this.cooldownTimer = 0;
				project();
			}
		}
	}

	private void project()
	{
		IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);


		int projectedSlot = -1;


		switch (selectionMode)
		{
			case BIGGEST:
				int maxSize = 0;
				int maxSlot = -1;
				for (int i = 0; i < itemHandler.getSlots(); i++)
				{
					ItemStack is;
					if ((is = itemHandler.getStackInSlot(i)) != null)
					{
						if (is.stackSize > maxSize)
						{
							maxSize = is.stackSize;
							maxSlot = i;
						}
					}
				}
				projectedSlot = maxSlot;
				break;
			case FIRST:
				for (int i = 0; i < itemHandler.getSlots(); i++)
				{
					if (itemHandler.getStackInSlot(i) != null)
					{
						projectedSlot = i;
						break;
					}
				}
				break;
			case LAST:
				for (int i = itemHandler.getSlots() - 1; i >= 0; i--)
				{
					if (itemHandler.getStackInSlot(i) != null)
					{
						projectedSlot = i;
						break;
					}
				}
				break;
			default:
				break;

		}

		if (projectedSlot != -1)
		{
			ItemStack stack = itemHandler.extractItem(projectedSlot, 64, false);

			if (stack != null)
			{
				IBlockState state = TileEntityItemProjector.this.worldObj.getBlockState(TileEntityItemProjector.this.pos);

				EnumFacing facing = state.getValue(BlockItemProjector.FACING);

				EntityProjectedItem itemEntity = new EntityProjectedItem(TileEntityItemProjector.this.worldObj, TileEntityItemProjector.this.pos.getX() + 0.5 + facing.getFrontOffsetX() * 0.625, TileEntityItemProjector.this.pos.getY() + 0.5 + (facing == EnumFacing.UP ? 0.6 : (facing == EnumFacing.DOWN ? -0.75 : facing.getFrontOffsetY() * 0.625)), TileEntityItemProjector.this.pos.getZ() + 0.5 + facing.getFrontOffsetZ() * 0.625, stack.copy(), facing);
				itemEntity.setLifeSpan(500);

				TileEntityItemProjector.this.worldObj.spawnEntityInWorld(itemEntity);
			}
		}
	}

	public void rotateSelectionMode()
	{
		this.selectionMode = RandomUtil.rotateEnum(this.selectionMode);
	}

}
