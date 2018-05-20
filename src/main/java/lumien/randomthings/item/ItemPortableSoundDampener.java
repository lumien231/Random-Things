package lumien.randomthings.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import lumien.randomthings.RandomThings;
import lumien.randomthings.container.inventories.InventoryItem;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemPortableSoundDampener extends ItemBase implements IBauble
{

	public ItemPortableSoundDampener()
	{
		super("portableSoundDampener");

		this.setMaxStackSize(1);
	}

	public static InventoryItem getInventory(ItemStack dampener)
	{
		return new InventoryItem("inventory", 9, dampener);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote && handIn == EnumHand.MAIN_HAND)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.PORTABLE_SOUND_DAMPENER, worldIn, 0, 0, 0);

			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.BODY;
	}

}
