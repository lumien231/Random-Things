package lumien.randomthings.tileentity;

import lumien.randomthings.block.BlockInventoryTester;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.ContainerSynced;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileEntityInventoryTester extends TileEntityBase implements ITickable
{
	int counter = 0;

	boolean emitRedstone = false;

	@ContainerSynced
	boolean invertSignal = false;

	public TileEntityInventoryTester()
	{
		this.setItemHandler(1);
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setBoolean("emitRedstone", emitRedstone);
		compound.setBoolean("invertSignal", invertSignal);
		compound.setInteger("counter", counter);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.emitRedstone = compound.getBoolean("emitRedstone");
		this.invertSignal = compound.getBoolean("invertSignal");
		this.counter = compound.getInteger("counter");
	}

	@Override
	public void update()
	{
		if (!world.isRemote && ++counter == 2)
		{
			counter = 0;
			IItemHandler me = getItemHandler();
			ItemStack testStack = me.getStackInSlot(0);

			if (!testStack.isEmpty())
			{
				EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockInventoryTester.FACING);
				TileEntity te = this.world.getTileEntity(this.pos.offset(facing.getOpposite()));

				if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()))
				{
					IItemHandler target = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
					ItemStack testResult = ItemHandlerHelper.insertItemStacked(target, testStack, true);
					boolean newRedstone = testResult.isEmpty();

					if (invertSignal)
					{
						newRedstone = !newRedstone;
					}

					if (newRedstone != emitRedstone)
					{
						this.emitRedstone = newRedstone;

						this.syncTE();
						this.world.notifyNeighborsOfStateChange(pos, ModBlocks.inventoryTester, false);
					}
				}
			}
		}
	}

	public boolean isPowered()
	{
		return emitRedstone;
	}

	public void toggleInvert()
	{
		this.invertSignal = !this.invertSignal;
		this.markDirty();
	}

}
