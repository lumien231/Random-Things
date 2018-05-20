package lumien.randomthings.lib;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class EntityFilterItemStack
{
	ItemStack is;
	IEntityFilterItem filterInstance;

	public EntityFilterItemStack(ItemStack is, IEntityFilterItem filterInstance)
	{
		this.is = is;
		this.filterInstance = filterInstance;
	}

	public boolean apply(Entity e)
	{
		return filterInstance.apply(is, e);
	}
}
