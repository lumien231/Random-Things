package lumien.randomthings.asm;

public class AsmHandler
{
	public static float modBlockLight(float originalValue, int tintIndex)
	{

		if (tintIndex == 12340)
		{
			return 0.0073243305104143F;
		}
		else
		{
			return originalValue;
		}
	}
}
