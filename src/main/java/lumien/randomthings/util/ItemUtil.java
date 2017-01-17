package lumien.randomthings.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemUtil
{
	public static boolean areItemStackContentEqual(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}

		if (is1.getItem() != is2.getItem())
		{
			return false;
		}

		if (!ItemStack.areItemStackTagsEqual(is1, is2))
		{
			return false;
		}

		return is1.getItemDamage() == is2.getItemDamage();
	}

	public static boolean areOreDictionaried(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}
		int[] ids1 = OreDictionary.getOreIDs(is1);
		int[] ids2 = OreDictionary.getOreIDs(is2);

		for (int id1 : ids1)
		{
			for (int id2 : ids2)
			{
				if (id1 == id2)
				{
					return true;
				}
			}
		}

		return false;
	}
}
