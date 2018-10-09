package lumien.randomthings.client.render.magiccircles;

import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

import lumien.randomthings.util.client.ColorUtil;

public class ColorFunctions
{
	public static IColorFunction constant(Color cst)
	{
		return (p, t, c) -> {
			return cst;
		};
	}

	public static IColorFunction alternate(Color c1, Color c2)
	{
		return (p, t, c) -> {
			return t % 2 == 0 ? c2 : c1;
		};
	}

	public static IColorFunction alternateN(Color c1, Color c2, int n, int offset)
	{
		return (p, t, c) -> {
			return (t + offset) % n == 0 ? c2 : c1;
		};
	}

	public static IColorFunction flicker(long rngMod, int slow)
	{
		return (p, t, c) -> {
			Random rng = new Random((long) ((t * t * t * t + p + rngMod) / slow));

			if (rng.nextInt(3) == 0)
			{
				return ColorUtil.brighter(c, 20f);
			}

			return c;
		};
	}

	public static IColorFunction limit(IColorFunction original, Function<Integer, Boolean> limitFunction)
	{
		return (p, t, c) -> {
			if (limitFunction.apply(t))
			{
				return original.apply(p, t, c);
			}

			return c;
		};
	}
}
