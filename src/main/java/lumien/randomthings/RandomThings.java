package lumien.randomthings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lumien.randomthings.asm.AsmHandler;
import lumien.randomthings.block.FertilizedDirtBlock;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.renderer.DiviningRodRenderer;
import lumien.randomthings.client.screen.ModScreens;
import lumien.randomthings.client.vfx.VFXHandler;
import lumien.randomthings.container.ModContainerTypes;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.ModConstants;
import lumien.randomthings.network.RTPacketHandler;
import lumien.randomthings.tileentity.ModTileEntityTypes;
import lumien.randomthings.worldgen.BloodRoseFeature;
import lumien.randomthings.worldgen.ModFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;


@Mod(ModConstants.MOD_ID)
public class RandomThings
{
	private static final Logger LOGGER = LogManager.getLogger();

	public static RandomThings INSTANCE;

	public RandomThings()
	{
		INSTANCE = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

		MinecraftForge.EVENT_BUS.register(this);

		MinecraftForge.EVENT_BUS.addListener((UseHoeEvent event) -> {
			ItemUseContext context = event.getContext();

			World world = context.getWorld();
			BlockPos pos = context.getPos();
			BlockState state = world.getBlockState(pos);

			if (state.getBlock() == ModBlocks.FERTILIZED_DIRT && !state.get(FertilizedDirtBlock.TILLED))
			{
				event.setResult(Result.ALLOW);
				world.setBlockState(pos, state.with(FertilizedDirtBlock.TILLED, true));
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		});

		MinecraftForge.EVENT_BUS.addListener((ClientTickEvent event) -> {
			if (event.phase == TickEvent.Phase.END)
			{
				DiviningRodRenderer.get().tick();
				VFXHandler.INSTANCE.tick();
			}
		});
	}

	private void setupCommon(final FMLCommonSetupEvent event)
	{
		AsmHandler.modBlockLight(0F, 1);
		RTPacketHandler.register();
		
		ForgeRegistries.BIOMES.forEach(b -> {
			b.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(ModFeatures.BLOOD_ROSES, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_HEIGHTMAP_32, new FrequencyConfig(1)));
		});
	}

	private void setupClient(final FMLClientSetupEvent event)
	{
		ModScreens.register();

		MinecraftForge.EVENT_BUS.addListener((RenderWorldLastEvent rwl) -> {
			DiviningRodRenderer.get().render();
			VFXHandler.INSTANCE.render(rwl.getPartialTicks());
		});
	}



	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{
		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
		{
			ModBlocks.registerBlocks(blockRegistryEvent);
		}

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
		{
			ModItems.initItemGroup();

			ModItems.registerItems(itemRegistryEvent);
		}

		@SubscribeEvent
		public static void onTileEntityTypesRegistry(final RegistryEvent.Register<TileEntityType<?>> tileEntityTypeRegistryEvent)
		{
			ModTileEntityTypes.registerTypes(tileEntityTypeRegistryEvent);
		}

		@SubscribeEvent
		public static void onContainerTypesRegistry(final RegistryEvent.Register<ContainerType<?>> containerTypeRegistryEvent)
		{
			ModContainerTypes.registerContainerTypes(containerTypeRegistryEvent);
		}
		
		@SubscribeEvent
		public static void onFeaturesRegistry(final RegistryEvent.Register<Feature<?>> featureRegistryEvent) {
			ModFeatures.registerFeatures(featureRegistryEvent);
		}
	}
}
