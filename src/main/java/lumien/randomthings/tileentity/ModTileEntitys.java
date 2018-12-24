package lumien.randomthings.tileentity;

import lumien.randomthings.tileentity.cores.TileEntityNatureCore;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityAdvancedRedstoneInterface;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntitys
{
	public static void register()
	{
		registerTE(TileEntityPlayerInterface.class, "playerInterface");
		registerTE(TileEntityCreativePlayerInterface.class, "creativePlayerInterface");
		registerTE(TileEntityOnlineDetector.class, "onlineDetector");
		registerTE(TileEntityEnderBridge.class, "enderBridge");
		registerTE(TileEntityPrismarineEnderBridge.class, "prismarineEnderBridge");
		registerTE(TileEntityNatureCore.class, "natureCore");
		registerTE(TileEntityChatDetector.class, "chatDetector");
		registerTE(TileEntityBasicRedstoneInterface.class, "basicRedstoneInterface");
		registerTE(TileEntityAdvancedRedstoneInterface.class, "advancedRedstoneInterface");
		registerTE(TileEntityImbuingStation.class, "imbuingStation");
		registerTE(TileEntitySpecialChest.class, "specialChest");
		registerTE(TileEntityAnalogEmitter.class, "analogEmitter");
		registerTE(TileEntityFluidDisplay.class, "fluidDisplay");
		registerTE(TileEntityCustomWorkbench.class, "customWorkbench");
		registerTE(TileEntityEnderMailbox.class, "enderMailbox");
		registerTE(TileEntityEntityDetector.class, "entityDetector");
		registerTE(TileEntityPotionVaporizer.class, "potionVaporizer");
		registerTE(TileEntityVoxelProjector.class, "voxelProjector");
		registerTE(TileEntityEnderAnchor.class, "enderAnchor");
		registerTE(TileEntityRainShield.class, "rainShield");
		registerTE(TileEntityBlockBreaker.class, "blockBreaker");
		registerTE(TileEntityLightRedirector.class, "lightRedirector");
		registerTE(TileEntityFilteredRedirectorPlate.class, "filteredRedirectorPlate");
		registerTE(TileEntityFilteredSuperLubricentPlatform.class, "filteredSuperLubricentPlatform");
		registerTE(TileEntityRedstoneObserver.class, "redstoneObserver");
		registerTE(TileEntityBiomeRadar.class, "biomeRadar");
		registerTE(TileEntityIronDropper.class, "ironDropper");
		registerTE(TileEntityInventoryRerouter.class, "inventoryRerouter");
		registerTE(TileEntityRuneBase.class, "runeBase");
		registerTE(TileEntitySlimeCube.class, "slimeCore");
		registerTE(TileEntityPeaceCandle.class, "peaceCandle");
		registerTE(TileEntityNotificationInterface.class, "notificationInterface");
		registerTE(TileEntityInventoryTester.class, "inventoryTester");
		registerTE(TileEntityFlooBrick.class, "flooBrick");
		registerTE(TileEntityGlobalChatDetector.class, "globalChatDetector");
		registerTE(TileEntityBlockDestabilizer.class, "blockDestabilizer");
		registerTE(TileEntityAdvancedRedstoneRepeater.class, "configurableRepeater");
		registerTE(TileEntityAncientFurnace.class, "ancientFurnace");
		registerTE(TileEntitySoundBox.class, "soundBox");
		registerTE(TileEntitySoundDampener.class, "soundDampener");
		registerTE(TileEntityAdvancedRedstoneTorch.class, "advancedRedstoneTorch");
		registerTE(TileEntityBlockDiaphanous.class, "diaphanousBlock");
		registerTE(TileEntitySpectreLens.class, "spectreLens");
		registerTE(TileEntitySpectreEnergyInjector.class, "spectreEnergyInjector");
		registerTE(TileEntitySpectreCoil.class, "spectreCoil");
		registerTE(TileEntityProcessingPlate.class, "processingPlate");
		registerTE(TileEntityExtractionPlate.class, "extractionPlate");
		registerTE(TileEntityIgniter.class, "igniter");
		registerTE(TileEntityLinkOrb.class, "linkOrb");

		registerTE(TileEntityItemCollector.class, "itemCollector");
		registerTE(TileEntityAdvancedItemCollector.class, "advancedItemCollector");
	}

	private static void registerTE(Class<? extends TileEntity> clazz, String name)
	{
		GameRegistry.registerTileEntity(clazz, "randomthings:" + name);
	}
}
