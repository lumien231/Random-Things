package lumien.randomthings.item;

import lumien.randomthings.block.BlockLifeAnchor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLinkingOrb extends ItemBase
{
	public ItemLinkingOrb()
	{
		super("linkingOrb");

		this.setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.getBlockState(pos).getBlock() instanceof BlockLifeAnchor)
		{
			if (!worldIn.isRemote)
			{
				NBTTagCompound compound;
				compound = stack.getTagCompound();
				if (compound == null)
				{
					stack.setTagCompound(compound = new NBTTagCompound());
				}

				compound.setInteger("dimension", worldIn.provider.getDimension());
				compound.setInteger("targetX", pos.getX());
				compound.setInteger("targetY", pos.getY());
				compound.setInteger("targetZ", pos.getZ());
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			NBTTagCompound compound;
			compound = stack.getTagCompound();
			if (compound == null)
			{
				stack.setTagCompound(compound = new NBTTagCompound());
			}
			if (compound.hasKey("targetX"))
			{
				boolean active = compound.getBoolean("active");
				compound.setBoolean("active", !active);
				if (compound.getBoolean("active"))
				{
					//worldIn.playSoundAtEntity(playerIn, "randomthings:linkingOrbEnable", 1, 4); TODO
				}
				else
				{
					// worldIn.playSoundAtEntity(playerIn, "randomthings:linkingOrbEnable", 1, 0.9f); TODO
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			return false;
		}
		NBTTagCompound compound = stack.getTagCompound();
		return compound.getBoolean("active");
	}
}
