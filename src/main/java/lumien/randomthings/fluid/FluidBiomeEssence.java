package lumien.randomthings.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBiomeEssence extends Fluid
{

	public FluidBiomeEssence()
	{
		super("biomeEssence", new ResourceLocation("randomthings:textures/fluids/biomeEssenceStill.png"), new ResourceLocation("randomthings:textures/fluids/biomeEssenceFlowing.png"));
	}

}
