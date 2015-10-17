package lumien.randomthings.item.block;

import lumien.randomthings.block.BlockBiomeStone;
import lumien.randomthings.block.BlockPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockPlatform extends ItemBlock
{
	public ItemBlockPlatform(Block block)
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
		return super.getUnlocalizedName() + "." + BlockPlatform.EnumType.byMetadata(stack.getItemDamage()).getUnlocalizedName();
	}
}
