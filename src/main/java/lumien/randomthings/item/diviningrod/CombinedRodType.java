package lumien.randomthings.item.diviningrod;

import java.awt.Color;

import lumien.randomthings.handler.RTEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;

public class CombinedRodType extends RodType
{
	RodType[] parts;

	public CombinedRodType(String name, RodType... parts)
	{
		super(name);
	
		this.parts = parts;
	}

	@Override
	public boolean matches(World worldObj, BlockPos pos, IBlockState state)
	{
		for (RodType rt:parts)
		{
			if (rt.matches(worldObj, pos, state))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Color getIndicatorColor(World worldObj, BlockPos pos, IBlockState state)
	{
		for (RodType rt:parts)
		{
			if (rt.matches(worldObj, pos, state))
			{
				return rt.getIndicatorColor(worldObj, pos, state);
			}
		}
		
		return Color.BLACK;
	}

	@Override
	public boolean shouldBeAvailable()
	{
		for (RodType rt:parts)
		{
			if (rt.shouldBeAvailable())
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Color getItemColor()
	{
		return new Color(Color.HSBtoRGB(((RTEventHandler.clientAnimationCounter + Animation.getPartialTickTime()) / 1000F) % 1.0F, 1, 1));
	}

}
