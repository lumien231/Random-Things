package lumien.randomthings.util.client;

import java.awt.Color;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import lumien.randomthings.client.render.magiccircles.ColorFunctions;
import lumien.randomthings.client.render.magiccircles.IColorFunction;
import lumien.randomthings.client.render.magiccircles.ITriangleFunction;
import lumien.randomthings.handler.RTEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.Animation;

public class MKRRenderUtil
{
	static ResourceLocation mkrFont = new ResourceLocation("randomthings", "textures/entitys/mkr_font.png");

	public static void renderLightningArc(float x1, float y1, float z1, float x2, float y2, float z2)
	{
		Minecraft mc = Minecraft.getMinecraft();

		mc.entityRenderer.disableLightmap();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();

		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		for (int i = 0; i < 5; i++)
		{
			GlStateManager.glBegin(GL11.GL_LINE_STRIP);

			ColorUtil.applyColor(new Color(235, 63, 229, 200));

			Vector3f vec1 = new Vector3f(x1, y1, z1);
			Vector3f vec2 = new Vector3f(x2, y2, z2);

			Vector3f dif = Vector3f.sub(vec2, vec1, null);

			float length = dif.length();
			float passedLength = 0;


			GlStateManager.glVertex3f(x1, y1, z1);

			Random rng = new Random(i);

			while (length > 0.1F)
			{
				float nextPoint = (float) (rng.nextFloat() * 0.05F + 0.05F);
				passedLength += nextPoint;

				Vector3f nextPointVec = Vector3f.add(vec1, (Vector3f) new Vector3f(dif).normalise(null).scale(passedLength), null);

				GlStateManager.glVertex3f(nextPointVec.x + rng.nextFloat() * 0.1F - 0.05F, nextPointVec.y + rng.nextFloat() * 0.1F - 0.05F, nextPointVec.z + rng.nextFloat() * 0.1F - 0.05F);

				length -= nextPoint;
			}

			GlStateManager.glVertex3f(x2, y2, z2);

			GlStateManager.glEnd();
		}

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		mc.entityRenderer.enableLightmap();
	}

