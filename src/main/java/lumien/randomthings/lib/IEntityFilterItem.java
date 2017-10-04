package lumien.randomthings.lib;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IEntityFilterItem
{
	public boolean apply(ItemStack me, Entity entity);
}
