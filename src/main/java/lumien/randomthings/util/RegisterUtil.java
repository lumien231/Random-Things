package lumien.randomthings.util;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterUtil
{

	public static void registerItem(Item i, String name)
	{
		i.setRegistryName(new ResourceLocation("randomthings", name));
		i.setUnlocalizedName(name);
		i.setCreativeTab(RandomThings.instance.creativeTab);
		GameRegistry.register(i);
	}
}
