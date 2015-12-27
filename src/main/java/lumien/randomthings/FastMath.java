package lumien.randomthings;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.MathHelper;

public class FastMath
{
	private static final class Icecore
	{

		private static final int Size_Ac = 100000;
		private static final int Size_Ar = Size_Ac + 1;
		private static final float Pi = (float) Math.PI;
		private static final float Pi_H = Pi / 2;

		private static final float Atan2[] = new float[Size_Ar];
		private static final float Atan2_PM[] = new float[Size_Ar];
		private static final float Atan2_MP[] = new float[Size_Ar];
		private static final float Atan2_MM[] = new float[Size_Ar];

		private static final float Atan2_R[] = new float[Size_Ar];
		private static final float Atan2_RPM[] = new float[Size_Ar];
		private static final float Atan2_RMP[] = new float[Size_Ar];
		private static final float Atan2_RMM[] = new float[Size_Ar];

		static
		{
			for (int i = 0; i <= Size_Ac; i++)
			{
				double d = (double) i / Size_Ac;
				double x = 1;
				double y = x * d;
				float v = (float) Math.atan2(y, x);
				Atan2[i] = v;
				Atan2_PM[i] = Pi - v;
				Atan2_MP[i] = -v;
				Atan2_MM[i] = -Pi + v;

				Atan2_R[i] = Pi_H - v;
				Atan2_RPM[i] = Pi_H + v;
				Atan2_RMP[i] = -Pi_H + v;
				Atan2_RMM[i] = -Pi_H - v;
			}
		}

		public static final float atan2(float y, float x)
		{
			if (y < 0)
			{
				if (x < 0)
				{
					// (y < x) because == (-y > -x)
					if (y < x)
					{
						return Atan2_RMM[(int) (x / y * Size_Ac)];
					}
					else
					{
						return Atan2_MM[(int) (y / x * Size_Ac)];
					}
				}
				else
				{
					y = -y;
					if (y > x)
					{
						return Atan2_RMP[(int) (x / y * Size_Ac)];
					}
					else
					{
						return Atan2_MP[(int) (y / x * Size_Ac)];
					}
				}
			}
			else
			{
				if (x < 0)
				{
					x = -x;
					if (y > x)
					{
						return Atan2_RPM[(int) (x / y * Size_Ac)];
					}
					else
					{
						return Atan2_PM[(int) (y / x * Size_Ac)];
					}
				}
				else
				{
					if (y > x)
					{
						return Atan2_R[(int) (x / y * Size_Ac)];
					}
					else
					{
						return Atan2[(int) (y / x * Size_Ac)];
					}
				}
			}
		}
	}

	private static final class Riven
	{
		private static final int SIN_BITS, SIN_MASK, SIN_COUNT;
		private static final float radFull, radToIndex;
		private static final float degFull, degToIndex;
		private static final float[] sin, cos;

		static
		{
			SIN_BITS = 12;
			SIN_MASK = ~(-1 << SIN_BITS);
			SIN_COUNT = SIN_MASK + 1;

			radFull = (float) (Math.PI * 2.0);
			degFull = (float) (360.0);
			radToIndex = SIN_COUNT / radFull;
			degToIndex = SIN_COUNT / degFull;

			sin = new float[SIN_COUNT];
			cos = new float[SIN_COUNT];

			for (int i = 0; i < SIN_COUNT; i++)
			{
				sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
				cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
			}

			// Four cardinal directions (credits: Nate)
			for (int i = 0; i < 360; i += 90)
			{
				sin[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * Math.PI / 180.0);
				cos[(int) (i * degToIndex) & SIN_MASK] = (float) Math.cos(i * Math.PI / 180.0);
			}
		}

		public static final float sin(float rad)
		{
			return sin[(int) (rad * radToIndex) & SIN_MASK];
		}

		public static final float cos(float rad)
		{
			return cos[(int) (rad * radToIndex) & SIN_MASK];
		}
	}


	public static float sin(float rad)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return Riven.sin(rad);
		}
		else
		{
			return MathHelper.sin(rad);
		}
	}

	public static float cos(float rad)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return Riven.cos(rad);
		}
		else
		{
			return MathHelper.cos(rad);
		}
	}
	
	public static double sin(double rad)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return Riven.sin((float) rad);
		}
		else
		{
			return Math.sin(rad);
		}
	}

	public static double cos(double rad)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return Riven.cos((float) rad);
		}
		else
		{
			return Math.cos(rad);
		}
	}

	public static double atan2(double a1, double a2)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return Icecore.atan2((float) a1, (float) a2);
		}
		else
		{
			return Math.atan2(a1, a2);
		}
	}
}
