package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemBean extends ItemBase
{
	public ItemBean()
	{
		super("beans");

		this.setHasSubtypes(true);

		OreDictionary.registerOre("cropBean", new ItemStack(this, 1, 0));
		OreDictionary.registerOre("listAllveggie", new ItemStack(this, 1, 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (int i = 0; i < 3; i++)
			{
				subItems.add(new ItemStack(this, 1, i));
			}
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
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (side != EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		}
		else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
		{
			return EnumActionResult.FAIL;
		}
		else if (worldIn.isAirBlock(pos.up()))
		{
			if (stack.getItemDamage() == 0 && ModBlocks.beanSprout.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.beanSprout.getDefaultState());

				worldIn.playSound(null, pos.up(), ModBlocks.beanSprout.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);

				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
			else if (stack.getItemDamage() == 1 && ModBlocks.beanStalk.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.lesserBeanStalk.getDefaultState());
				worldIn.scheduleUpdate(pos.up(), ModBlocks.lesserBeanStalk, 20);

				worldIn.playSound(null, pos.up(), ModBlocks.beanStalk.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);

				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
			else if (stack.getItemDamage() == 2 && ModBlocks.beanStalk.canPlaceBlockAt(worldIn, pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.beanStalk.getDefaultState());
				worldIn.scheduleUpdate(pos.up(), ModBlocks.beanStalk, 20);

				worldIn.playSound(null, pos.up(), ModBlocks.beanStalk.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1, 1);

				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
		else
		{
			return EnumActionResult.FAIL;
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
