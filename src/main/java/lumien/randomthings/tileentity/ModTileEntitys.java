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
		registerTE(TileEntityBasicRedstoneInterface.class, "redstoneInterface");
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
		registerTE(TileEntityItemSealer.class, "itemSealer");
		registerTE(TileEntityItemCorrector.class, "itemCorrector");
		registerTE(TileEntityItemRejuvenator.class, "itemRejuvenator");
		registerTE(TileEntityFilteredItemRedirector.class, "filteredItemRedirector");
		registerTE(TileEntityFilteredSuperLubricentPlatform.class, "filteredSuperLubricentPlatform");
		registerTE(TileEntityRedstoneObserver.class, "redstoneObserver");
		registerTE(TileEntityBiomeRadar.class,"biomeRadar");
		registerTE(TileEntityIronDropper.class,"ironDropper");
		registerTE(TileEntityItemProjector.class,"itemProjector");

		registerTE(TileEntityItemCollector.class, "itemCollector");
		registerTE(TileEntityAdvancedItemCollector.class, "advancedItemCollector");
	}

	private static void registerTE(Class<? extends TileEntity> clazz, String name)
	{
		GameRegistry.registerTileEntity(clazz, "randomthings:" + name);
	}
}
