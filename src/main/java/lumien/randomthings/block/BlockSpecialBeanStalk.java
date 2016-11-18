package lumien.randomthings.block;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockSpecialBeanStalk extends BlockBase
{

	protected BlockSpecialBeanStalk()
	{
		super("specialBeanStalk", Material.ROCK);

		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		return;
	}

}
