package lumien.randomthings.tileentity;

import lumien.randomthings.tileentity.cores.TileEntityEnderCore;
import lumien.randomthings.tileentity.cores.TileEntityNatureCore;
import lumien.randomthings.tileentity.cores.TileEntityNetherCore;
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
		registerTE(TileEntityNetherCore.class, "netherCore");
		registerTE(TileEntityEnderCore.class, "enderCore");
		registerTE(TileEntityChatDetector.class, "chatDetector");
		registerTE(TileEntityRedstoneInterface.class, "redstoneInterface");
		registerTE(TileEntityImbuingStation.class, "imbuingStation");
		registerTE(TileEntitySpecialChest.class, "specialChest");
		registerTE(TileEntityAnalogEmitter.class, "analogEmitter");
		registerTE(TileEntityFluidDisplay.class, "fluidDisplay");
		registerTE(TileEntityCustomWorkbench.class, "customWorkbench");
		registerTE(TileEntityEnderMailbox.class, "enderMailbox");
		registerTE(TileEntityEntityDetector.class, "entityDetector");
		registerTE(TileEntityPotionVaporizer.class, "potionVaporizer");
		registerTE(TileEntityVoxelProjector.class, "voxelProjector");
		registerTE(TileEntityItemCollector.class, "itemCollector");
		registerTE(TileEntityShieldRod.class, "shieldRod");
	}

	private static void registerTE(Class<? extends TileEntity> clazz, String name)
	{
		GameRegistry.registerTileEntity(clazz, "randomthings:" + name);
	}
}
