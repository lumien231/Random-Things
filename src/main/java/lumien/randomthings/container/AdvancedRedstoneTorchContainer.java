package lumien.randomthings.container;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.tileentity.AdvancedRedstoneTorchTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;

/**
 * AdvancedRedstoneTorchContainer
 */
public class AdvancedRedstoneTorchContainer extends Container implements ISignalContainer
{
	IWorldPosCallable pos;

	public IntReferenceHolder strengthGreen = IntReferenceHolder.single();
	public IntReferenceHolder strengthRed = IntReferenceHolder.single();

	AdvancedRedstoneTorchTileEntity te;

	public AdvancedRedstoneTorchContainer(int windowId, IInventory playerInventory, PacketBuffer extraData)
	{
		this(windowId, IWorldPosCallable.DUMMY);
	}

	public AdvancedRedstoneTorchContainer(int windowId, IWorldPosCallable pos)
	{
		super(ModContainerTypes.ADVANCED_REDSTONE_TORCH, windowId);

		this.pos = pos;

		this.trackInt(strengthGreen);
		this.trackInt(strengthRed);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn)
	{
		return isWithinUsableDistance(this.pos, playerIn, ModBlocks.ADVANCED_REDSTONE_TORCH) || isWithinUsableDistance(this.pos, playerIn, ModBlocks.ADVANCED_WALL_REDSTONE_TORCH);
	}

	@Override
	public void detectAndSendChanges()
	{
		this.pos.consume((world, pos) -> {
			TileEntity te = world.getTileEntity(pos);

			if (te instanceof AdvancedRedstoneTorchTileEntity)
			{
				AdvancedRedstoneTorchTileEntity art = (AdvancedRedstoneTorchTileEntity) te;

				this.strengthGreen.set(art.signalStrengthGreen());
				this.strengthRed.set(art.signalStrengthRed());
			}
		});

		super.detectAndSendChanges();
	}

	@Override
	public void handle(int id, PacketBuffer data)
	{
		if (id == 0)
		{
			int action = data.readInt();

			this.pos.consume((world, pos) -> {
				TileEntity te = world.getTileEntity(pos);

				if (te instanceof AdvancedRedstoneTorchTileEntity)
				{
					AdvancedRedstoneTorchTileEntity art = (AdvancedRedstoneTorchTileEntity) te;

					switch (action)
					{
						case 0: // Decrease Green Strength
							art.setSignalStrengthGreen(Math.max(0, art.signalStrengthGreen() - 1));
							break;
						case 1: // Increase Green Strength
							art.setSignalStrengthGreen(Math.min(15, art.signalStrengthGreen() + 1));
							break;
						case 2: // Decrease Red Strength
							art.setSignalStrengthRed(Math.max(0, art.signalStrengthRed() - 1));
							break;
						case 3: // Increase Red Strength
							art.setSignalStrengthRed(Math.min(15, art.signalStrengthRed() + 1));
							break;
					}

					art.getWorld().notifyNeighbors(art.getPos(), art.getBlockState().getBlock());
				}
			});
		}
	}

}