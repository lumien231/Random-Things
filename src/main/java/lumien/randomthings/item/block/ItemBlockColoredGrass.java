package lumien.randomthings.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockColoredGrass extends ItemBlock
{

	public ItemBlockColoredGrass(Block block)
	{
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return ItemDye.dyeColors[EnumDyeColor.byMetadata(stack.getItemDamage()).getDyeDamage()];
	}
	
	@Override
	public int getMetadata(int damage)
    {
        return damage;
    }
	
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }
}
