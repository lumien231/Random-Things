package lumien.randomthings.item.diviningrod;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class OreRodType extends RodType
{
	String oreName;

	int oreID;
	Color color;

	public OreRodType(String name, String ore, Color color)
	{
		super(name);

		this.oreName = ore;
		this.oreID = OreDictionary.getOreID(ore);
		this.color = color;
	}

	@Override
	public boolean matches(World worldObj, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() != Blocks.AIR)
		{
			Item item = Item.getItemFromBlock(state.getBlock());

			int meta = state.getBlock().getMetaFromState(state);
			if (item != null)
			{
				ItemStack stack = new ItemStack(item, 1, meta);

				if (!stack.isEmpty())
				{
					int[] ids = OreDictionary.getOreIDs(stack);

					for (int i : ids)
					{
						if (i == oreID)
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public boolean shouldBeAvailable()
	{
		return !OreDictionary.getOres(oreName).isEmpty();
	}

	@Override
	public Color getItemColor()
	{
		return color;
	}

}
