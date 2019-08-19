package lumien.randomthings.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("randomthings")
public class ModBlocks
{
	@ObjectHolder("fertilized_dirt")
	public static Block FERTILIZED_DIRT;

	@ObjectHolder("rainbow_lamp")
	public static Block RAINBOW_LAMP;


	@ObjectHolder("block_of_sticks")
	public static Block BLOCK_OF_STICKS;

	@ObjectHolder("block_of_sticks_returning")
	public static Block BLOCK_OF_STICKS_RETURNING;


	@ObjectHolder("platform_oak")
	public static Block PLATFORM_OAK;

	@ObjectHolder("platform_spruce")
	public static Block PLATFORM_SPRUCE;

	@ObjectHolder("platform_birch")
	public static Block PLATFORM_BIRCH;

	@ObjectHolder("platform_jungle")
	public static Block PLATFORM_JUNGLE;

	@ObjectHolder("platform_acacia")
	public static Block PLATFORM_ACACIA;

	@ObjectHolder("platform_darkoak")
	public static Block PLATFORM_DARKOAK;

	public static void registerBlocks(RegistryEvent.Register<Block> blockRegistryEvent)
	{
		IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

		registry.register(new FertilizedDirtBlock().setRegistryName("fertilized_dirt"));
		registry.register(new RainbowLampBlock().setRegistryName("rainbow_lamp"));

		registry.register(new SticksBlock(false).setRegistryName("block_of_sticks"));
		registry.register(new SticksBlock(true).setRegistryName("block_of_sticks_returning"));

		registry.register(new PlatformBlock().setRegistryName("platform_oak"));
		registry.register(new PlatformBlock().setRegistryName("platform_spruce"));
		registry.register(new PlatformBlock().setRegistryName("platform_birch"));
		registry.register(new PlatformBlock().setRegistryName("platform_jungle"));
		registry.register(new PlatformBlock().setRegistryName("platform_acacia"));
		registry.register(new PlatformBlock().setRegistryName("platform_darkoak"));
	}
}
