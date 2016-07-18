package lumien.randomthings.util;

public class RandomUtil
{
	public static <E extends Enum> E rotateEnum(E oldEnum)
	{
		int currentIndex = oldEnum.ordinal();
		
		Object[] enumConstants = oldEnum.getDeclaringClass().getEnumConstants();
		int length = enumConstants.length;

		return (E) ((currentIndex + 1 < length) ? enumConstants[currentIndex + 1] : enumConstants[0]);

	}
}
