package lumien.randomthings.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

import lumien.randomthings.RandomThings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks
{
	@ObjectHolder("randomthings:fertilized_dirt")
	public static Block FERTILIZED_DIRT;
	
	
	private static final Logger LOGGER = LogManager.getLogger();

	static Type BlockAnnotation = Type.getType(RTBlock.class);

	public static List<Block> itemBlocksToRegister = new ArrayList<Block>();

	public static void registerBlocks(RegistryEvent.Register<Block> blockRegistryEvent)
	{
		ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> BlockAnnotation.equals(a.getAnnotationType())).forEach(ad -> {
			
			LOGGER.info("Attempting to register...");
			try
			{
				String name = (String) ad.getAnnotationData().get("value");
				Class<?> blockClass = Class.forName(ad.getMemberName());
				
				LOGGER.info(" - " + name);
				
				LOGGER.info("Group: " + RandomThings.INSTANCE.getItemGroup());

				Block blockInstance = (Block) blockClass.newInstance();
				blockInstance.setRegistryName(name);

				blockRegistryEvent.getRegistry().register(blockInstance);

				itemBlocksToRegister.add(blockInstance);
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
			{
				LOGGER.error("Error registering class as block: " + ad.getMemberName());
				e.printStackTrace();
			}
		});
	}

	private static void registerWithItem(Block b, String name)
	{
		b.setRegistryName(name);

	}
}
