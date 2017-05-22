package lumien.randomthings.lib.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedEnum<T extends Enum> implements IUnlistedProperty<T>
{
	String name;
	Class<? extends Enum> enumClass;

	private UnlistedEnum(String name, Class<? extends T> enumClass)
	{
		this.name = name;
		this.enumClass = enumClass;
	}

	public static UnlistedEnum create(String name, Class<? extends Enum> enumClass)
	{
		return new UnlistedEnum(name, enumClass);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isValid(T value)
	{
		return true;
	}

	@Override
	public Class<T> getType()
	{
		return (Class<T>) enumClass;
	}

	@Override
	public String valueToString(T value)
	{
		return value.toString();
	}

}
