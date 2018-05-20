package lumien.randomthings.handler.compability.oc;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class TileEntityEnvironment<T> extends AbstractManagedEnvironment implements NamedBlock
{
	String name;

	T tileEntity;

	public TileEntityEnvironment(String name, T tileEntity)
	{
		this.name = name;
		this.tileEntity = tileEntity;

		this.setNode(Network.newNode(this, Visibility.Network).withComponent(this.name, Visibility.Network).create());
	}

	@Override
	public String preferredName()
	{
		return name;
	}

	@Override
	public int priority()
	{
		return 4;
	}
}
