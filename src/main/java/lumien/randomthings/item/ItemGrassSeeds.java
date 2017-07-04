package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.lib.IRTItemColor;
import net.minecraft.block.BlockDirt;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGrassSeeds extends ItemBase implements IRTItemColor
{
	public ItemGrassSeeds()
	{
		super("grassSeeds");

		this.setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (int i = 0; i < 17; i++)
			{
				subItems.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		if (stack.getItemDamage() == 0)
		{
			return super.getUnlocalizedName() + ".normal";
		}
		else
		{
			return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata() - 1).getUnlocalizedName();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemstack(ItemStack stack, int renderPass)
	{
		if (stack.getItemDamage() == 0)
		{
			return 3512880;
		}
		else
		{
			return ItemDye.DYE_COLORS[EnumDyeColor.byMetadata(stack.getItemDamage() - 1).getDyeDamage()];
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (!worldIn.isRemote)
		{
			if (worldIn.getBlockState(pos).getBlock() instanceof BlockDirt && worldIn.getBlockState(pos).getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT)
			{
				stack.shrink(1);
				if (stack.getItemDamage() == 0)
				{
					worldIn.setBlockState(pos, Blocks.GRASS.getDefaultState());
				}
				else
				{
					worldIn.setBlockState(pos, ModBlocks.coloredGrass.getStateFromMeta(stack.getItemDamage() - 1));
				}

				return EnumActionResult.SUCCESS;
			}

			return EnumActionResult.FAIL;
		}
		return EnumActionResult.SUCCESS;
	}
}
