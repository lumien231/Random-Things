package lumien.randomthings.handler.compability.oc;

import li.cil.oc.api.Driver;

public class OCComp
{
	public static void init()
	{
		Driver.add(new DriverOnlineDetector());
		Driver.add(new DriverCreativePlayerInterface());
		Driver.add(new DriverBasicRedstoneInterface());
		Driver.add(new DriverRedstoneObserver());
		Driver.add(new DriverNotificationInterface());
	}
}
