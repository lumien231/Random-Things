package lumien.randomthings.client.mesh;

import java.util.Map;

import gnu.trove.map.hash.TIntObjectHashMap;
import lumien.randomthings.util.ReflectionUtil;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.registries.IRegistryDelegate;

public class SoundRecorderMesh implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		boolean recording = false;

		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("recording"))
		{
			recording = true;
		}

		return new ModelResourceLocation("randomthings:soundrecorder_" + (recording ? "active" : "idle"));
	}

}
