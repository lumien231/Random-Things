package lumien.randomthings.client.util;

import com.mojang.blaze3d.platform.GlStateManager;

import java.util.Random;
import java.util.function.Function;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderUtils
{
	public static void drawFunctionLinePart(Function<Float, Vector3f> function, float lineLength, float progress)
	{
		// Setup Render
		GlStateManager.enableDepthTest();
		GlStateManager.disableTexture();
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.lineWidth(2f);
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);

		// Numbers
		float neededProgress = progress + lineLength;
		float mappedProgress = progress * neededProgress;

		float progressStart = Math.max(0, mappedProgress - lineLength);
		float progressEnd = Math.min(mappedProgress, 1);

		float range = progressEnd - progressStart;

		int lineSegments = 100;

		float step = range / lineSegments;

		// Render
		GlStateManager.lineWidth(2F);
		GlStateManager.begin(GL11.GL_LINE_STRIP);

		if (range != 0)
		{
			for (int seg = 0; seg < lineSegments; seg++)
			{
				float p = progressStart + seg * step;
				Vector3f vec = function.apply(p);

				float alpha = seg > lineSegments - 10 ? (10 - (seg - (lineSegments - 10))) / 10f : 1f;
				alpha = 0.5F;

				GlStateManager.color4f(1, 0, 0, alpha);
				GlStateManager.vertex3f(vec.x, vec.y, vec.z);
			}
		}

		Vector3f last = function.apply(progressEnd);

		GlStateManager.vertex3f(last.x, last.y, last.z);

		GlStateManager.end();

		GlStateManager.enableTexture();
		RenderUtils.enableDefaultBlending();
	}

	public static Function<Float, Vector3f> getLineFunction(Vector3f from, Vector3f to)
	{
		return (progress) -> {
			Vector3f between = new Vector3f();
			between.interpolate(from, to, progress);
			return between;
		};
	}

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

	public static void drawCylinder(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableTexture();

		Tessellator t = Tessellator.getInstance();
		BufferBuilder wr = t.getBuffer();
		ActiveRenderInfo ari = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

		GL11.glPointSize(1.0f);
		wr.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);

		float radius = 2;

		float oX = 2;
		float oY = 2;
		float oZ = 0;

		Random rng = new Random(5);

		for (int i = 0; i < 1; i++)
		{
			double alpha = rng.nextFloat() * Math.PI * 2;
			double beta = rng.nextFloat() * Math.PI;

			float x = (float) (radius * Math.sin(alpha)) + oX;
			float y = (float) (radius * Math.cos(beta) * Math.cos(alpha)) + oY;
			float z = (float) (radius * Math.sin(beta) * Math.cos(alpha)) + oZ;

			wr.pos(x1 + x, y1 + y, z1 + z).color(255, 0, 0, 255).endVertex();
		}

		t.draw();



		GlStateManager.enableTexture();
		GlStateManager.enableLighting();
	}

	public static void enableDefaultBlending()
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
}
