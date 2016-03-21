package lumien.randomthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerCustomWorkbench extends ContainerWorkbench
{

	public ContainerCustomWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos p_i45800_3_)
	{
		super(playerInventory, worldIn, p_i45800_3_);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}
}
