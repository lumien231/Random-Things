package lumien.randomthings.item;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.handler.runes.EnumRuneDust;
import lumien.randomthings.lib.IRTItemColor;
import lumien.randomthings.tileentity.TileEntityRuneBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRuneDust extends ItemBase implements IRTItemColor
{

	public ItemRuneDust()
	{
		super("runeDust");

		this.setHasSubtypes(true);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && facing == EnumFacing.UP)
		{
			ItemStack me = player.getHeldItem(hand);

			IBlockState targetState = worldIn.getBlockState(pos);
			TileEntityRuneBase te = null;

			if (targetState.getBlock() == ModBlocks.runeBase)
			{
				te = (TileEntityRuneBase) worldIn.getTileEntity(pos);
			}
			else
			{
				if (targetState.isSideSolid(worldIn, pos, EnumFacing.UP))
				{
					BlockPos replacePos = pos.offset(facing);
					IBlockState toReplace = worldIn.getBlockState(replacePos);

					if (toReplace.getBlock().isAir(toReplace, worldIn, replacePos) || toReplace.getBlock().isReplaceable(worldIn, replacePos))
					{
						worldIn.setBlockState(replacePos, ModBlocks.runeBase.getDefaultState());

						te = (TileEntityRuneBase) worldIn.getTileEntity(replacePos);
					}
				}
			}

			if (te != null)
			{
				int[][] runeData = te.getRuneData();

				int x = (int) Math.floor(hitX * 4);
				int y = (int) Math.floor(hitZ * 4);

				if (runeData[x][y] == -1)
				{
					runeData[x][y] = me.getItemDamage();
					te.syncTE();

					if (!player.capabilities.isCreativeMode)
					{
						me.shrink(1);
					}

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			int damage = 0;
			for (EnumRuneDust runeType : EnumRuneDust.values())
			{
				subItems.add(new ItemStack(this, 1, damage));
				damage++;
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		EnumRuneDust runeType = EnumRuneDust.values()[stack.getItemDamage()];

		return super.getUnlocalizedName(stack) + "." + runeType.getName();
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex)
	{
		return EnumRuneDust.getColor(stack.getItemDamage());
	}
}
