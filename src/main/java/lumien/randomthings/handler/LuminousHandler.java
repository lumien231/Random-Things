package lumien.randomthings.handler;

import lumien.randomthings.lib.ILuminous;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LuminousHandler
{
	static Tessellator tessellator = null;
	static boolean luminousItem = false;
	static float backUpX;
	static float backUpY;
	static VertexBuffer luminousBuffer;

	public static void luminousHookStart(ItemStack stack, VertexBuffer buffer)
	{
		luminousItem = stack.getItem() instanceof ILuminous;
		
		if (luminousItem)
		{
			backUpX = OpenGlHelper.lastBrightnessX;
			backUpY = OpenGlHelper.lastBrightnessY;

			if (tessellator == null)
			{
				tessellator = Tessellator.getInstance();
			}

			luminousBuffer = buffer;
		}
	}

	public static int luminousHookPre(int tint)
	{
		if (luminousItem && tint == -2)
		{
			tessellator.draw();
			luminousBuffer.begin(7, DefaultVertexFormats.ITEM);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
			
			GlStateManager.disableLighting();
			return -1;
		}

		return tint;
	}

	public static void luminousHookPost()
	{
		if (luminousItem)
		{
			tessellator.draw();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, backUpX, backUpY);
			luminousBuffer.begin(7, DefaultVertexFormats.ITEM);
			
			GlStateManager.enableLighting();
		}
	}
	
	public static void luminousHookEnd()
	{
		if (luminousItem)
		{
			luminousBuffer = null;
			luminousItem = false;
		}
	}
}
