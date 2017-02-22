package lumien.randomthings.lib;

import net.minecraft.block.state.IBlockState;

public interface ILuminousBlock
{
	public boolean shouldGlow(IBlockState state, int tintIndex);
}
