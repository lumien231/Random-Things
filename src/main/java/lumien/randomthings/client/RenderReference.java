package lumien.randomthings.client;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;

public class RenderReference
{
	public static final ItemCameraTransforms BLOCK_ITEM_TRANSFORM;

	static
	{
		BLOCK_ITEM_TRANSFORM = new ItemCameraTransforms(new ItemTransformVec3f(new Vector3f(75, 45, 0), new Vector3f(0, 0.15625f, 0), new Vector3f(0.375f, 0.375f, 0.375f)), new ItemTransformVec3f(new Vector3f(75, 45, 0), new Vector3f(0, 0.15625f, 0), new Vector3f(0.375f, 0.375f, 0.375f)), new ItemTransformVec3f(new Vector3f(0, 225, 0), new Vector3f(0, 0, 0), new Vector3f(0.40f, 0.40f, 0.40f)), new ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0, 0, 0), new Vector3f(0.40f, 0.40f, 0.40f)), new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(0.50f, 0.50f, 0.50f)), new ItemTransformVec3f(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(0.625f, 0.625f, 0.625f)), new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0.1875f, 0), new Vector3f(0.25f, 0.25f, 0.25f)), new ItemTransformVec3f(new Vector3f(0, 0, 0), new Vector3f(0, 0f, 0), new Vector3f(0.5f, 0.5f, 0.5f)));
	}
}
