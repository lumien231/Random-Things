package lumien.randomthings.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOfSticks extends ItemBlock
{

	public ItemBlockOfSticks(Block block)
	{
		super(block);

		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String type = "error";

		switch (stack.getItemDamage())
		{
		case 0:
			type = "normal";
			break;
		case 1:
			type = "returning";
			break;
		}
		return super.getUnlocalizedName() + "." + type;
	}
}
