package lumien.randomthings.lib;

import net.minecraft.block.Block;

public interface INoItem
{
	public default boolean hasNoItem()
	{
		return true;
	}
}
