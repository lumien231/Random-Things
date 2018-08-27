package lumien.randomthings.client.mesh;

import java.util.Map;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IRegistryDelegate;

public class PortKeyMesh implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		NBTTagCompound camo = stack.getSubCompound("camo");

		if (camo != null)
		{
			ItemStack camoStack = new ItemStack(camo.getCompoundTag("stack"));
			Item item = camoStack.getItem();
			int meta = camoStack.getItemDamage();

			Map<IRegistryDelegate<Item>, Int2ObjectMap<ModelResourceLocation>> modelMap = ReflectionUtil.getModelMap();

			if (modelMap != null && modelMap.containsKey(item.delegate))
			{
				Int2ObjectMap<ModelResourceLocation> metaMap = modelMap.get(item.delegate);

				if (metaMap.containsKey(meta))
				{
					return metaMap.get(meta);
				}
			}
		}
		return new ModelResourceLocation("randomthings:portkey");
	}

}
