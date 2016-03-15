package lumien.randomthings.item;

import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemRedstoneActivator extends ItemBase
{

	public ItemRedstoneActivator()
	{
		super("redstoneActivator");
		
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			RedstoneSignalHandler.getHandler().addSignal(worldIn, pos, 20, 15);
			return true;
		}
		else
		{
			return false;
		}
	}
}
