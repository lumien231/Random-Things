package lumien.randomthings.client.models;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.mesh.StainedBrickItemMesh;
import lumien.randomthings.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemModels
{
	public static void register()
	{
		registerBlock(ModBlocks.fertilizedDirt);
		registerBlock(ModBlocks.playerInterface);
		registerBlock(ModBlocks.creativePlayerInterface);
		registerBlock(ModBlocks.lapisGlass);
		// registerBlock(ModBlocks.portalGenerator); TODO Portal Generator
		registerBlock(ModBlocks.biomeGlass);
		registerBlock(ModBlocks.lapisLamp);
		registerBlock(ModBlocks.dyeingMachine);
		registerBlock(ModBlocks.onlineDetector);
		registerBlock(ModBlocks.enderBridge);
		registerBlock(ModBlocks.prismarineEnderBridge);
		registerBlock(ModBlocks.enderAnchor);
		registerBlock(ModBlocks.beanStalk);
		registerBlock(ModBlocks.lesserBeanStalk);
		registerBlock(ModBlocks.beanPod);
		registerBlock(ModBlocks.beanSprout);
		registerBlock(ModBlocks.natureCore);
		registerBlock(ModBlocks.netherCore);
		registerBlock(ModBlocks.enderCore);
		registerBlock(ModBlocks.lifeAnchor);
		registerBlock(ModBlocks.chatDetector);
		registerBlock(ModBlocks.redstoneInterface);
		registerBlock(ModBlocks.lightRedirector);
		registerBlock(ModBlocks.imbuingStation);
		registerBlock(ModBlocks.spectreBlock);
		registerBlock(ModBlocks.analogEmitter);
		registerBlock(ModBlocks.fluidDisplay);
		registerBlock(ModBlocks.customWorkbench);
		registerBlock(ModBlocks.enderMailbox);
		registerBlock(ModBlocks.pitcherPlant);
		registerBlock(ModBlocks.specialChest);
		registerBlock(ModBlocks.platform);
		registerBlock(ModBlocks.entityDetector);
		registerBlock(ModBlocks.quartzLamp);
		registerBlock(ModBlocks.quartzGlass);

		registerItem(ModItems.chaliceOfImmortality);
		registerItem(ModItems.biomeCrystal);
		registerItem(ModItems.summoningPendulum);
		registerItem(ModItems.stableEnderpearl);
		registerItem(ModItems.positionFilter);
		registerItem(ModItems.beanStew);
		registerItem(ModItems.linkingOrb);
		registerItem(ModItems.inertLinkingOrb);
		registerItem(ModItems.redstoneTool);
		registerItem(ModItems.waterWalkingBoots);
		registerItem(ModItems.dungeonChestGenerator);
		registerItem(ModItems.lavaCharm);
		registerItem(ModItems.lavaWader);
		registerItem(ModItems.obsidianSkull);
		registerItem(ModItems.obsidianSkullRing);
		registerItem(ModItems.obsidianWaterWalkingBoots);
		registerItem(ModItems.magicHood);
		registerItem(ModItems.bottleOfAir);
		registerItem(ModItems.rezStone);
		registerItem(ModItems.enderLetter);
		// registerItem(ModItems.craftingRecipe);

		registerBricks();
		registerBiomeStone();
		registerColoredGrass();
		registerGrassSeeds();
		registerBeans();
		registerImbues();
		registerChests();
		registerPlatforms();
	}

	private static void registerChests()
	{
		Item chestItem = Item.getItemFromBlock(ModBlocks.specialChest);

		ModelLoader.setCustomModelResourceLocation(chestItem, 0, new ModelResourceLocation("randomthings:specialChest", "inventory"));
		ModelLoader.setCustomModelResourceLocation(chestItem, 1, new ModelResourceLocation("randomthings:specialChest", "inventory"));
	}

	private static void registerImbues()
	{
		ModelBakery.addVariantName(ModItems.imbue, "randomthings:imbues/fire");
		ModelBakery.addVariantName(ModItems.imbue, "randomthings:imbues/poison");
		ModelBakery.addVariantName(ModItems.imbue, "randomthings:imbues/experience");
		ModelBakery.addVariantName(ModItems.imbue, "randomthings:imbues/wither");

		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 0, new ModelResourceLocation("randomthings:imbues/fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 1, new ModelResourceLocation("randomthings:imbues/poison", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 2, new ModelResourceLocation("randomthings:imbues/experience", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 3, new ModelResourceLocation("randomthings:imbues/wither", "inventory"));
	}

	private static void registerBeans()
	{
		ModelBakery.addVariantName(ModItems.beans, "randomthings:bean");
		ModelBakery.addVariantName(ModItems.beans, "randomthings:lesserBean");
		ModelBakery.addVariantName(ModItems.beans, "randomthings:magicBean");

		ModelLoader.setCustomModelResourceLocation(ModItems.beans, 0, new ModelResourceLocation("randomthings:bean", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.beans, 1, new ModelResourceLocation("randomthings:lesserBean", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.beans, 2, new ModelResourceLocation("randomthings:magicBean", "inventory"));
	}

	private static void registerGrassSeeds()
	{
		for (int i = 0; i < 17; i++)
		{
			ModelLoader.setCustomModelResourceLocation(ModItems.grassSeeds, i, new ModelResourceLocation("randomthings:grassSeeds", "inventory"));
		}
	}

	private static void registerColoredGrass()
	{
		for (int i = 0; i < 16; i++)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.coloredGrass), i, new ModelResourceLocation("randomthings:coloredGrass"));
		}
	}

	private static void registerBlock(Block b)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation("randomthings:" + GameRegistry.findUniqueIdentifierFor(b).name, "inventory"));
	}

	private static void registerItem(Item i)
	{
		String register = "randomthings:" + GameRegistry.findUniqueIdentifierFor(i).name;
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(register, "inventory"));
	}

	private static void registerBricks()
	{
		for (int i = 0; i < 16; i++)
		{
			ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.stainedBrick), "randomthings:stainedBrick/" + EnumDyeColor.byMetadata(i).getUnlocalizedName());
		}
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ModBlocks.stainedBrick), new StainedBrickItemMesh());
	}

	private static void registerBiomeStone()
	{
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.biomeStone), "randomthings:biomeStone/cobble");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.biomeStone), "randomthings:biomeStone/smooth");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.biomeStone), "randomthings:biomeStone/brick");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.biomeStone), "randomthings:biomeStone/cracked");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.biomeStone), "randomthings:biomeStone/chiseled");

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 0, new ModelResourceLocation("randomthings:biomeStone/cobble", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 1, new ModelResourceLocation("randomthings:biomeStone/smooth", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 2, new ModelResourceLocation("randomthings:biomeStone/brick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 3, new ModelResourceLocation("randomthings:biomeStone/cracked", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 4, new ModelResourceLocation("randomthings:biomeStone/chiseled", "inventory"));
	}
	
	private static void registerPlatforms()
	{
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/oak");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/birch");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/spruce");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/acacia");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/jungle");
		ModelBakery.addVariantName(Item.getItemFromBlock(ModBlocks.platform), "randomthings:platform/darkOak");

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 0, new ModelResourceLocation("randomthings:platform/oak", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 1, new ModelResourceLocation("randomthings:platform/spruce", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 2, new ModelResourceLocation("randomthings:platform/birch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 3, new ModelResourceLocation("randomthings:platform/jungle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 4, new ModelResourceLocation("randomthings:platform/acacia", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 5, new ModelResourceLocation("randomthings:platform/darkOak", "inventory"));
	}
}