	public static void renderLinkOrb(BlockPos pos, double x, double y, double z)
	{
		float progress = ((RTEventHandler.clientAnimationCounter + pos.hashCode() + Animation.getPartialTickTime()) / 2) % 100;

		float rotation = RTEventHandler.clientAnimationCounter + pos.hashCode() + Animation.getPartialTickTime();

		y = y + Math.sin(rotation / 40) * 0.05;

		GlStateManager.translate(x, y, z);

		GlStateManager.rotate(-rotation, -0.5F, 0.5F, 0.5F);
		renderIcosahedron(x, y, z, 0.1F, ColorFunctions.constant(new Color(150, 10, 180, 200)).next(ColorFunctions.flicker(20, 20)).tt(progress));
		GlStateManager.rotate(rotation, -0.5F, 0.5F, 0.5F);

		GlStateManager.rotate(rotation, 0.5F, 0.5F, 0.5F);
		renderIcosahedron(x, y, z, 0.2F, ColorFunctions.constant(new Color(235, 63, 229, 200)).next(ColorFunctions.flicker(0, 20)).tt(progress));

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		GL11.glLineWidth(2);
		renderIcosahedron(x, y, z, 0.201F, (t) -> Color.WHITE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		GlStateManager.rotate(-rotation, 0.5F, 0.5F, 0.5F);

		GlStateManager.translate(-x, -y, -z);
	}

	public static void renderIcosahedron(double x, double y, double z, float sizeMod, ITriangleFunction triangleFunction)
	{
		Minecraft mc = Minecraft.getMinecraft();

		mc.entityRenderer.disableLightmap();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();

		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

		GlStateManager.color(1, 1, 1);

		float phi = (float) ((1 + Math.sqrt(5)) / 2);

		float[][] v = new float[][] { { phi, 1, 0 }, { -phi, 1, 0 }, { phi, -1, 0 }, { -phi, -1, 0 }, { 1, 0, phi }, { 1, 0, -phi }, { -1, 0, phi }, { -1, 0, -phi }, { 0, phi, 1 }, { 0, -phi, 1 }, { 0, phi, -1 }, { 0, -phi, -1 } };

		float[][][] mesh = new float[][][] { { v[0], v[8], v[4] }, { v[0], v[5], v[10] }, { v[2], v[4], v[9] }, { v[2], v[11], v[5] }, { v[1], v[6], v[8] }, { v[1], v[10], v[7] }, { v[3], v[9], v[6] }, { v[3], v[7], v[11] }, { v[0], v[10], v[8] }, { v[1], v[8], v[10] }, { v[2], v[9], v[11] }, { v[11], v[9], v[3] }, { v[4], v[2], v[0] }, { v[5], v[0], v[2] }, { v[6], v[1], v[3] }, { v[7], v[3], v[1] }, { v[8], v[6], v[4] }, { v[9], v[4], v[6] }, { v[10], v[5], v[7] }, { v[11], v[7], v[5] } };

		Color cB = new Color(255, 0, 255);

		float aP = RTEventHandler.clientAnimationCounter + Animation.getPartialTickTime();

		// GlStateManager.disableCull();

		GlStateManager.glBegin(GL11.GL_TRIANGLES);
		for (int ti = 0; ti < mesh.length; ti++)
		{
			float[][] vertices = mesh[ti];

			ColorUtil.applyColor(triangleFunction.apply(ti));

			for (int i = 0; i < vertices.length; i++)
			{
				float[] vertex = vertices[i];

				GlStateManager.glVertex3f(vertex[0] * sizeMod, vertex[1] * sizeMod, vertex[2] * sizeMod);
			}
		}

		GlStateManager.glEnd();

		// GlStateManager.enableCull();

		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		mc.entityRenderer.enableLightmap();
	}

	public static void circle1(double posX, double posY, double posZ, Color c1, Color c2, boolean fullCircle)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		RenderUtils.enableDefaultBlending();

		float progress = ((RTEventHandler.clientAnimationCounter + Animation.getPartialTickTime()) / 2) % 100;

		if (progress <= 100)
		{
			double radius = Math.sin(progress * (Math.PI / 20)) * 0.8f + 0.2f;

			if (progress * (Math.PI / 20) >= Math.PI / 2)
			{
				radius = 1f;
			}

			float rotation = (float) (360D / 100 * progress);

			double modY = Math.min(0.05, 0.05 / 8 * progress);
			posY += modY;

			GlStateManager.disableCull();

			GlStateManager.translate(posX, posY, posZ);
			GlStateManager.rotate(rotation, 0, 1, 0);

			renderCircleDecTriInner(radius / 4, ColorFunctions.alternate(c2, c1).tt(progress), 11);

			renderCircleDecTriPart3Tri(1.5 * radius / 4, radius / 4, ColorFunctions.alternate(c1, c2).next(ColorFunctions.flicker(1000, 10)).tt(progress), 30);

			renderCircleDecTriPart3Tri(2 * radius / 4, 1.5 * radius / 4, ColorFunctions.alternate(c2, c1).next(ColorFunctions.flicker(2000, 10)).tt(progress), 30);

			renderCircleDecTriPart3Tri(2.5 * radius / 4, 2 * radius / 4, ColorFunctions.alternate(c2, c1).next(ColorFunctions.limit(ColorFunctions.constant(c2.darker()), (i) -> {
				return i % 10 % 2 == 0;
			})).next(ColorFunctions.flicker(3000, 10)).tt(progress), 30);

			renderCircleDecTriPart3Tri(3 * radius / 4, 2.5 * radius / 4, ColorFunctions.alternate(c2, c1).next(ColorFunctions.flicker(4000, 10)).tt(progress), 30);

			renderCircleDecTriPart3Tri(radius, 3 * radius / 4, ColorFunctions.constant(c2).next(ColorFunctions.limit(ColorFunctions.constant(c1), (i) -> {
				return i % 2 == 1;
			})).next(ColorFunctions.flicker(5000, 10)).tt(progress), 30);

			if (fullCircle)
			{
				renderCircleDecOuterPart(radius, (i) -> {
					return c2;
				});
			}

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

			Color c1Dot = ColorUtil.brighter(c1, 20F);
			Color dotColor = new Color(c1Dot.getRed(), c1Dot.getGreen(), c1Dot.getBlue(), 50);
			ITriangleFunction outlineFunction = ITriangleFunction.from(progress, ColorFunctions.constant(dotColor));
			ITriangleFunction darkerFunction = outlineFunction.modColor((c) -> {
				return c.darker();
			});

			renderCircleDecTriInner(radius / 4, outlineFunction, 11);
			renderCircleDecTriPart3Tri(1.5 * radius / 4, radius / 4, darkerFunction, 30);
			renderCircleDecTriPart3Tri(2 * radius / 4, 1.5 * radius / 4, darkerFunction, 30);
			renderCircleDecTriPart3Tri(2.5 * radius / 4, 2 * radius / 4, darkerFunction, 30);
			renderCircleDecTriPart3Tri(radius, 3 * radius / 4, outlineFunction.modColor((c) -> {
				return c.darker().darker().darker().darker();
			}), 30);

			GlStateManager.rotate(-rotation, 0, 1, 0);
			GlStateManager.translate(-posX, -posY, -posZ);
			GlStateManager.enableTexture2D();

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}

	public static void circle2(double posX, double posY, double posZ, Color c1, Color c2, boolean fullCircle)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		RenderUtils.enableDefaultBlending();

		float progress = ((RTEventHandler.clientAnimationCounter + Animation.getPartialTickTime())) / 2 % 100;

		if (progress <= 100)
		{
			double radius = Math.sin(progress * (Math.PI / 20)) * 0.8f + 0.2f;

			if (progress * (Math.PI / 20) >= Math.PI / 2)
			{
				radius = 1f;
			}

			float rotation = (float) (360D / 100 * progress);

			double modY = Math.min(0.05, 0.05 / 8 * progress);
			posY += modY;

			GlStateManager.disableCull();

			GlStateManager.translate(posX, posY, posZ);
			GlStateManager.rotate(rotation, 0, 1, 0);

			renderCircleDecTriInner(radius / 4, (i) -> {
				return i % 4 == 0 ? c2 : ((i - 2) % 4 == 0 ? new Color(50, 50, 50, 50) : c1);
			}, 21, (p) -> {
				if (p % 2 == 0)
				{
					return 3;
				}
				else
				{
					return 1;
				}
			});

			renderCircleDecTriPartCross(1.5 * radius / 4, radius / 4, ColorFunctions.constant(c2).next(ColorFunctions.limit(ColorFunctions.constant(c1), (i) -> {
				return i % 6 < 4 && i / 6 % 2 != 0;
			})).tt(progress), 60);

			renderCircleDecTriPart3Tri(2 * radius / 4, 1.5 * radius / 4, ColorFunctions.alternate(c1, c2).next(ColorFunctions.flicker(0, 10)).tt(progress), 30);

			renderCircleDecTriPart3Tri(2.5 * radius / 4, 2 * radius / 4, ColorFunctions.alternate(c2, c1).next(ColorFunctions.flicker(500, 10)).tt(progress), 30);

			renderCircleDecTriPart5Tri(3 * radius / 4, 2.5 * radius / 4, ColorFunctions.alternateN(c2, c1, 5, 3).next(ColorFunctions.flicker(0, 10)).tt(progress), 60);

			renderCircleDecTriPart5Tri(radius, 3 * radius / 4, (i) -> {

				Color c = Color.getHSBColor(1F / 50 * i + progress / 100, 1, 1);

				return new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
			}, 50);

			if (fullCircle)
			{
				renderCircleDecOuterPart(radius, (i) -> {
					return c2;
				});
			}

			renderCircleDecOuterPart(radius, (t) -> {
				return new Color(255, 0, 0, 100);
			});

			renderCircleDecTriPart3Tri(radius + 0.1, radius + 0.05, (t) -> {
				return Color.RED;
			}, 30);

			GL11.glDisable(GL11.GL_LINE_SMOOTH);

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

			Color c1Dot = ColorUtil.brighter(c1, 20F);
			Color dotColor = new Color(c1Dot.getRed(), c1Dot.getGreen(), c1Dot.getBlue(), 50);
			ITriangleFunction dotColorFunction = ColorFunctions.constant(dotColor).tt(progress);

			renderCircleDecTriInner(radius / 4, dotColorFunction.modColor((c) -> {
				return dotColor;
			}), 11);

			renderCircleDecTriPart3Tri(2 * radius / 4, 1.5 * radius / 4, dotColorFunction.darker(), 30);
			renderCircleDecTriPart3Tri(2.5 * radius / 4, 2 * radius / 4, dotColorFunction.darker(), 30);
			// renderCircleDecTriPart5Tri(radius, 3 * radius / 4, (i) -> {
			// Color c = Color.getHSBColor(1F / 50 * i + progress / 100, 1,
			// 0.8F);
			//
			// return c;
			// }, 50);

			GlStateManager.rotate(-rotation, 0, 1, 0);
			GlStateManager.translate(-posX, -posY, -posZ);
			GlStateManager.enableTexture2D();

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}

	public static void renderTextRing(double posX, double posY, double posZ, double radius, double qStep, int brokenNess, Color c)
	{
		Minecraft mc = Minecraft.getMinecraft();
		GlStateManager.enableTexture2D();
		GlStateManager.disableLighting();
		RenderUtils.enableDefaultBlending();

		mc.renderEngine.bindTexture(mkrFont);

		GlStateManager.disableCull();

		for (double q = 0; q < Math.PI * 2; q += qStep)
		{
			double cX = Math.sin(q) * radius;
			double cZ = Math.cos(q) * radius;

			double x = cX + posX;
			double y = posY;
			double z = cZ + posZ;

			double winkel = 360 / (Math.PI * 2) * q;

			float fontSize = (float) (0.03 / 0.25 * radius);

			ColorUtil.applyColor(c);

			GlStateManager.translate(x, y, z);
			GlStateManager.rotate((float) winkel, 0, 1, 0);

			GlStateManager.glBegin(GL11.GL_QUADS);

			int charMod = new Random((long) (q * 1000) + RTEventHandler.clientAnimationCounter / 15).nextInt(25);
			float uXMod = (float) (1F / 256F * 10F * charMod);
			float uYMod = brokenNess * 1 / 256F * 9;

			GlStateManager.glTexCoord2f(uXMod, uYMod);
			GlStateManager.glVertex3f(-fontSize, fontSize, 0);

			GlStateManager.glTexCoord2f(uXMod, 1 / 256F * 9 + uYMod);
			GlStateManager.glVertex3f(-fontSize, -fontSize, 0);

			GlStateManager.glTexCoord2f(uXMod + 1 / 256F * 10, 1 / 256F * 9 + uYMod);
			GlStateManager.glVertex3f(fontSize, -fontSize, 0);

			GlStateManager.glTexCoord2f(uXMod + 1 / 256F * 10, uYMod);
			GlStateManager.glVertex3f(fontSize, fontSize, 0);

			GlStateManager.glEnd();

			GlStateManager.rotate(-(float) winkel, 0, 1, 0);
			GlStateManager.translate(-x, -y, -z);
		}
	}

	public static void renderMKRCastCharacters(double posX, double posY, double posZ, Color c)
	{
		for (int offset = 0; offset <= 15; offset += 15)
		{
			float progress = ((RTEventHandler.clientAnimationCounter + offset + Animation.getPartialTickTime())) % 30;

			double radius = Math.sin(progress * (Math.PI / 20)) * 0.15f + 0.1f;

			if (progress * (Math.PI / 20) >= Math.PI / 2)
			{
				radius = 0.25f;
			}

			if (progress >= 25)
			{
				radius -= 0.08F / 5 * (progress - 25);
			}

			double heightMod = 3D / 30 * progress - 0.3;

			double qLimit = Math.PI / (radius * 40);

			renderTextRing(posX, posY + heightMod, posZ, radius, Math.PI / 10.5, progress < 5 ? 2 : (progress < 8 ? 1 : 0), c);
		}
	}

	public static void renderCircleDecTriInner(double r, ITriangleFunction triangleFunction, int triCount)
	{
		renderCircleDecTriInner(r, triangleFunction, triCount, (i) -> {
			return 1;
		});
	}

	// triCount <= 11
	public static void renderCircleDecTriInner(double r, ITriangleFunction triangleFunction, int triCount, Function<Integer, Integer> countFunction)
	{
		GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
		GlStateManager.glVertex3f(0, 0, 0);

		int currentTriCount = 0;
		for (int c = 0; c < 10; c++)
		{
			int countForC = countFunction.apply(c);

			double winkelPart = 36 / countForC;

			double winkel = Math.PI * 2 / 360 * c * 36;
			double winkel2 = Math.PI * 2 / 360 * (c + 1) * 36;

			double pX = Math.sin(winkel) * (r);
			double pZ = Math.cos(winkel) * (r);

			double pX2 = Math.sin(winkel2) * (r);
			double pZ2 = Math.cos(winkel2) * (r);

			double dX = (pX2 - pX);
			double dZ = (pZ2 - pZ);

			double length = Math.sqrt(dX * dX + dZ * dZ);

			double partLength = length / countForC;

			double nX = dX / length;
			double nZ = dZ / length;

			GlStateManager.glVertex3f((float) (pX), 0, (float) (pZ));

			for (int p = 1; p <= countForC; p++)
			{
				if (currentTriCount < triCount)
				{
					ColorUtil.applyColor(triangleFunction.apply(currentTriCount));
					GlStateManager.glVertex3f((float) (pX + nX * partLength * p), 0, (float) (pZ + nZ * partLength * p));
					currentTriCount++;
				}
			}
		}

		GlStateManager.glEnd();
	}

	public static void renderCircleDecOuterPart(double r, ITriangleFunction triangleFunction)
	{
		for (int c = 0; c < 10; c++)
		{
			int winkelDeg1 = 36 * c;
			int winkelDeg2 = 36 * (c + 1);

			double winkelRad1 = (Math.PI * 2) / 360D * winkelDeg1;
			double winkelRad2 = (Math.PI * 2) / 360D * winkelDeg2;

			double pX1 = Math.sin(winkelRad1) * (r);
			double pZ1 = Math.cos(winkelRad1) * (r);

			double pX2 = Math.sin(winkelRad2) * (r);
			double pZ2 = Math.cos(winkelRad2) * (r);

			double cX = (pX2 + pX1) / 2;
			double cZ = (pZ2 + pZ1) / 2;

			GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);

			GlStateManager.glVertex3f((float) cX, 0, (float) cZ);
			for (int winkel = winkelDeg1; winkel <= winkelDeg2; winkel += 4)
			{
				int tri = c * 8 + (winkel - winkelDeg1) / 5;

				ColorUtil.applyColor(triangleFunction.apply(tri));
				double tWinkelRad = (Math.PI * 2) / 360D * winkel;

				double tX = Math.sin(tWinkelRad) * (r);
				double tZ = Math.cos(tWinkelRad) * (r);

				GlStateManager.glVertex3f((float) tX, 0, (float) tZ);
			}

			GlStateManager.glEnd();
		}
	}

