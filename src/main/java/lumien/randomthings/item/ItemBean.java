package lumien.randomthings.item;

import java.util.List;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBean extends ItemBase
{
	public ItemBean()
	{
		super("beans");

		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
		for (int i = 0; i < 3; i++)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
			case 0:
				return "item.bean";
			case 1:
				return "item.lesserMagicBean";
			case 2:
				return "item.magicBean";
		}
		return "item.bean.name";
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side != EnumFacing.UP)
		{
			return false;
		}
		else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
		{
			return false;
		}
		else if (worldIn.isAirBlock(pos.up()))
		{
			if (stack.getItemDamage() == 0 && ModBlocks.beanSprout.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.beanSprout.getDefaultState());

				worldIn.playSoundEffect(pos.getX(), pos.up().getY(), pos.getZ(), ModBlocks.beanSprout.stepSound.getPlaceSound(), 1, 1);

				--stack.stackSize;
				return true;
			}
			else if (stack.getItemDamage() == 1 && ModBlocks.beanStalk.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.lesserBeanStalk.getDefaultState());
				worldIn.scheduleUpdate(pos.up(), ModBlocks.lesserBeanStalk, 20);

				worldIn.playSoundEffect(pos.getX(), pos.up().getY(), pos.getZ(), ModBlocks.beanStalk.stepSound.getPlaceSound(), 1, 1);

				--stack.stackSize;
				return true;
			}
			else if (stack.getItemDamage() == 2 && ModBlocks.beanStalk.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.beanStalk.getDefaultState());
				worldIn.scheduleUpdate(pos.up(), ModBlocks.beanStalk, 20);

				worldIn.playSoundEffect(pos.getX(), pos.up().getY(), pos.getZ(), ModBlocks.beanStalk.stepSound.getPlaceSound(), 1, 1);

				--stack.stackSize;
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		if (stack.getItemDamage() == 2)
		{
			return EnumRarity.RARE;
		}
		else
		{
			return EnumRarity.COMMON;
		}
	}
}
