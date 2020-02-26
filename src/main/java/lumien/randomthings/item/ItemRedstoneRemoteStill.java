package lumien.randomthings.item;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRedstoneRemoteStill extends ItemBase{
    public ItemRedstoneRemoteStill()
    {
        super("redstoneRemoteStill");

        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!worldIn.isRemote)
        {
            if (playerIn.isSneaking())
            {
                playerIn.openGui(RandomThings.instance, GuiIds.REDSTONE_REMOTE_STILL_EDIT, worldIn, 0, 0, 0);
            }
            else
            {
                playerIn.openGui(RandomThings.instance, GuiIds.REDSTONE_REMOTE_STILL_USE, worldIn, 0, 0, 0);
            }
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }
}
