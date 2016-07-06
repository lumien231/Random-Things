package lumien.randomthings.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModFluids
{
	public static Fluid biomeEssence;
	public static void load(FMLPreInitializationEvent event)
	{
		biomeEssence = new FluidBiomeEssence();
		FluidRegistry.registerFluid(biomeEssence);
		FluidRegistry.addBucketForFluid(biomeEssence);
	}
}
