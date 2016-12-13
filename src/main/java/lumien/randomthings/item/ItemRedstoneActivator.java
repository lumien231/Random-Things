package lumien.randomthings.item;

import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRedstoneActivator extends ItemBase
{

	public ItemRedstoneActivator()
	{
		super("redstoneActivator");

		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			RedstoneSignalHandler.getHandler().addSignal(worldIn, pos, 20, 15);

			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.PASS;
		}
	}
}
