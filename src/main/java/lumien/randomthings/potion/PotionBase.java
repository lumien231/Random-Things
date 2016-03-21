package lumien.randomthings.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

public class PotionBase extends Potion
{

	protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);

		Potion.potionRegistry.register(-1, new ResourceLocation("randomthings:" + name), this);
	}

}
