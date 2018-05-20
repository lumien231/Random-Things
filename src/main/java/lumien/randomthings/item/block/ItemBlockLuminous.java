package lumien.randomthings.item.block;

import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.lib.ILuminousItem;
import mezz.jei.api.ingredients.ISlowRenderItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mezz.jei.api.ingredients.ISlowRenderItem", modid = "jei")
public class ItemBlockLuminous extends ItemBlock implements ILuminousItem, ISlowRenderItem
{

	public ItemBlockLuminous(Block block)
	{
		super(block);
	}

	@Override
	public boolean shouldGlow(ItemStack stack, int tintIndex)
	{
		ILuminousBlock myBlock = (ILuminousBlock) this.block;
		return myBlock.shouldGlow(block.getStateFromMeta(stack.getItemDamage()), tintIndex);
	}
}
