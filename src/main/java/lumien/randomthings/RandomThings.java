package lumien.randomthings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lumien.randomthings.blocks.FertilizedDirtBlock;
import lumien.randomthings.blocks.ModBlocks;
import lumien.randomthings.items.ModItems;
import lumien.randomthings.lib.ModConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(ModConstants.MOD_ID)
public class RandomThings
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static RandomThings INSTANCE;
	
	ItemGroup rtItemGroup;

	public RandomThings()
	{
		INSTANCE = this;
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		
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
	}
	
	public void setup(final FMLCommonSetupEvent event)
	{

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
			RandomThings.INSTANCE.rtItemGroup = new ItemGroup("randomthings")
			{
				@Override
				public ItemStack createIcon()
				{
					return new ItemStack(ModBlocks.FERTILIZED_DIRT);
				}
			};
			
			ModItems.registerItems(itemRegistryEvent);
		}
	}

	public ItemGroup getItemGroup()
	{
		return rtItemGroup;
	}
}
