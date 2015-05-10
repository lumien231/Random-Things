package lumien.randomthings.client.models;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class BlockModels
{
	public static void register()
	{
		ModelLoader.setCustomStateMapper(ModBlocks.coloredGrass, new FocusStateMapper(new ModelResourceLocation("randomthings:coloredGrass", "normal")));
	}

}
