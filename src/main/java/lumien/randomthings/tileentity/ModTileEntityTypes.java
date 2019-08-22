package lumien.randomthings.tileentity;

import java.util.function.Supplier;

import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("randomthings")
public class ModTileEntityTypes
{
	@ObjectHolder("advanced_redstone_torch")
	public static TileEntityType<AdvancedRedstoneTorchTileEntity> ADVANCED_REDSTONE_TORCH;


	public static void registerTypes(RegistryEvent.Register<TileEntityType<?>> typeRegistryEvent)
	{
		IForgeRegistry<TileEntityType<?>> registry = typeRegistryEvent.getRegistry();

		registerSimple(registry, "advanced_redstone_torch", AdvancedRedstoneTorchTileEntity::new, ModBlocks.ADVANCED_REDSTONE_TORCH, ModBlocks.ADVANCED_WALL_REDSTONE_TORCH);
	}

	private static void registerSimple(IForgeRegistry<TileEntityType<?>> registry, String name, Supplier<? extends TileEntity> factoryIn, Block... validBlocks)
	{
		registry.register(TileEntityType.Builder.create(factoryIn, validBlocks).build(null).setRegistryName(name));
	}
}