	public static void renderCircleDecTriPart5Tri(double r1, double r2, ITriangleFunction triangleFunction, int triCount)
	{
		renderCircleDecTriPart5Tri(r1, r2, triangleFunction, triCount, 0, 10);
	}

	// Tricount <= 50
	public static void renderCircleDecTriPart5Tri(double r1, double r2, ITriangleFunction triangleFunction, int triCount, int cStart, int cEnd)
	{
		GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);

		for (int c = cStart; c < cEnd; c++)
		{
			int winkelDeg1 = 36 * c;
			int winkelDeg2 = 36 * (c + 1);

			int triIndex = (c - cStart) * 5;

			double winkelRad1 = (Math.PI * 2) / 360D * winkelDeg1;
			double winkelRad2 = (Math.PI * 2) / 360D * winkelDeg2;

			double pX1 = Math.sin(winkelRad1) * (r1);
			double pZ1 = Math.cos(winkelRad1) * (r1);

			double pX2 = Math.sin(winkelRad2) * (r1);
			double pZ2 = Math.cos(winkelRad2) * (r1);

			double pX1H = Math.sin(winkelRad1) * (r2);
			double pZ1H = Math.cos(winkelRad1) * (r2);

			double pX2H = Math.sin(winkelRad2) * (r2);
			double pZ2H = Math.cos(winkelRad2) * (r2);

			double cXH = (pX2H + pX1H) / 2;
			double cZH = (pZ2H + pZ1H) / 2;

			double tX1 = pX1 + (pX2 - pX1) / 3;
			double tZ1 = pZ1 + (pZ2 - pZ1) / 3;

			double tX2 = pX1 + 2 * (pX2 - pX1) / 3;
			double tZ2 = pZ1 + 2 * (pZ2 - pZ1) / 3;

			if (triIndex < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex));
				GlStateManager.glVertex3f((float) pX1, 0, (float) pZ1);
				GlStateManager.glVertex3f((float) pX1H, 0, (float) pZ1H);
				GlStateManager.glVertex3f((float) tX1, 0, (float) tZ1);
			}

			if (triIndex + 1 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 1));
				GlStateManager.glVertex3f((float) cXH, 0, (float) cZH);
			}

			if (triIndex + 2 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 2));
				GlStateManager.glVertex3f((float) tX2, 0, (float) tZ2);
			}

			if (triIndex + 3 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 3));
				GlStateManager.glVertex3f((float) pX2H, 0, (float) pZ2H);
			}

			if (triIndex + 4 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 4));
				GlStateManager.glVertex3f((float) pX2, 0, (float) pZ2);
			}
		}

		GlStateManager.glEnd();
	}

	public static void renderCircleDecTriPart3Tri(double r1, double r2, ITriangleFunction triangleFunction, int triCount)
	{
		renderCircleDecTriPart3Tri(r1, r2, triangleFunction, triCount, 0, 10);
	}

	// triCount <= 30
	public static void renderCircleDecTriPart3Tri(double r1, double r2, ITriangleFunction triangleFunction, int triCount, int cStart, int cEnd)
	{
		GlStateManager.glBegin(GL11.GL_TRIANGLE_STRIP);

		for (int c = cStart; c < cEnd; c++)
		{
			int winkelDeg1 = 36 * c;
			int winkelDeg2 = 36 * (c + 1);

			int triIndex = (c - cStart) * 3;

			double winkelRad1 = (Math.PI * 2) / 360D * winkelDeg1;
			double winkelRad2 = (Math.PI * 2) / 360D * winkelDeg2;

			double pX1 = Math.sin(winkelRad1) * (r1);
			double pZ1 = Math.cos(winkelRad1) * (r1);

			double pX2 = Math.sin(winkelRad2) * (r1);
			double pZ2 = Math.cos(winkelRad2) * (r1);

			double pX1H = Math.sin(winkelRad1) * (r2);
			double pZ1H = Math.cos(winkelRad1) * (r2);

			double pX2H = Math.sin(winkelRad2) * (r2);
			double pZ2H = Math.cos(winkelRad2) * (r2);

			double cX = (pX2 + pX1) / 2;
			double cZ = (pZ2 + pZ1) / 2;

			if (triIndex < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex));
				GlStateManager.glVertex3f((float) pX1, 0, (float) pZ1);
				GlStateManager.glVertex3f((float) pX1H, 0, (float) pZ1H);
				GlStateManager.glVertex3f((float) cX, 0, (float) cZ);
			}

			if (triIndex + 1 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 1));
				GlStateManager.glVertex3f((float) pX2H, 0, (float) pZ2H);
			}

			if (triIndex + 2 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 2));
				GlStateManager.glVertex3f((float) pX2, 0, (float) pZ2);
			}
		}

		GlStateManager.glEnd();
	}

	public static void renderCircleDecTriPartCross(double r1, double r2, ITriangleFunction triangleFunction, int triCount)
	{
		for (int c = 0; c < 10; c++)
		{
			int winkelDeg1 = 36 * c;
			int winkelDeg2 = 36 * (c + 1);

			int triIndex = c * 6;

			double winkelRad1 = (Math.PI * 2) / 360D * winkelDeg1;
			double winkelRad2 = (Math.PI * 2) / 360D * winkelDeg2;

			double pX1 = Math.sin(winkelRad1) * (r1);
			double pZ1 = Math.cos(winkelRad1) * (r1);

			double pX2 = Math.sin(winkelRad2) * (r1);
			double pZ2 = Math.cos(winkelRad2) * (r1);

			double pX1N = Math.sin(winkelRad1) * (r2);
			double pZ1N = Math.cos(winkelRad1) * (r2);

			double pX2N = Math.sin(winkelRad2) * (r2);
			double pZ2N = Math.cos(winkelRad2) * (r2);

			double cXF = (pX2 + pX1) / 2;
			double cZF = (pZ2 + pZ1) / 2;

			double cXN = (pX2N + pX1N) / 2;
			double cZN = (pZ2N + pZ1N) / 2;

			double cX = (cXF + cXN) / 2;
			double cZ = (cZF + cZN) / 2;

			double uX1 = cX + (cX - pX2N);
			double uZ1 = cZ + (cZ - pZ2N);

			double uX2 = cX + (cX - pX1N);
			double uZ2 = cZ + (cZ - pZ1N);

			GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);

			GlStateManager.glVertex3f((float) cX, 0, (float) cZ);

			if (triIndex < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex));
				GlStateManager.glVertex3f((float) pX1N, 0, (float) pZ1N);
				GlStateManager.glVertex3f((float) uX1, 0, (float) uZ1);
			}

			if (triIndex + 1 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 1));
				GlStateManager.glVertex3f((float) uX2, 0, (float) uZ2);
			}

			if (triIndex + 2 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 2));
				GlStateManager.glVertex3f((float) pX2N, 0, (float) pZ2N);
			}

			if (triIndex + 3 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 3));
				GlStateManager.glVertex3f((float) pX1N, 0, (float) pZ1N);
			}

			GlStateManager.glEnd();

			GlStateManager.glBegin(GL11.GL_TRIANGLES);
			if (triIndex + 4 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 4));
				GlStateManager.glVertex3f((float) pX1, 0, (float) pZ1);
				GlStateManager.glVertex3f((float) uX1, 0, (float) uZ1);
				GlStateManager.glVertex3f((float) pX1N, 0, (float) pZ1N);
			}

			if (triIndex + 5 < triCount)
			{
				ColorUtil.applyColor(triangleFunction.apply(triIndex + 5));
				GlStateManager.glVertex3f((float) pX2, 0, (float) pZ2);
				GlStateManager.glVertex3f((float) pX2N, 0, (float) pZ2N);
				GlStateManager.glVertex3f((float) uX2, 0, (float) uZ2);
			}

			GlStateManager.glEnd();
		}
	}
}
