package lumien.randomthings.tileentity;

import lumien.randomthings.block.plates.BlockProcessingPlate;
import lumien.randomthings.lib.ContainerSynced;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileEntityProcessingPlate extends TileEntityBase implements ITickable
{
	@ContainerSynced
	EnumFacing insertFacing = EnumFacing.UP;

	@ContainerSynced
	EnumFacing extractFacing = EnumFacing.DOWN;

	public EnumFacing getInsertFacing()
	{
		return insertFacing;
	}

	public EnumFacing getExtractFacing()
	{
		return extractFacing;
	}

	public void rotateExtractFacing()
	{
		int newIndex = extractFacing.ordinal() + 1;

		if (newIndex >= EnumFacing.VALUES.length)
		{
			newIndex = 0;
		}
		
		this.extractFacing = EnumFacing.VALUES[newIndex];
	}
	
	public void rotateInsertFacing()
	{
		int newIndex = insertFacing.ordinal() + 1;

		if (newIndex >= EnumFacing.VALUES.length)
		{
			newIndex = 0;
		}
		
		this.insertFacing = EnumFacing.VALUES[newIndex];
	}
	
	@Override
	public boolean syncAdditionalData()
	{
		return false;
	}
	
	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setInteger("extractFacing", extractFacing.ordinal());
		compound.setInteger("insertFacing", insertFacing.ordinal());
	}
	
	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.extractFacing = EnumFacing.VALUES[compound.getInteger("extractFacing")];
		this.insertFacing = EnumFacing.VALUES[compound.getInteger("insertFacing")];
	}

	@Override
	public void update()
	{
		if (!this.world.isRemote && this.world.getTotalWorldTime() % 10 == 0)
		{
			TileEntity targetTE = this.world.getTileEntity(this.pos.down());

			if (targetTE != null && targetTE.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, extractFacing))
			{
				IItemHandler itemHandler = targetTE.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, extractFacing);

				if (itemHandler != null)
				{
					for (int slot = 0; slot < itemHandler.getSlots(); slot++)
					{
						ItemStack extracted = itemHandler.extractItem(slot, 64, false);

						if (!extracted.isEmpty())
						{
							EnumFacing outputFacing = this.world.getBlockState(this.pos).getValue(BlockProcessingPlate.OUTPUT_FACING);

							Vec3d facingVec = new Vec3d(outputFacing.getDirectionVec()).scale(0.53).add(new Vec3d(pos).addVector(0.5, 0, 0.5));

							Vec3d outputMotionVec = new Vec3d(outputFacing.getDirectionVec()).scale(0.1);

							EntityItem entityItem = new EntityItem(this.world, facingVec.x, facingVec.y, facingVec.z);

							entityItem.motionX = outputMotionVec.x;
							entityItem.motionY = outputMotionVec.y;
							entityItem.motionZ = outputMotionVec.z;
							
							entityItem.setPickupDelay(10);

							entityItem.setItem(extracted);

							this.world.spawnEntity(entityItem);

							break;
						}
					}
				}
			}
		}
	}
}
