package lumien.randomthings.block;

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
	public static BlockLifeAnchor lifeAnchor;
	public static BlockRedstoneInterface redstoneInterface;
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

	public static BlockNatureCore natureCore;
	public static BlockNetherCore netherCore;
	public static BlockEnderCore enderCore;

	public static BlockBeanStalk beanStalk;
	public static BlockBeanStalk lesserBeanStalk;

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
		lifeAnchor = new BlockLifeAnchor();
		redstoneInterface = new BlockRedstoneInterface();
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

		natureCore = new BlockNatureCore();
		netherCore = new BlockNetherCore();
		enderCore = new BlockEnderCore();

		biomeStone = new BlockBiomeStone();
		biomeGlass = new BlockBiomeGlass();
		stainedBrick = new BlockStainedBrick();
		coloredGrass = new BlockColoredGrass();
	}
}
