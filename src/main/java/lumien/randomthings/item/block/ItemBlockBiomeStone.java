package lumien.randomthings.item.block;

import lumien.randomthings.block.BlockBiomeStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockBiomeStone extends ItemBlock
{
    public ItemBlockBiomeStone(Block block)
	{
		super(block);
	}

	@Override
	public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + BlockBiomeStone.EnumType.byMetadata(stack.getItemDamage()).getUnlocalizedName();
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return this.block.getBlockColor();
	}
}
