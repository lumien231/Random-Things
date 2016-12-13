package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlazeAndSteel extends ItemBase
{

	public ItemBlazeAndSteel()
	{
		super("blazeAndSteel");

		this.setMaxStackSize(1);
		this.setMaxDamage(64);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		pos = pos.offset(facing);

		if (!playerIn.canPlayerEdit(pos, facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			if (worldIn.isAirBlock(pos))
			{
				worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.8F + 0.8F);
				worldIn.setBlockState(pos, ModBlocks.blazingFire.getDefaultState(), 11);
			}

			stack.damageItem(1, playerIn);
			return EnumActionResult.SUCCESS;
		}
	}
}
