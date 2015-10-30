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
		registerTEBackward(TileEntityPlayerInterface.class, "playerInterface");
		registerTEBackward(TileEntityCreativePlayerInterface.class, "creativePlayerInterface");
		registerTEBackward(TileEntityOnlineDetector.class, "onlineDetector");
		registerTEBackward(TileEntityEnderBridge.class, "enderBridge");
		registerTEBackward(TileEntityPrismarineEnderBridge.class, "prismarineEnderBridge");
		registerTEBackward(TileEntityNatureCore.class, "natureCore");
		registerTEBackward(TileEntityNetherCore.class, "netherCore");
		registerTEBackward(TileEntityEnderCore.class, "enderCore");
		registerTEBackward(TileEntityChatDetector.class, "chatDetector");
		registerTEBackward(TileEntityRedstoneInterface.class, "redstoneInterface");
		registerTEBackward(TileEntityImbuingStation.class, "imbuingStation");
		registerTEBackward(TileEntitySpecialChest.class, "specialChest");
		registerTEBackward(TileEntityAnalogEmitter.class, "analogEmitter");
		registerTE(TileEntityFluidDisplay.class, "fluidDisplay");
		registerTE(TileEntityCustomWorkbench.class, "customWorkbench");
		registerTE(TileEntityEnderMailbox.class, "enderMailbox");
		registerTE(TileEntityEntityDetector.class,"entityDetector");
	}

	private static void registerTEBackward(Class<? extends TileEntity> clazz, String name)
	{
		GameRegistry.registerTileEntityWithAlternatives(clazz, "randomthings:" + name, name); // TODO: Remove alternative after some versions
	}

	private static void registerTE(Class<? extends TileEntity> clazz, String name)
	{
		GameRegistry.registerTileEntity(clazz, "randomthings:" + name);
	}
}
