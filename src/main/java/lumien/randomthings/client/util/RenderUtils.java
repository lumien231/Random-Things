package lumien.randomthings.client.util;

import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils
{

	public static void drawCube(float posX, float posY, float posZ, float width, float length, float height, int red, int green, int blue, int alpha)
	{
		GlStateManager.disableLighting();

		Tessellator t = Tessellator.getInstance();
		BufferBuilder wr = t.getBuffer();

		GlStateManager.disableTexture();

		GlStateManager.translated(posX, posY, posZ);

		wr.begin(7, DefaultVertexFormats.POSITION_COLOR);

		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P4

		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P4

		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P4

		t.draw();

		GlStateManager.translated(-posX, -posY, -posZ);
		GlStateManager.enableTexture();

		GlStateManager.enableLighting();
	}

	public static void drawCube(float posX, float posY, float posZ, float size, int red, int green, int blue, int alpha)
	{
		drawCube(posX, posY, posZ, size, size, size, red, green, blue, alpha);
	}

	public static void enableDefaultBlending()
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}
