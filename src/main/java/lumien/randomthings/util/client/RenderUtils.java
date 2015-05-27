package lumien.randomthings.util.client;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class RenderUtils
{
	static Gui gui = new Gui();

	public static void drawCube(float posX, float posY, float posZ, float size, float red, float green, float blue, float alpha)
	{
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		
		Tessellator t = Tessellator.getInstance();
		WorldRenderer wr = t.getWorldRenderer();
		
		GlStateManager.disableTexture2D();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);
		
		wr.startDrawingQuads();
		wr.setColorRGBA_F(red, green, blue, alpha);
		
		wr.addVertex(0F, 0F, 0F); // P1
		wr.addVertex(0F, size, 0F); // P2
		wr.addVertex(size, size, 0F); // P3
		wr.addVertex(size, 0F, 0F); // P4

		wr.addVertex(size, size, 0F); // P1
		wr.addVertex(size, size, size); // P2
		wr.addVertex(size, 0F, size); // P3
		wr.addVertex(size, 0F, 0F); // P4

		wr.addVertex(size, size, size); // P1
		wr.addVertex(0F, size, size); // P1
		wr.addVertex(0F, 0F, size); // P1
		wr.addVertex(size, 0F, size); // P1

		wr.addVertex(0F, size, size); // P1
		wr.addVertex(0F, size, 0F); // P1
		wr.addVertex(0F, 0F, 0F); // P1
		wr.addVertex(0F, 0F, size); // P1

		wr.addVertex(0F, 0F, 0F); // P1
		wr.addVertex(size, 0F, 0F); // P1
		wr.addVertex(size, 0F, size); // P1
		wr.addVertex(0F, 0F, size); // P1

		wr.addVertex(0F, size, 0F); // P1
		wr.addVertex(0F, size, size); // P2
		wr.addVertex(size, size, size); // P3
		wr.addVertex(size, size, 0F); // P4
		
		t.draw();
		
		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}

	public static void enableDefaultBlending()
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}
