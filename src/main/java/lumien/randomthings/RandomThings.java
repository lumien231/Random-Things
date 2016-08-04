package lumien.randomthings;

import java.util.List;

import org.apache.logging.log4j.Logger;

import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.GuiHandler;
import lumien.randomthings.config.ModConfiguration;
import lumien.randomthings.container.SyncHandler;
import lumien.randomthings.entitys.ModEntitys;
import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.handler.magicavoxel.ServerModelLibrary;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.RTCreativeTab;
import lumien.randomthings.lib.Reference;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.recipes.ModRecipes;
import lumien.randomthings.tileentity.ModTileEntitys;
import lumien.randomthings.tileentity.TileEntityEnderAnchor;
import lumien.randomthings.worldgen.WorldGenCores;
import lumien.randomthings.worldgen.WorldGenPlants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class RandomThings implements LoadingCallback
{
	@Instance(Reference.MOD_ID)
	public static RandomThings instance;

	@SidedProxy(clientSide = "lumien.randomthings.client.ClientProxy", serverSide = "lumien.randomthings.CommonProxy")
	public static CommonProxy proxy;

	public RTCreativeTab creativeTab;

	public Logger logger;

	public ModConfiguration configuration;

	ASMDataTable asmDataTable;
	
	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		asmDataTable = event.getAsmData();

		creativeTab = new RTCreativeTab();
		logger = event.getModLog();

		configuration = new ModConfiguration();
		configuration.preInit(event);

		ModBlocks.load(event);
		ModItems.load(event);
		ModTileEntitys.register();
		ModEntitys.init();
		ModPotions.preInit(event);
		proxy.registerModels();

		RTEventHandler eventHandler = new RTEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);
		FMLCommonHandler.instance().bus().register(eventHandler);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		PacketHandler.init();

		ForgeChunkManager.setForcedChunkLoadingCallback(this, this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModRecipes.register();
		ModDimensions.register();

		GameRegistry.registerWorldGenerator(new WorldGenPlants(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenCores(), 1000);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.registerRenderers();
		SyncHandler.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new RTCommand());
		ServerModelLibrary.getInstance().refresh();
	}

	public ASMDataTable getASMData()
	{
		return asmDataTable;
	}

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket t : tickets)
		{
			NBTTagCompound compound = t.getModData();
			TileEntity te = world.getTileEntity(new BlockPos(compound.getInteger("posX"), compound.getInteger("posY"), compound.getInteger("posZ")));
			if (te != null && te instanceof TileEntityEnderAnchor)
			{
				TileEntityEnderAnchor anchor = (TileEntityEnderAnchor) te;
				anchor.setTicket(t);

				ForgeChunkManager.forceChunk(t, world.getChunkFromBlockCoords(anchor.getPos()).getChunkCoordIntPair());
			}
		}
	}
}
