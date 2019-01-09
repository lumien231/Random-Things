package lumien.randomthings.block;

import lumien.randomthings.block.BlockSpectreCoil.CoilType;
import lumien.randomthings.block.plates.BlockAcceleratorPlate;
import lumien.randomthings.block.plates.BlockBouncyPlate;
import lumien.randomthings.block.plates.BlockCollectionPlate;
import lumien.randomthings.block.plates.BlockCorrectorPlate;
import lumien.randomthings.block.plates.BlockDirectionalAcceleratorPlate;
import lumien.randomthings.block.plates.BlockExtractionPlate;
import lumien.randomthings.block.plates.BlockFilteredRedirectorPlate;
import lumien.randomthings.block.plates.BlockItemRejuvenatorPlate;
import lumien.randomthings.block.plates.BlockItemSealerPlate;
import lumien.randomthings.block.plates.BlockProcessingPlate;
import lumien.randomthings.block.plates.BlockRedirectorPlate;
import lumien.randomthings.block.plates.BlockRedstonePlate;
import lumien.randomthings.block.redstoneinterface.BlockAdvancedRedstoneInterface;
import lumien.randomthings.block.redstoneinterface.BlockBasicRedstoneInterface;
import lumien.randomthings.block.spectretree.BlockSpectreLeaf;
import lumien.randomthings.block.spectretree.BlockSpectreLog;
import lumien.randomthings.block.spectretree.BlockSpectrePlank;
import lumien.randomthings.block.spectretree.BlockSpectreSapling;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModBlocks
{
	public static BlockFertilizedDirt fertilizedDirt;
	public static BlockFertilizedDirt fertilizedDirtTilled;
	public static BlockPlayerInterface playerInterface;
	public static BlockCreativePlayerInterface creativePlayerInterface;
	public static BlockLapisGlass lapisGlass;
	public static BlockLapisLamp lapisLamp;
	public static BlockDyeingMachine dyeingMachine;
	public static BlockOnlineDetector onlineDetector;
	public static BlockChatDetector chatDetector;
	public static BlockGlobalChatDetector globalChatDetector;
	public static BlockEnderBridge enderBridge;
	public static BlockEnderAnchor enderAnchor;
	public static BlockBiomeStone biomeStone;
	public static BlockBiomeGlass biomeGlass;
	public static BlockColoredGrass coloredGrass;
	public static BlockPrismarineEnderBridge prismarineEnderBridge;
	public static BlockPod beanPod;
	public static BlockBeanSprout beanSprout;
	public static BlockLightRedirector lightRedirector;
	public static BlockImbuingStation imbuingStation;
	public static BlockSpectreBlock spectreBlock;
	public static BlockSpecialChest specialChest;
	public static BlockAnalogEmitter analogEmitter;
	public static BlockFluidDisplay fluidDisplay;
	public static BlockCustomWorkbench customWorkbench;
	public static BlockEnderMailbox enderMailbox;
	public static BlockPitcherPlant pitcherPlant;
	public static BlockPlatform platform;
	public static BlockEntityDetector entityDetector;
	public static BlockQuartzLamp quartzLamp;
	public static BlockQuartzGlass quartzGlass;
	public static BlockPotionVaporizer potionVaporizer;
	public static BlockVoxelProjector voxelProjector;
	public static BlockSakanade sakanade;
	public static BlockRainShield rainShield;
	public static BlockBlockBreaker blockBreaker;
	public static BlockSuperLubricentIce superLubricentIce;
	public static BlockCompressedSlimeBlock compressedSlimeBlock;
	public static BlockSuperLubricentPlatform superLubricentPlatform;
	public static BlockFilteredSuperLubricentPlatform filteredSuperLubricentPlatform;
	public static BlockRedstoneObserver redstoneObserver;
	public static BlockBiomeRadar biomeRadar;
	public static BlockIronDropper ironDropper;
	public static BlockIgniter igniter;
	public static BlockBlockOfSticks blockOfSticks;
	public static BlockBlazingFire blazingFire;
	public static BlockBlockLuminous luminousBlock;
	public static BlockInventoryRerouter inventoryRerouter;
	public static BlockRuneBase runeBase;
	public static BlockSlimeCube slimeCube;
	public static BlockPeaceCandle peaceCandle;
	public static BlockNotificationInterface notificationInterface;
	public static BlockGlowingMushroom glowingMushroom;
	public static BlockInventoryTester inventoryTester;
	public static BlockSuperLubricentStone superLubricentStone;
	public static BlockTriggerGlass triggerGlass;
	public static BlockBlockDestabilizer blockDestabilizer;
	public static BlockLotus lotus;
	public static BlockBlockLuminousTranslucent translucentLuminousBlock;
	public static BlockSoundBox soundBox;
	public static BlockSoundDampener soundDampener;
	public static BlockBlockDiaphanous blockDiaphanous;
	public static BlockSidedRedstone sidedRedstone;
	public static BlockSpectreLens spectreLens;
	public static BlockSpectreEnergyInjector spectreEnergyInjector;
	public static BlockLinkOrb linkOrb;
	
	public static BlockSpectreCoil spectreCoilNormal;
	public static BlockSpectreCoil spectreCoilRedstone;
	public static BlockSpectreCoil spectreCoilEnder;
	public static BlockSpectreCoil spectreCoilNumber;
	public static BlockSpectreCoil spectreCoilGenesis;

	public static BlockAncientFurnace ancientFurnace;
	public static BlockAncientBrick ancientBrick;

	public static BlockAdvancedRedstoneRepeater unpoweredAdvancedRedstoneRepeater;
	public static BlockAdvancedRedstoneRepeater poweredAdvancedRedstoneRepeater;

	public static BlockAdvancedRedstoneTorch advancedRedstoneTorchOff;
	public static BlockAdvancedRedstoneTorch advancedRedstoneTorchOn;

	public static BlockFlooBrick flooBrick;

	public static BlockSpectrePlank spectrePlank;
	public static BlockSpectreSapling spectreSapling;
	public static BlockSpectreLog spectreLog;
	public static BlockSpectreLeaf spectreLeaf;

	public static BlockItemCollector itemCollector;
	public static BlockAdvancedItemCollector advancedItemCollector;

	public static BlockContactButton contactButton;
	public static BlockContactLever contactLever;

	public static BlockNatureCore natureCore;

	public static BlockBeanStalk beanStalk;
	public static BlockBeanStalk lesserBeanStalk;

	public static BlockSpectreCore spectreCore;

	public static BlockRainbowLamp rainbowLamp;

	public static BlockBasicRedstoneInterface basicRedstoneInterface;
	public static BlockAdvancedRedstoneInterface advancedRedstoneInterface;

	public static BlockStainedBrick stainedBrick;
	public static BlockStainedBrick luminousStainedBrick;

	// Plates
	public static BlockRedirectorPlate redirectorPlate;
	public static BlockFilteredRedirectorPlate filteredRedirectorPlate;
	public static BlockCorrectorPlate correctorPlate;
	public static BlockItemSealerPlate itemSealerPlate;
	public static BlockItemRejuvenatorPlate itemRejuvenatorPlate;
	public static BlockAcceleratorPlate acceleratorPlate;
	public static BlockDirectionalAcceleratorPlate acceleratorPlateDirectional;
	public static BlockBouncyPlate bouncyPlate;
	public static BlockCollectionPlate collectionPlate;
	public static BlockRedstonePlate redstonePlate;
	public static BlockRedstonePlate redstonePlatePowered;
	public static BlockProcessingPlate processingPlate;
	public static BlockExtractionPlate extractionPlate;

	public static void load(FMLPreInitializationEvent event)
	{
		fertilizedDirt = new BlockFertilizedDirt(false);
		fertilizedDirtTilled = new BlockFertilizedDirt(true);
		playerInterface = new BlockPlayerInterface();
		creativePlayerInterface = new BlockCreativePlayerInterface();
		lapisGlass = new BlockLapisGlass();
		lapisLamp = new BlockLapisLamp();
		dyeingMachine = new BlockDyeingMachine();
		onlineDetector = new BlockOnlineDetector();
		chatDetector = new BlockChatDetector();
		enderBridge = new BlockEnderBridge();
		prismarineEnderBridge = new BlockPrismarineEnderBridge();
		enderAnchor = new BlockEnderAnchor();
		beanStalk = new BlockBeanStalk(true);
		lesserBeanStalk = new BlockBeanStalk(false);
		beanPod = new BlockPod();
		beanSprout = new BlockBeanSprout();
		lightRedirector = new BlockLightRedirector();
		imbuingStation = new BlockImbuingStation();
		spectreBlock = new BlockSpectreBlock();
		specialChest = new BlockSpecialChest();
		analogEmitter = new BlockAnalogEmitter();
		fluidDisplay = new BlockFluidDisplay();
		customWorkbench = new BlockCustomWorkbench();
		enderMailbox = new BlockEnderMailbox();
		pitcherPlant = new BlockPitcherPlant();
		platform = new BlockPlatform();
		entityDetector = new BlockEntityDetector();
		quartzLamp = new BlockQuartzLamp();
		quartzGlass = new BlockQuartzGlass();
		potionVaporizer = new BlockPotionVaporizer();
		voxelProjector = new BlockVoxelProjector();
		contactButton = new BlockContactButton();
		contactLever = new BlockContactLever();
		sakanade = new BlockSakanade();
		rainShield = new BlockRainShield();
		blockBreaker = new BlockBlockBreaker();
		superLubricentIce = new BlockSuperLubricentIce();
		compressedSlimeBlock = new BlockCompressedSlimeBlock();
		superLubricentPlatform = new BlockSuperLubricentPlatform();
		filteredSuperLubricentPlatform = new BlockFilteredSuperLubricentPlatform();
		redstoneObserver = new BlockRedstoneObserver();
		biomeRadar = new BlockBiomeRadar();
		ironDropper = new BlockIronDropper();
		igniter = new BlockIgniter();
		blockOfSticks = new BlockBlockOfSticks();
		blazingFire = new BlockBlazingFire();
		luminousBlock = new BlockBlockLuminous();
		translucentLuminousBlock = new BlockBlockLuminousTranslucent();
		inventoryRerouter = new BlockInventoryRerouter();
		runeBase = new BlockRuneBase();
		slimeCube = new BlockSlimeCube();
		peaceCandle = new BlockPeaceCandle();
		notificationInterface = new BlockNotificationInterface();
		glowingMushroom = new BlockGlowingMushroom();
		inventoryTester = new BlockInventoryTester();
		superLubricentStone = new BlockSuperLubricentStone();
		globalChatDetector = new BlockGlobalChatDetector();
		triggerGlass = new BlockTriggerGlass();
		blockDestabilizer = new BlockBlockDestabilizer();
		lotus = new BlockLotus();
		ancientFurnace = new BlockAncientFurnace();
		ancientBrick = new BlockAncientBrick();
		soundBox = new BlockSoundBox();
		soundDampener = new BlockSoundDampener();
		blockDiaphanous = new BlockBlockDiaphanous();
		sidedRedstone = new BlockSidedRedstone();
		spectreLens = new BlockSpectreLens();
		spectreEnergyInjector = new BlockSpectreEnergyInjector();
		processingPlate = new BlockProcessingPlate();
		
		spectreCoilNormal = new BlockSpectreCoil(CoilType.NORMAL);
		spectreCoilRedstone = new BlockSpectreCoil(CoilType.REDSTONE);
		spectreCoilEnder = new BlockSpectreCoil(CoilType.ENDER);
		spectreCoilNumber = new BlockSpectreCoil(CoilType.NUMBER);
		spectreCoilGenesis = new BlockSpectreCoil(CoilType.GENESIS);

		unpoweredAdvancedRedstoneRepeater = new BlockAdvancedRedstoneRepeater(false);
		poweredAdvancedRedstoneRepeater = new BlockAdvancedRedstoneRepeater(true);

		advancedRedstoneTorchOff = new BlockAdvancedRedstoneTorch(false);
		advancedRedstoneTorchOn = new BlockAdvancedRedstoneTorch(true);

		flooBrick = new BlockFlooBrick();

		spectrePlank = new BlockSpectrePlank();
		spectreSapling = new BlockSpectreSapling();
		spectreLog = new BlockSpectreLog();
		spectreLeaf = new BlockSpectreLeaf();

		itemCollector = new BlockItemCollector();
		advancedItemCollector = new BlockAdvancedItemCollector();

		natureCore = new BlockNatureCore();

		biomeStone = new BlockBiomeStone();
		biomeGlass = new BlockBiomeGlass();
		coloredGrass = new BlockColoredGrass();

		spectreCore = new BlockSpectreCore();

		rainbowLamp = new BlockRainbowLamp();

		basicRedstoneInterface = new BlockBasicRedstoneInterface();
		advancedRedstoneInterface = new BlockAdvancedRedstoneInterface();

		stainedBrick = new BlockStainedBrick(false);
		luminousStainedBrick = new BlockStainedBrick(true);

		redirectorPlate = new BlockRedirectorPlate();
		filteredRedirectorPlate = new BlockFilteredRedirectorPlate();
		redstonePlate = new BlockRedstonePlate(false);
		redstonePlatePowered = new BlockRedstonePlate(true);
		correctorPlate = new BlockCorrectorPlate();
		itemSealerPlate = new BlockItemSealerPlate();
		itemRejuvenatorPlate = new BlockItemRejuvenatorPlate();
		acceleratorPlate = new BlockAcceleratorPlate();
		acceleratorPlateDirectional = new BlockDirectionalAcceleratorPlate();
		bouncyPlate = new BlockBouncyPlate();
		collectionPlate = new BlockCollectionPlate();
		extractionPlate = new BlockExtractionPlate();
	}
}
