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

	@ObjectHolder("block_of_sticks")
	public static Block BLOCK_OF_STICKS;

	@ObjectHolder("block_of_sticks_returning")
	public static Block BLOCK_OF_STICKS_RETURNING;

	@ObjectHolder("rainbow_lamp")
	public static Block RAINBOW_LAMP;

	public static void registerBlocks(RegistryEvent.Register<Block> blockRegistryEvent)
	{
		IForgeRegistry<Block> registry = blockRegistryEvent.getRegistry();

		registry.register(new FertilizedDirtBlock().setRegistryName("fertilized_dirt"));

		registry.register(new SticksBlock(false).setRegistryName("block_of_sticks"));
		registry.register(new SticksBlock(true).setRegistryName("block_of_sticks_returning"));

		registry.register(new RainbowLampBlock().setRegistryName("rainbow_lamp"));
	}
}
