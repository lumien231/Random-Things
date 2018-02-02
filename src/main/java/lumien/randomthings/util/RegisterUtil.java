package lumien.randomthings.util;

import lumien.randomthings.RandomThings;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class RegisterUtil
{

	public static void registerItem(Item i, String name)
	{
		i.setRegistryName(new ResourceLocation("randomthings", name));
		i.setUnlocalizedName(name);
		i.setCreativeTab(RandomThings.instance.creativeTab);
		ForgeRegistries.ITEMS.register(i);
	}
}
