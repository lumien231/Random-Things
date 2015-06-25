package lumien.randomthings.item;

import lumien.randomthings.block.BlockLifeAnchor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
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

				compound.setInteger("dimension", worldIn.provider.getDimensionId());
				compound.setInteger("targetX", pos.getX());
				compound.setInteger("targetY", pos.getY());
				compound.setInteger("targetZ", pos.getZ());
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
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
					worldIn.playSoundAtEntity(playerIn, "randomthings:linkingOrbEnable", 1, 4);
				}
				else
				{
					worldIn.playSoundAtEntity(playerIn, "randomthings:linkingOrbEnable", 1, 0.9f);
				}
			}
		}
		return stack;
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
