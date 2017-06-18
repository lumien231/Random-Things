package lumien.randomthings.handler;

import lumien.randomthings.lib.ILuminousItem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class LuminousHandler
{
	static Tessellator tessellator = null;
	static ILuminousItem luminousItem = null;
	static ItemStack stack;
	static float backUpX;
	static float backUpY;
	static BufferBuilder luminousBuffer;

	public static void luminousHookStart(ItemStack stack, BufferBuilder buffer)
	{
		if (stack.getItem() instanceof ILuminousItem)
		{
			luminousItem = (ILuminousItem) stack.getItem();
			LuminousHandler.stack = stack;

			backUpX = OpenGlHelper.lastBrightnessX;
			backUpY = OpenGlHelper.lastBrightnessY;

			if (tessellator == null)
			{
				tessellator = Tessellator.getInstance();
			}

			luminousBuffer = buffer;
		}
	}

	public static void luminousHookPre(int tint)
	{
		if (luminousItem != null && tint != -1 && luminousItem.shouldGlow(stack, tint))
		{
			tessellator.draw();
			luminousBuffer.begin(7, DefaultVertexFormats.ITEM);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

			GlStateManager.disableLighting();
		}
	}

	public static void luminousHookPost()
	{
		if (luminousItem != null)
		{
			tessellator.draw();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, backUpX, backUpY);
			luminousBuffer.begin(7, DefaultVertexFormats.ITEM);

			GlStateManager.enableLighting();
		}
	}

	public static void luminousHookEnd()
	{
		if (luminousItem != null)
		{
			luminousBuffer = null;
			luminousItem = null;
			stack = null;
		}
	}
}
