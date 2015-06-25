package lumien.randomthings.util;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterUtil
{
	public static void registerBlock(Block b, String name)
	{
		b.setUnlocalizedName(name);
		b.setCreativeTab(RandomThings.instance.creativeTab);
		GameRegistry.registerBlock(b, name);
	}

	public static void registerItem(Item i, String name)
	{
		i.setUnlocalizedName(name);
		i.setCreativeTab(RandomThings.instance.creativeTab);
		GameRegistry.registerItem(i, name);
	}
}
