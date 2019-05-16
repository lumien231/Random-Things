package lumien.randomthings;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import lumien.randomthings.asm.ClassTransformer;
import lumien.randomthings.asm.CustomClassWriter;
import lumien.randomthings.asm.confirmer.ServerConfirmer;
import lumien.randomthings.biomes.ModBiomes;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.GuiHandler;
import lumien.randomthings.client.ModSounds;
import lumien.randomthings.config.Features;
import lumien.randomthings.config.ModConfiguration;
import lumien.randomthings.container.SyncHandler;
import lumien.randomthings.enchantment.ModEnchantments;
import lumien.randomthings.entitys.ModEntitys;
import lumien.randomthings.handler.ModDimensions;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.handler.compability.oc.OCComp;
import lumien.randomthings.handler.magicavoxel.ServerModelLibrary;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.diviningrod.ItemDiviningRod;
import lumien.randomthings.lib.RTCreativeTab;
import lumien.randomthings.lib.Reference;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.recipes.ModRecipes;
import lumien.randomthings.tileentity.ModTileEntitys;
import lumien.randomthings.tileentity.TileEntityEnderAnchor;
import lumien.randomthings.worldgen.WorldGenAncientFurnace;
import lumien.randomthings.worldgen.WorldGenCores;
import lumien.randomthings.worldgen.WorldGenEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions = "[1.12,1.13)", certificateFingerprint = Reference.MOD_FINGERPRINT, dependencies = "after:jei@[4.7.11.100,);")
public class RandomThings implements LoadingCallback
{
	@Instance(Reference.MOD_ID)
	public static RandomThings instance;

	@SidedProxy(clientSide = "lumien.randomthings.client.ClientProxy", serverSide = "lumien.randomthings.CommonProxy")
	public static CommonProxy proxy;

	@SidedProxy(clientSide = "lumien.randomthings.asm.confirmer.ClientConfirmer", serverSide = "lumien.randomthings.asm.confirmer.ServerConfirmer")
	public static ServerConfirmer asmConfirmer;

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
		ModEnchantments.preInit(event);
		ModBiomes.preInit(event);
		ModSounds.preInit(event);
		proxy.registerModels();

		RTEventHandler eventHandler = new RTEventHandler();
		MinecraftForge.EVENT_BUS.register(eventHandler);

		WorldGenEventHandler worldGenEventHandler = new WorldGenEventHandler();
		MinecraftForge.TERRAIN_GEN_BUS.register(worldGenEventHandler);
		MinecraftForge.EVENT_BUS.register(worldGenEventHandler);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		PacketHandler.init();

		ForgeChunkManager.setForcedChunkLoadingCallback(this, this);

		// IMC
		if (Features.MAGNETIC_ENCHANTMENT)
			FMLInterModComms.sendMessage("enderio", "recipe:xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><recipes>" + "<recipe name=\"Enchanter: randomthings:magnetic\" required=\"true\" disabled=\"false\"><enchanting>" + "<input name=\"oredict:blockIron\" amount=\"4\"/><enchantment name=\"" + "randomthings" + ":magnetic\" costMultiplier=\"1\"/></enchanting></recipe></recipes>");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModRecipes.register();
		ModDimensions.register();

		GameRegistry.registerWorldGenerator(new WorldGenCores(), 1000);
		GameRegistry.registerWorldGenerator(new WorldGenAncientFurnace(), 1000);

		if (Loader.isModLoaded("opencomputers"))
		{
			OCComp.init();
		}
		
		FMLInterModComms.sendMessage("Thaumcraft", "harvestClickableCrop", new ItemStack(ModBlocks.lotus, 1, 3));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.registerRenderers();
		SyncHandler.postInit(event);

		logger.log(Level.DEBUG, "Confirming ASM Transformations...");

		// Confirm all ASM Patches
		asmConfirmer.confirm();

		logger.log(Level.DEBUG, ClassTransformer.transformations + "/17 ASM Transformations were applied.");

		CustomClassWriter.customClassLoader = null;

		ModRecipes.addGlowingMushroomRecipes();

		// ThermalExpansionComp.postInit(event); NU

		ItemDiviningRod.postInit();
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
			if (te != null && te instanceof TileEntityEnderAnchor && Features.ENDER_ANCHOR_CHUNKLOADING)
			{
				TileEntityEnderAnchor anchor = (TileEntityEnderAnchor) te;
				anchor.setTicket(t);

				ForgeChunkManager.forceChunk(t, world.getChunkFromBlockCoords(anchor.getPos()).getPos());
			}
			else
			{
				ForgeChunkManager.releaseTicket(t);
			}
		}
	}
}
