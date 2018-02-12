package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityBlockDestabilizer;
import lumien.randomthings.tileentity.TileEntityIronDropper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBlockDestabilizer extends ContainerTE<TileEntityBlockDestabilizer>
{
	public ContainerBlockDestabilizer(EntityPlayer player, World world, int x, int y, int z)
	{
		super(player, world, x, y, z);
	}

	@Override
	public void signal(int signal)
	{
		switch (signal)
		{
			case 0:
				te.toggleLazy();
				break;
			case 1:
				te.resetLazy();
				break;
		}
	}
}
