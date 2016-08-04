package lumien.randomthings.block;

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
	public static BlockEnderBridge enderBridge;
	public static BlockEnderAnchor enderAnchor;
	public static BlockBiomeStone biomeStone;
	public static BlockBiomeGlass biomeGlass;
	public static BlockStainedBrick stainedBrick;
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
	public static BlockItemSealer itemSealer;
	public static BlockSuperLubricentIce superLubricentIce;
	public static BlockItemCorrector itemCorrector;
	public static BlockCompressedSlimeBlock compressedSlimeBlock;
	public static BlockItemRedirector itemRedirector;
	public static BlockItemRejuvenator itemRejuvenator;
	public static BlockSuperLubricentPlatform superLubricentPlatform;
	public static BlockFilteredItemRedirector filteredItemRedirector;
	public static BlockFilteredSuperLubricentPlatform filteredSuperLubricentPlatform;
	public static BlockRedstoneObserver redstoneObserver;
	public static BlockBiomeRadar biomeRadar;
	public static BlockIronDropper ironDropper;
	public static BlockItemProjector itemProjector;
	
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
		itemSealer = new BlockItemSealer();
		superLubricentIce = new BlockSuperLubricentIce();
		itemCorrector = new BlockItemCorrector();
		compressedSlimeBlock = new BlockCompressedSlimeBlock();
		itemRedirector = new BlockItemRedirector();
		itemRejuvenator = new BlockItemRejuvenator();
		superLubricentPlatform = new BlockSuperLubricentPlatform();
		filteredItemRedirector = new BlockFilteredItemRedirector();
		filteredSuperLubricentPlatform = new BlockFilteredSuperLubricentPlatform();
		redstoneObserver = new BlockRedstoneObserver();
		biomeRadar = new BlockBiomeRadar();
		ironDropper = new BlockIronDropper();
		itemProjector = new BlockItemProjector();
		
		spectrePlank = new BlockSpectrePlank();
		spectreSapling = new BlockSpectreSapling();
		spectreLog = new BlockSpectreLog();
		spectreLeaf = new BlockSpectreLeaf();
		
		itemCollector = new BlockItemCollector();
		advancedItemCollector = new BlockAdvancedItemCollector();
		
		natureCore = new BlockNatureCore();

		biomeStone = new BlockBiomeStone();
		biomeGlass = new BlockBiomeGlass();
		stainedBrick = new BlockStainedBrick();
		coloredGrass = new BlockColoredGrass();
		
		spectreCore = new BlockSpectreCore();
		
		rainbowLamp = new BlockRainbowLamp();
		
		basicRedstoneInterface = new BlockBasicRedstoneInterface();
		advancedRedstoneInterface = new BlockAdvancedRedstoneInterface();
	}
}
