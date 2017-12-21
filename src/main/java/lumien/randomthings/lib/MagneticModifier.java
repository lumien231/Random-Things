package lumien.randomthings.lib;

import lumien.randomthings.item.ModItems;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MagneticModifier extends AttributeModifier
{
	EntityPlayer attachedPlayer;
	
	public MagneticModifier(EntityPlayer attachedPlayer)
	{
		super("magnetic", 0, 0);
		
		this.attachedPlayer = attachedPlayer;
	}
	
	@Override
	public double getAmount()
	{
		ItemStack holdingMain = attachedPlayer.getHeldItemMainhand();

		if (!holdingMain.isEmpty() && (holdingMain.getItem() == ModItems.spectrePickaxe || holdingMain.getItem() == ModItems.spectreAxe || holdingMain.getItem() == ModItems.spectreShovel))
		{
			return 3;
		}
		
		return 0;
	}

	@Override
	public boolean isSaved()
	{
		return true;
	}
}
