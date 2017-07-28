package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotGhostItemHandler;
import lumien.randomthings.container.slots.SlotGhostItemHandlerStacked;
import lumien.randomthings.tileentity.TileEntityInventoryTester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerInventoryTester extends ContainerTE<TileEntityInventoryTester>
{

	public ContainerInventoryTester(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
		
		IItemHandler itemHandler = te.getItemHandler();
		
		this.addSlotToContainer(new SlotGhostItemHandlerStacked(itemHandler, 0, 64, 18));
		
		IItemHandler playerItemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new SlotItemHandler(playerItemHandler, i1 + k * 9 + 9, 8 + i1 * 18, 54 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new SlotItemHandler(playerItemHandler, l, 8 + l * 18, 112));
		}
	}

	@Override
	public void signal(int signal)
	{
		if (signal == 0)
		{
			this.te.toggleInvert();
		}
	}

}
