package lumien.randomthings.tileentity;

import lumien.randomthings.tileentity.cores.TileEntityEnderCore;
import lumien.randomthings.tileentity.cores.TileEntityNatureCore;
import lumien.randomthings.tileentity.cores.TileEntityNetherCore;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntitys
{
	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityPlayerInterface.class, "playerInterface");
		GameRegistry.registerTileEntity(TileEntityCreativePlayerInterface.class, "creativePlayerInterface");
		GameRegistry.registerTileEntity(TileEntityOnlineDetector.class, "onlineDetector");
		GameRegistry.registerTileEntity(TileEntityEnderBridge.class, "enderBridge");
		GameRegistry.registerTileEntity(TileEntityPrismarineEnderBridge.class, "prismarineEnderBridge");
		GameRegistry.registerTileEntity(TileEntityNatureCore.class, "natureCore");
		GameRegistry.registerTileEntity(TileEntityNetherCore.class, "netherCore");
		GameRegistry.registerTileEntity(TileEntityEnderCore.class, "enderCore");
		GameRegistry.registerTileEntity(TileEntityChatDetector.class, "chatDetector");
		GameRegistry.registerTileEntity(TileEntityRedstoneInterface.class, "redstoneInterface");
		GameRegistry.registerTileEntity(TileEntityImbuingStation.class, "imbuingStation");
		GameRegistry.registerTileEntity(TileEntitySpecialChest.class, "specialChest");
	}
}
