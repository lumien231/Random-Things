package lumien.randomthings.util;

import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.ArrayUtils;

import lumien.randomthings.recipes.ModRecipes;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DyeUtil
{
	// Returns the color of a dye using ore dictionary
	public static int getDyeColor(ItemStack dyeStack)
	{
		HashSet<Integer> oreDictIds = new HashSet<>();
		oreDictIds.addAll(Arrays.asList(ArrayUtils.toObject(OreDictionary.getOreIDs(dyeStack))));

		for (int i = 0; i < ModRecipes.oreDictDyes.length; i++)
		{
			String dyeString = ModRecipes.oreDictDyes[i];
			int id = OreDictionary.getOreID(dyeString);
			if (oreDictIds.contains(id))
			{
				return ItemDye.DYE_COLORS[15 - i];
			}
		}

		return 0;
	}

	public static boolean isVanillaDye(ItemStack dyeStack)
	{
		HashSet<Integer> oreDictIds = new HashSet<>();
		oreDictIds.addAll(Arrays.asList(ArrayUtils.toObject(OreDictionary.getOreIDs(dyeStack))));

		for (int i = 0; i < ModRecipes.oreDictDyes.length; i++)
		{
			String dyeString = ModRecipes.oreDictDyes[i];
			int id = OreDictionary.getOreID(dyeString);
			if (oreDictIds.contains(id))
			{
				return true;
			}
		}

		return false;
	}
}
