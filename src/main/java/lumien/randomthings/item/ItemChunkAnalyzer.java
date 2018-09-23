package lumien.randomthings.item;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemChunkAnalyzer extends ItemBase
{

	public ItemChunkAnalyzer()
	{
		super("chunkAnalyzer");
		
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.CHUNK_ANALYZER, worldIn, 0, 0, 0);

			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
	}
}
