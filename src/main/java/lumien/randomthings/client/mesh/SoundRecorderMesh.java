package lumien.randomthings.client.mesh;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

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
