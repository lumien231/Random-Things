package lumien.randomthings.container;

import lumien.randomthings.tileentity.AdvancedRedstoneTorchTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder("randomthings")
public class ModContainerTypes
{
	@ObjectHolder("advanced_redstone_torch")
	public static ContainerType<AdvancedRedstoneTorchContainer> ADVANCED_REDSTONE_TORCH;

	public static void registerContainerTypes(Register<ContainerType<?>> containerTypeRegistryEvent)
	{
		IForgeRegistry<ContainerType<?>> registry = containerTypeRegistryEvent.getRegistry();

		registry.register(IForgeContainerType.create(AdvancedRedstoneTorchContainer::new).setRegistryName("advanced_redstone_torch"));
	}
}