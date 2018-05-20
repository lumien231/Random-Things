package lumien.randomthings.container;

import lumien.randomthings.container.slots.SlotGhostItemHandlerStacked;
import lumien.randomthings.tileentity.TileEntityInventoryTester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if (slotId >= 0 && slotId < this.inventorySlots.size() && clickTypeIn == ClickType.PICKUP)
		{
			Slot s = this.getSlot(slotId);
			ItemStack is = s.getStack();
			int stackSize = is.getCount();

			if (!is.isEmpty() && s instanceof SlotGhostItemHandlerStacked)
			{
				if (!player.inventory.getItemStack().isEmpty())
				{
					return super.slotClick(slotId, dragType, clickTypeIn, player);
				}

				switch (dragType)
				{
				case 0:
					is.setCount(Math.min(64, stackSize * 2));
					return is;
				case 1:
					is.setCount((int) Math.floor(stackSize / 2));
					return is;
				}
			}
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);

	}
}
