package lumien.randomthings.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSpecialChest extends ItemBlock
{
	public ItemBlockSpecialChest(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String type = "error";

		switch (stack.getItemDamage())
		{
		case 0:
			type = "nature";
			break;
		case 1:
			type = "water";
			break;
		}
		return super.getUnlocalizedName() + "." + type;
	}
}
