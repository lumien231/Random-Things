package lumien.randomthings.item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.handler.runes.EnumRuneDust;
import lumien.randomthings.tileentity.TileEntityRuneBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRunePattern extends ItemBase
{

	public ItemRunePattern()
	{
		super("runePattern");

		this.setMaxStackSize(1);
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, world, tooltip, advanced);

		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("runeData"))
		{
			int[] runeData = stack.getTagCompound().getIntArray("runeData");

			int[] amount = new int[EnumRuneDust.values().length];

			for (int i = 0; i < runeData.length; i++)
			{
				if (runeData[i] != -1)
				{
					amount[runeData[i]]++;
				}
			}

			ArrayList<Pair<EnumRuneDust, Integer>> list = new ArrayList<Pair<EnumRuneDust, Integer>>();
			for (int i = 0; i < amount.length; i++)
			{
				if (amount[i] != 0)
				{
					list.add(Pair.of(EnumRuneDust.values()[i], amount[i]));
				}
			}

			list.sort(new Comparator<Pair<EnumRuneDust, Integer>>()
			{
				@Override
				public int compare(Pair<EnumRuneDust, Integer> o1, Pair<EnumRuneDust, Integer> o2)
				{
					return o1.getRight() - o2.getRight();
				}
			});

			for (Pair<EnumRuneDust, Integer> element : Lists.reverse(list))
			{
				tooltip.add("- " + element.getRight() + "x " + I18n.format("item.runeDust." + element.getLeft().getName() + ".name"));
			}
		}
		else
		{
			tooltip.add(I18n.format("tooltip.general.empty"));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (playerIn.isSneaking())
		{
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.PAPER));
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		if (worldIn.isAirBlock(pos.up()) && state.isSideSolid(worldIn, pos, EnumFacing.UP))
		{
			ItemStack me = player.getHeldItem(hand);

			if (me.getTagCompound() != null)
			{
				int[] runeData = me.getTagCompound().getIntArray("runeData");

				int[][] actualRuneData = new int[4][4];

				boolean anyThing = player.capabilities.isCreativeMode;

				for (int i = 0; i < runeData.length; i++)
				{
					int x = i % 4;
					int y = i / 4;
					int runeType = runeData[i];

					if (runeType != -1 && !player.capabilities.isCreativeMode)
					{
						boolean available = false;
						for (int s = 0; s < player.inventory.getSizeInventory(); s++)
						{
							ItemStack stack = player.inventory.getStackInSlot(s);

							if (!stack.isEmpty() && stack.getItem() == ModItems.runeDust && stack.getItemDamage() == runeType)
							{
								if (!worldIn.isRemote)
								{
									stack.shrink(1);
								}
								available = true;
							}
						}

						if (!available)
						{
							runeType = -1;
						}
						else
						{
							anyThing = true;
						}
					}

					actualRuneData[x][y] = runeType;
				}

				if (anyThing)
				{
					if (!worldIn.isRemote)
					{
						worldIn.setBlockState(pos.up(), ModBlocks.runeBase.getDefaultState());
						TileEntityRuneBase te = (TileEntityRuneBase) worldIn.getTileEntity(pos.up());
						te.setRuneData(actualRuneData);
					}

					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.FAIL;
	}
}
