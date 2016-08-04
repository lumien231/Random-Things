package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockIronDropper;
import lumien.randomthings.lib.ContainerSynced;
import lumien.randomthings.lib.IRedstoneSensitive;
import lumien.randomthings.util.RandomUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityIronDropper extends TileEntityBase implements IRedstoneSensitive, ITickable
{
	public enum REDSTONE_MODE
	{
		PULSE, REPEAT_POWERED, REPEAT;
	}

	public enum PICKUP_DELAY
	{
		NONE, TICKS_5, TICKS_20;
	}

	public enum EFFECTS
	{
		NONE, SOUND, PARTICLE, SOUND_PARTICLE;
	}

	@ContainerSynced
	REDSTONE_MODE redstoneMode = REDSTONE_MODE.REPEAT_POWERED;

	@ContainerSynced
	PICKUP_DELAY pickupDelay = PICKUP_DELAY.TICKS_5;
	
	@ContainerSynced
	EFFECTS effects = EFFECTS.NONE;

	@ContainerSynced
	boolean randomMotion;

	int dropCounter = 0;

	public TileEntityIronDropper()
	{
		this.setItemHandler(new ItemStackHandler(9)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				TileEntityIronDropper.this.markDirty();
			}
		});
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			dropCounter++;

			if (dropCounter % 4 == 0 && (redstoneMode == REDSTONE_MODE.REPEAT || (redstoneMode == REDSTONE_MODE.REPEAT_POWERED && isPoweredByRedstone())))
			{
				drop();
			}
		}
	}

	private void drop()
	{
		IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockIronDropper.FACING);
		BlockPos blockpos = pos.offset(facing);
		TileEntity tileEntity = worldObj.getTileEntity(blockpos);

		int slot = 0;
		ItemStack stack = null;
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			stack = itemHandler.getStackInSlot(i);

			if (stack != null)
			{
				slot = i;
				break;
			}
		}

		if (stack != null)
		{
			ItemStack toDrop = stack.copy();
			toDrop.stackSize = 1;

			if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
			{
				ItemStack result = ItemHandlerHelper.insertItemStacked(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()), toDrop, false);

				if (result == null || result.stackSize == 0)
				{
					itemHandler.extractItem(slot, 1, false);
				}
			}
			else
			{
				itemHandler.extractItem(slot, 1, false);

				double speed = 6;

				double posX = pos.getX() + 0.5 + 0.7D * facing.getFrontOffsetX();
				double posY = pos.getY() + 0.5 + 0.7D * facing.getFrontOffsetY();
				double posZ = pos.getZ() + 0.5 + 0.7D * facing.getFrontOffsetZ();

				if (facing.getAxis() == EnumFacing.Axis.Y)
				{
					posY = posY - 0.125D;
				}
				else
				{
					posY = posY - 0.15625D;
				}

				EntityItem entityItem = new EntityItem(worldObj, posX, posY, posZ, toDrop);

				int pickupDelayInteger;

				switch (pickupDelay)
				{
					case NONE:
						pickupDelayInteger = 0;
						break;
					case TICKS_20:
						pickupDelayInteger = 20;
						break;
					case TICKS_5:
						pickupDelayInteger = 5;
						break;
					default:
						pickupDelayInteger = 0;
						break;
				}

				entityItem.setPickupDelay(pickupDelayInteger);

				double d3;

				if (randomMotion)
				{
					d3 = worldObj.rand.nextDouble() * 0.1D + 0.2D;
				}
				else
				{
					d3 = 0.25D;
				}

				entityItem.motionX = facing.getFrontOffsetX() * d3;
				entityItem.motionY = 0.20000000298023224D;
				entityItem.motionZ = facing.getFrontOffsetZ() * d3;

				if (randomMotion)
				{
					entityItem.motionX += worldObj.rand.nextGaussian() * 0.007499999832361937D * speed;
					entityItem.motionY += worldObj.rand.nextGaussian() * 0.007499999832361937D * speed;
					entityItem.motionZ += worldObj.rand.nextGaussian() * 0.007499999832361937D * speed;
				}
				else
				{
					entityItem.motionX += facing.getFrontOffsetX() * 0.5 * 0.007499999832361937D * speed;
					entityItem.motionY += facing.getFrontOffsetY() * 0.5 * 0.007499999832361937D * speed;
					entityItem.motionZ += facing.getFrontOffsetZ() * 0.5 * 0.007499999832361937D * speed;
				}

				worldObj.spawnEntityInWorld(entityItem);
			}
			
			if (effects == EFFECTS.SOUND || effects == EFFECTS.SOUND_PARTICLE)
			{
				worldObj.playEvent(1000, this.pos, 0);
			}
			
			if (effects == EFFECTS.PARTICLE || effects == EFFECTS.SOUND_PARTICLE)
			{
				worldObj.playEvent(2000, this.pos, facing.getFrontOffsetX() + 1 + (facing.getFrontOffsetZ() + 1) * 3);
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		compound.setInteger("redstoneMode", redstoneMode.ordinal());
		compound.setInteger("dropCounter", dropCounter);
		compound.setInteger("pickupDelay", pickupDelay.ordinal());
		compound.setBoolean("randomMotion", randomMotion);
		compound.setInteger("effects", effects.ordinal());
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		this.redstoneMode = REDSTONE_MODE.values()[compound.getInteger("redstoneMode")];
		this.dropCounter = compound.getInteger("dropCounter");
		this.pickupDelay = PICKUP_DELAY.values()[compound.getInteger("pickupDelay")];
		this.randomMotion = compound.getBoolean("randomMotion");
		this.effects = EFFECTS.values()[compound.getInteger("effects")];
	}

	@Override
	public boolean writeNBTToDescriptionPacket()
	{
		return false;
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
		if (redstoneMode == REDSTONE_MODE.PULSE && newState)
		{
			drop();
		}
	}

	public REDSTONE_MODE getRedstoneMode()
	{
		return redstoneMode;
	}

	public void setRedstoneMode(REDSTONE_MODE redstoneMode)
	{
		this.redstoneMode = redstoneMode;
	}

	public void rotateRedstoneMode()
	{
		redstoneMode = RandomUtil.rotateEnum(redstoneMode);
	}

	public void rotatePickupDelay()
	{
		pickupDelay = RandomUtil.rotateEnum(pickupDelay);
	}

	public void rotateRandomMotion()
	{
		randomMotion = !randomMotion;
	}
	
	public void rotateEffects()
	{
		effects = RandomUtil.rotateEnum(effects);
	}
}