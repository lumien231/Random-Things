package lumien.randomthings.lib.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedBool implements IUnlistedProperty<Boolean>
{
	String name;

	private UnlistedBool(String name)
	{
		this.name = name;
	}

	public static UnlistedBool create(String name)
	{
		return new UnlistedBool(name);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isValid(Boolean value)
	{
		return true;
	}

	@Override
	public Class<Boolean> getType()
	{
		return Boolean.class;
	}

	@Override
	public String valueToString(Boolean value)
	{
		return value + "";
	}

}
