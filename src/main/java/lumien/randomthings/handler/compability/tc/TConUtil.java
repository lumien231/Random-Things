package lumien.randomthings.handler.compability.tc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TConUtil
{
	static
	{
		try
		{
			shovelClass = Class.forName("slimeknights.tconstruct.tools.tools.Shovel");
		}
		catch (ClassNotFoundException cnfe)
		{
			// TCon not loaded or outdated
			
			shovelClass = null;
		}
	}
	
	static Class shovelClass;
	
	public static boolean isTconShovel(ItemStack is)
	{
		if (shovelClass == null)
		{
			return false;
		}
		
		
		if (!is.isEmpty())
		{
			Item i = is.getItem();
			
			return shovelClass.isAssignableFrom(i.getClass());
		}
		
		return false;
	}
}
