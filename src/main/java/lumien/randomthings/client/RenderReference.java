package lumien.randomthings.client;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;

public class RenderReference
{
	public static final ItemCameraTransforms BLOCK_ITEM_TRANSFORM;
	
	static
	{
		Vector3f rotation = new Vector3f(10, -45, 170);
		Vector3f translation = new Vector3f(0, 1.5F, -2.75F);
		translation.scale(0.0625F);
		Vector3f scale = new Vector3f(0.375F, 0.375F, 0.375F);
		BLOCK_ITEM_TRANSFORM = new ItemCameraTransforms(new ItemTransformVec3f(rotation, translation, scale), ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}
}
