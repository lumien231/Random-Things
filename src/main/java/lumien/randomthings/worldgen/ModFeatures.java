package lumien.randomthings.worldgen;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("randomthings")
public class ModFeatures
{
	@ObjectHolder("blood_roses")
	public static BloodRoseFeature BLOOD_ROSES;

	public static void registerFeatures(Register<Feature<?>> featureRegistryEvent)
	{
		featureRegistryEvent.getRegistry().register(new BloodRoseFeature(NoFeatureConfig::deserialize).setRegistryName("blood_roses"));
	}
	
}
