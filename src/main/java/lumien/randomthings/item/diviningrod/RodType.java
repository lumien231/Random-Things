package lumien.randomthings.item.diviningrod;

import java.awt.Color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RodType
{
	String name;

	public RodType(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public abstract boolean matches(World worldObj, BlockPos pos, IBlockState state);
	
	public Color getIndicatorColor(World worldObj, BlockPos pos, IBlockState state)
	{
		return getItemColor();
	}
	
	public abstract Color getItemColor();
	
	public abstract boolean shouldBeAvailable();
}
