package lumien.randomthings.lib;

import lumien.randomthings.item.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RTCreativeTab extends CreativeTabs
{
	public RTCreativeTab()
	{
		super("rt.name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModItems.magicHood);
	}

}
