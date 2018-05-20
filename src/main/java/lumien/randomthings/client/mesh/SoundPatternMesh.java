package lumien.randomthings.client.mesh;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SoundPatternMesh implements ItemMeshDefinition
{

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		NBTTagCompound compound;
		if (stack.hasTagCompound() && ((compound = stack.getTagCompound()).hasKey("sound")))
		{
			return new ModelResourceLocation("randomthings:soundpattern_full");
		}
		else
		{
			return new ModelResourceLocation("randomthings:soundpattern_empty");
		}
	}

}
