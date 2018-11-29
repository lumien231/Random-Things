package lumien.randomthings.client.models.blocks;

import lumien.randomthings.block.BlockIgniter;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.models.EmptyStateMapper;
import lumien.randomthings.client.models.FocusStateMapper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;

public class BlockModels
{
	public static void register()
	{
		ModelLoader.setCustomStateMapper(ModBlocks.coloredGrass, new FocusStateMapper(new ModelResourceLocation("randomthings:coloredGrass", "normal")));
		ModelLoader.setCustomStateMapper(ModBlocks.specialChest, new EmptyStateMapper());
		ModelLoader.setCustomStateMapper(ModBlocks.blockDiaphanous, new EmptyStateMapper());
		
		ModelLoader.setCustomStateMapper(ModBlocks.spectreSapling, new FocusStateMapper(new ModelResourceLocation("randomthings:spectreSapling", "normal")));
		ModelLoader.setCustomStateMapper(ModBlocks.spectreLeaf, new FocusStateMapper(new ModelResourceLocation("randomthings:spectreLeaf", "normal")));

		ModelLoader.setCustomStateMapper(ModBlocks.inventoryRerouter, new FocusStateMapper(new ModelResourceLocation("randomthings:inventoryrerouter", "normal")));

		ModelLoader.setCustomStateMapper(ModBlocks.runeBase, new FocusStateMapper(new ModelResourceLocation("randomthings:runeBase", "normal")));
	}

}
