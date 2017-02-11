package lumien.randomthings.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockSpecialBeanStalk extends BlockBase
{

	protected BlockSpecialBeanStalk()
	{
		super("specialBeanStalk", Material.ROCK);

		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
	{
		return;
	}

}
