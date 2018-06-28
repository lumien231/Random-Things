package lumien.randomthings.client.render;

import java.util.Random;

import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.tileentity.TileEntitySpectreEnergyInjector;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderSpectreEnergyInjector extends TileEntitySpecialRenderer<TileEntitySpectreEnergyInjector>
{
	@Override
	public void render(TileEntitySpectreEnergyInjector te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		float f = ((float) (RTEventHandler.clientAnimationCounter + partialTicks)) / 200.0F;

		x += 0.5;
		y += 0.6;
		z += 0.5;

		float f1 = 0.0F;

		GlStateManager.translate(x, y, z);

		Random random = new Random(432L);
		GlStateManager.disableTexture2D();
		GlStateManager.shadeModel(7425);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableAlpha();
		GlStateManager.enableCull();
		GlStateManager.depthMask(false);
		GlStateManager.pushMatrix();

		for (int i = 0; (float) i < 25; ++i)
		{
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
			float f2 = random.nextFloat() * 0.2F + 0.1F + f1 * 10.0F;
			float f3 = random.nextFloat() * 0.1F + f1 * 2.0F;
			
			int red = (int) ((Math.sin(f) / 2 + 0.5) * 80) + 40;
			
			bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
			bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(0, 0, 0, (int) (255.0F * (1.0F - f1))).endVertex();
			bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(red, 230, 226, 0).endVertex();
			bufferbuilder.pos(0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(red, 230, 226, 0).endVertex();
			bufferbuilder.pos(0.0D, (double) f2, (double) (1.0F * f3)).color(red, 230, 226, 0).endVertex();
			bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(red, 230, 226, 0).endVertex();
			tessellator.draw();
		}

		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.shadeModel(7424);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(-x, -y, -z);
	}
}
