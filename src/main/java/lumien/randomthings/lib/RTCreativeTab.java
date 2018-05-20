package lumien.randomthings.lib;

import lumien.randomthings.item.ModItems;
import lumien.randomthings.potion.ModPotions;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
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

	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> p_78018_1_)
	{
		super.displayAllRelevantItems(p_78018_1_);

		p_78018_1_.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), ModPotions.collapseType));
		p_78018_1_.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), ModPotions.collapseTypeLong));
		p_78018_1_.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), ModPotions.collapseTypeStrong));
	}
}
