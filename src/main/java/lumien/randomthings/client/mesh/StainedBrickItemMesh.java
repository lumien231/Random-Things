package lumien.randomthings.client.mesh;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class StainedBrickItemMesh implements ItemMeshDefinition
{
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return new ModelResourceLocation("randomthings:stainedBrick/" + EnumDyeColor.byMetadata(stack.getItemDamage()).getUnlocalizedName(), "inventory");
	}

}
