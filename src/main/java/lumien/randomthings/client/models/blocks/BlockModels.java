package lumien.randomthings.client.models.blocks;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.models.EmptyStateMapper;
import lumien.randomthings.client.models.FocusStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class BlockModels
{
	public static void register()
	{
		ModelLoader.setCustomStateMapper(ModBlocks.coloredGrass, new FocusStateMapper(new ModelResourceLocation("randomthings:coloredGrass", "normal")));
		ModelLoader.setCustomStateMapper(ModBlocks.specialChest, new EmptyStateMapper());
	}

}
