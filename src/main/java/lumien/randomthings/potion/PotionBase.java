package lumien.randomthings.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

public class PotionBase extends Potion
{

	protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);

		this.setRegistryName(new ResourceLocation("randomthings:" + name));
		GameData.getPotionRegistry().register(this);
	}

}
