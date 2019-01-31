package lumien.randomthings.client.models;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.mesh.PortKeyMesh;
import lumien.randomthings.client.mesh.RedstoneActivatorMesh;
import lumien.randomthings.client.mesh.SoundPatternMesh;
import lumien.randomthings.client.mesh.SoundRecorderMesh;
import lumien.randomthings.client.mesh.SpectreChargerMesh;
import lumien.randomthings.handler.runes.EnumRuneDust;
import lumien.randomthings.item.ItemIngredient;
import lumien.randomthings.item.ItemWeatherEgg;
import lumien.randomthings.item.ItemWeatherEgg.TYPE;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.diviningrod.ItemDiviningRod;
import lumien.randomthings.item.diviningrod.RodType;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemModels
{
	public static void register()
	{
		registerBlock(ModBlocks.fertilizedDirt);
		registerBlock(ModBlocks.fertilizedDirtTilled);
		registerBlock(ModBlocks.playerInterface);
		registerBlock(ModBlocks.creativePlayerInterface);
		registerBlock(ModBlocks.lapisGlass);
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
		registerBlock(ModBlocks.chatDetector);
		registerBlock(ModBlocks.basicRedstoneInterface);
		registerBlock(ModBlocks.advancedRedstoneInterface);
		registerBlock(ModBlocks.lightRedirector);
		registerBlock(ModBlocks.imbuingStation);
		registerBlock(ModBlocks.spectreBlock);
		registerBlock(ModBlocks.analogEmitter);
		registerBlock(ModBlocks.fluidDisplay);
		registerBlock(ModBlocks.customWorkbench);
		registerBlock(ModBlocks.enderMailbox);
		registerBlock(ModBlocks.pitcherPlant);
		registerBlock(ModBlocks.specialChest);
		registerBlock(ModBlocks.entityDetector);
		registerBlock(ModBlocks.quartzLamp);
		registerBlock(ModBlocks.quartzGlass);
		registerBlock(ModBlocks.potionVaporizer);
		registerBlock(ModBlocks.voxelProjector);
		registerBlock(ModBlocks.contactButton);
		registerBlock(ModBlocks.contactLever);
		registerBlock(ModBlocks.sakanade);
		registerBlock(ModBlocks.rainShield);
		registerBlock(ModBlocks.blockBreaker);
		registerBlock(ModBlocks.spectreCore);
		registerBlock(ModBlocks.rainbowLamp);
		registerBlock(ModBlocks.superLubricentIce);
		registerBlock(ModBlocks.compressedSlimeBlock);
		registerBlock(ModBlocks.superLubricentPlatform);
		registerBlock(ModBlocks.filteredSuperLubricentPlatform);
		registerBlock(ModBlocks.redstoneObserver);
		registerBlock(ModBlocks.biomeRadar);
		registerBlock(ModBlocks.ironDropper);
		registerBlock(ModBlocks.igniter);
		registerBlock(ModBlocks.inventoryRerouter);
		registerBlock(ModBlocks.runeBase);
		registerBlock(ModBlocks.slimeCube);
		registerBlock(ModBlocks.peaceCandle);
		registerBlock(ModBlocks.notificationInterface);
		registerBlock(ModBlocks.glowingMushroom);
		registerBlock(ModBlocks.flooBrick);
		registerBlock(ModBlocks.inventoryTester);
		registerBlock(ModBlocks.superLubricentStone);
		registerBlock(ModBlocks.redstonePlate);
		registerBlock(ModBlocks.globalChatDetector);
		registerBlock(ModBlocks.triggerGlass);
		registerBlock(ModBlocks.blockDestabilizer);
		registerBlock(ModBlocks.unpoweredAdvancedRedstoneRepeater);
		registerBlock(ModBlocks.lotus);
		registerBlock(ModBlocks.soundBox);
		registerBlock(ModBlocks.soundDampener);
		registerBlock(ModBlocks.advancedRedstoneTorchOn);
		registerBlock(ModBlocks.blockDiaphanous);
		registerBlock(ModBlocks.sidedRedstone);
		registerBlock(ModBlocks.spectreLens);
		registerBlock(ModBlocks.spectreEnergyInjector);

		registerBlock(ModBlocks.acceleratorPlate);
		registerBlock(ModBlocks.acceleratorPlateDirectional);
		registerBlock(ModBlocks.correctorPlate);
		registerBlock(ModBlocks.redirectorPlate);
		registerBlock(ModBlocks.filteredRedirectorPlate);
		registerBlock(ModBlocks.itemRejuvenatorPlate);
		registerBlock(ModBlocks.itemSealerPlate);
		registerBlock(ModBlocks.bouncyPlate);
		registerBlock(ModBlocks.collectionPlate);
		registerBlock(ModBlocks.processingPlate);
		registerBlock(ModBlocks.extractionPlate);

		registerBlock(ModBlocks.spectreLeaf);
		registerBlock(ModBlocks.spectreLog);
		registerBlock(ModBlocks.spectrePlank);
		registerBlock(ModBlocks.spectreSapling);

		registerBlock(ModBlocks.itemCollector);
		registerBlock(ModBlocks.advancedItemCollector);

		registerItem(ModItems.biomeCrystal);
		registerItem(ModItems.summoningPendulum);
		registerItem(ModItems.stableEnderpearl);
		registerItem(ModItems.positionFilter);
		registerItem(ModItems.beanStew);
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
		registerItem(ModItems.entityFilter);
		registerItem(ModItems.itemFilter);
		registerItem(ModItems.spectreKey);
		registerItem(ModItems.redstoneRemote);
		registerItem(ModItems.spectreAnchor);
		registerItem(ModItems.spectreSword);
		registerItem(ModItems.goldenCompass);
		registerItem(ModItems.blazeAndSteel);
		registerItem(ModItems.superLubricentBoots);
		registerItem(ModItems.runePattern);
		registerItem(ModItems.flooSign);
		registerItem(ModItems.idCard);
		registerItem(ModItems.emeraldCompass);
		registerItem(ModItems.flooToken);
		registerItem(ModItems.lotusSeeds);
		registerItem(ModItems.portableSoundDampener);
		registerItem(ModItems.escapeRope);
		registerItem(ModItems.chunkAnalyzer);
		registerItem(ModItems.flooPouch);
		registerItem(ModItems.timeInABottle);
		registerItem(ModItems.spectreIlluminator);

		registerItem(ModItems.spectrePickaxe);
		registerItem(ModItems.spectreShovel);
		registerItem(ModItems.spectreAxe);

		registerSpectreCoils();
		registerBricks();
		registerBiomeStone();
		registerColoredGrass();
		registerGrassSeeds();
		registerBeans();
		registerImbues();
		registerChests();
		registerPlatforms();
		registerIngredients();
		registerBlockOfSticks();
		registerLuminousBlocks();
		registerAncientBrick();
		registerWeatherEggs();
		registerDiviningRods();

		registerRuneDust();

		ModelLoader.setCustomMeshDefinition(ModItems.redstoneActivator, new RedstoneActivatorMesh());
		ModelBakery.registerItemVariants(ModItems.redstoneActivator, new ModelResourceLocation[] { new ModelResourceLocation("randomthings:redstoneactivator_0"), new ModelResourceLocation("randomthings:redstoneactivator_1"), new ModelResourceLocation("randomthings:redstoneactivator_2") });

		ModelLoader.setCustomMeshDefinition(ModItems.portKey, new PortKeyMesh());
		ModelLoader.setCustomModelResourceLocation(ModItems.portKey, 1, new ModelResourceLocation("randomthings:portkey"));

		ModelLoader.setCustomMeshDefinition(ModItems.soundRecorder, new SoundRecorderMesh());
		ModelBakery.registerItemVariants(ModItems.soundRecorder, new ModelResourceLocation[] { new ModelResourceLocation("randomthings:soundrecorder_idle"), new ModelResourceLocation("randomthings:soundrecorder_active") });

		ModelLoader.setCustomMeshDefinition(ModItems.soundPattern, new SoundPatternMesh());
		ModelBakery.registerItemVariants(ModItems.soundPattern, new ModelResourceLocation[] { new ModelResourceLocation("randomthings:soundpattern_empty"), new ModelResourceLocation("randomthings:soundpattern_full") });
	
		ModelLoader.setCustomMeshDefinition(ModItems.spectreCharger, new SpectreChargerMesh());
		ModelBakery.registerItemVariants(ModItems.spectreCharger, new ModelResourceLocation[] { new ModelResourceLocation("randomthings:spectrecharger/normal"), new ModelResourceLocation("randomthings:spectrecharger/redstone"), new ModelResourceLocation("randomthings:spectrecharger/ender"), new ModelResourceLocation("randomthings:spectrecharger/genesis"),new ModelResourceLocation("randomthings:spectrecharger/normal_enabled"), new ModelResourceLocation("randomthings:spectrecharger/redstone_enabled"), new ModelResourceLocation("randomthings:spectrecharger/ender_enabled"), new ModelResourceLocation("randomthings:spectrecharger/genesis_enabled") });
		
		SpectreStateMapper spectreStateMapper = new SpectreStateMapper();
		ModelLoader.setCustomStateMapper(ModBlocks.spectreCoilNormal, spectreStateMapper);
		ModelLoader.setCustomStateMapper(ModBlocks.spectreCoilRedstone, spectreStateMapper);
		ModelLoader.setCustomStateMapper(ModBlocks.spectreCoilEnder, spectreStateMapper);
		ModelLoader.setCustomStateMapper(ModBlocks.spectreCoilNumber, spectreStateMapper);
		ModelLoader.setCustomStateMapper(ModBlocks.spectreCoilGenesis, spectreStateMapper);
		
		ModelLoader.setCustomModelResourceLocation(ModItems.enderBucket, 0, new ModelResourceLocation("randomthings:enderbucket", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.reinforcedEnderBucket, 0, new ModelResourceLocation("randomthings:reinforcedenderbucket", "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(ModItems.eclipsedClock, 0, new ModelResourceLocation("randomthings:eclipsedclock/eclipsedclock", "inventory"));
	
	}

	private static void registerSpectreCoils()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.spectreCoilNormal), 0, new ModelResourceLocation("randomthings:spectreCoil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.spectreCoilRedstone), 0, new ModelResourceLocation("randomthings:spectreCoil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.spectreCoilEnder), 0, new ModelResourceLocation("randomthings:spectreCoil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.spectreCoilNumber), 0, new ModelResourceLocation("randomthings:spectreCoil", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.spectreCoilGenesis), 0, new ModelResourceLocation("randomthings:spectreCoil", "inventory"));
	}
	
	private static void registerDiviningRods()
	{
		int meta = 0;
		for (RodType r:ItemDiviningRod.types)
		{
			ModelLoader.setCustomModelResourceLocation(ModItems.diviningRod, meta, new ModelResourceLocation("randomthings:diviningrod", "inventory"));
			meta++;
		}
	}
	
	private static void registerWeatherEggs()
	{
		for (TYPE t : ItemWeatherEgg.TYPE.values())
		{
			ModelLoader.setCustomModelResourceLocation(ModItems.weatherEgg, t.ordinal(), new ModelResourceLocation("randomthings:weathereggs/" + t.toString().toLowerCase(), "inventory"));
		}
	}

	private static void registerRuneDust()
	{
		for (EnumRuneDust runeType : EnumRuneDust.values())
		{
			String name = runeType.getName();

			ModelLoader.setCustomModelResourceLocation(ModItems.runeDust, runeType.ordinal(), new ModelResourceLocation("randomthings:runedust", "inventory"));
		}
	}

	private static void registerIngredients()
	{
		for (ItemIngredient.INGREDIENT i : ItemIngredient.INGREDIENT.values())
		{
			ModelLoader.setCustomModelResourceLocation(ModItems.ingredients, i.id, new ModelResourceLocation("randomthings:ingredient/" + i.name, "inventory"));
		}
	}

	private static void registerChests()
	{
		Item chestItem = Item.getItemFromBlock(ModBlocks.specialChest);

		ModelLoader.setCustomModelResourceLocation(chestItem, 0, new ModelResourceLocation("randomthings:specialChest", "inventory"));
		ModelLoader.setCustomModelResourceLocation(chestItem, 1, new ModelResourceLocation("randomthings:specialChest", "inventory"));
	}

	private static void registerImbues()
	{
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 0, new ModelResourceLocation("randomthings:imbues/fire", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 1, new ModelResourceLocation("randomthings:imbues/poison", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 2, new ModelResourceLocation("randomthings:imbues/experience", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 3, new ModelResourceLocation("randomthings:imbues/wither", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ModItems.imbue, 4, new ModelResourceLocation("randomthings:imbues/collapse", "inventory"));
	}

	private static void registerBeans()
	{
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
	}

	private static void registerItem(Item i)
	{
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}

	private static void registerLuminousBlocks()
	{
		for (int i = 0; i < 16; i++)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.luminousBlock), i, new ModelResourceLocation("randomthings:luminousblock/" + EnumDyeColor.byMetadata(i).getUnlocalizedName(), "inventory"));
		}

		for (int i = 0; i < 16; i++)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.translucentLuminousBlock), i, new ModelResourceLocation("randomthings:luminousblock/" + EnumDyeColor.byMetadata(i).getUnlocalizedName() + "_t", "inventory"));
		}
	}

	private static void registerBricks()
	{
		for (int i = 0; i < 16; i++)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.stainedBrick), i, new ModelResourceLocation("randomthings:stainedBrick/" + EnumDyeColor.byMetadata(i).getUnlocalizedName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.luminousStainedBrick), i, new ModelResourceLocation("randomthings:luminousStainedBrick/" + EnumDyeColor.byMetadata(i).getUnlocalizedName(), "inventory"));
		}
	}

	private static void registerBiomeStone()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 0, new ModelResourceLocation("randomthings:biomeStone/cobble", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 1, new ModelResourceLocation("randomthings:biomeStone/smooth", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 2, new ModelResourceLocation("randomthings:biomeStone/brick", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 3, new ModelResourceLocation("randomthings:biomeStone/cracked", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.biomeStone), 4, new ModelResourceLocation("randomthings:biomeStone/chiseled", "inventory"));
	}

	private static void registerAncientBrick()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ancientBrick), 0, new ModelResourceLocation("randomthings:ancientBrick/runes", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ancientBrick), 1, new ModelResourceLocation("randomthings:ancientBrick/default", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ancientBrick), 2, new ModelResourceLocation("randomthings:ancientBrick/empty", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.ancientBrick), 3, new ModelResourceLocation("randomthings:ancientBrick/full", "inventory"));
	}

	private static void registerPlatforms()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 0, new ModelResourceLocation("randomthings:platform/oak", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 1, new ModelResourceLocation("randomthings:platform/spruce", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 2, new ModelResourceLocation("randomthings:platform/birch", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 3, new ModelResourceLocation("randomthings:platform/jungle", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 4, new ModelResourceLocation("randomthings:platform/acacia", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.platform), 5, new ModelResourceLocation("randomthings:platform/darkOak", "inventory"));
	}

	private static void registerBlockOfSticks()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.blockOfSticks), 0, new ModelResourceLocation("randomthings:blockOfSticks", "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.blockOfSticks), 1, new ModelResourceLocation("randomthings:returningBlockOfSticks", "inventory"));
	}
}
