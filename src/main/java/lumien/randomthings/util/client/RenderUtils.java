package lumien.randomthings.util.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class RenderUtils
{
	static Gui gui = new Gui();

	public static void drawCube(float posX, float posY, float posZ, float width, float length, float height, int red, int green, int blue, int alpha)
	{
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		Tessellator t = Tessellator.getInstance();
		VertexBuffer wr = t.getBuffer();

		GlStateManager.disableTexture2D();

		GlStateManager.pushMatrix();
		GlStateManager.translate(posX, posY, posZ);

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

		GlStateManager.popMatrix();
		GlStateManager.enableTexture2D();

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}

	public static Map<EnumFacing, List<BakedQuad>> getQuadFaceMapFromState(IBlockState state)
	{
		HashMap<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(state);

		Random rng = new Random();

		for (EnumFacing f : EnumFacing.values())
		{
			map.put(f, model.getQuads(state, f, rng.nextLong()));
		}
		
		map.put(null, model.getQuads(state, null, rng.nextLong()));

		return map;
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
