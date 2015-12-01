package lumien.randomthings.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import org.lwjgl.opengl.GL11;

public class RenderUtils
{
	static Gui gui = new Gui();

	public static void drawCube(float posX, float posY, float posZ, float width, float length, float height, float red, float green, float blue, float alpha)
	{
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		Tessellator t = Tessellator.getInstance();
		WorldRenderer wr = t.getWorldRenderer();

		GlStateManager.disableTexture2D();

		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);

		wr.func_181668_a(7, DefaultVertexFormats.field_181706_f);

		int r = (int) (1F * 255F * red);
		int g = (int) (1F * 255F * green);
		int b = (int) (1F * 255F * blue);
		int a = (int) (1F * 255F * alpha);

		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(width, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(width, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

		wr.func_181662_b(width, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(width, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(width, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(width, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

		wr.func_181662_b(width, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(width, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(width, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(width, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, length).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(width, height, length).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(width, height, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

		t.draw();

		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}

	public static void drawCube(float posX, float posY, float posZ, float size, float red, float green, float blue, float alpha)
	{
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		Tessellator t = Tessellator.getInstance();
		WorldRenderer wr = t.getWorldRenderer();

		GlStateManager.disableTexture2D();
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);

		wr.func_181668_a(7, DefaultVertexFormats.field_181706_f);

		int r = (int) (1F * 255F * red);
		int g = (int) (1F * 255F * green);
		int b = (int) (1F * 255F * blue);
		int a = (int) (1F * 255F * alpha);

		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(size, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(size, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

		wr.func_181662_b(size, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(size, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(size, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(size, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

		wr.func_181662_b(size, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(size, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(size, 0F, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(size, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, 0F, size).func_181669_b(r, g, b, a).func_181675_d(); // P1

		wr.func_181662_b(0F, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P1
		wr.func_181662_b(0F, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P2
		wr.func_181662_b(size, size, size).func_181669_b(r, g, b, a).func_181675_d(); // P3
		wr.func_181662_b(size, size, 0F).func_181669_b(r, g, b, a).func_181675_d(); // P4

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
