package lumien.randomthings.client.render;

import lumien.randomthings.entitys.EntityReviveCircle;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderReviveCircle extends Render
{
	public RenderReviveCircle(RenderManager renderManager)
	{
		super(renderManager);
	}

	ResourceLocation texture = new ResourceLocation("RandomThings:textures/entitys/reviveCircle.png");

	private void doRender(EntityReviveCircle circle, double posX, double posY, double posZ, float partialTickTime)
	{
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		RenderUtils.enableDefaultBlending();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		GlStateManager.translate((float) posX + 0.5f, (float) posY + 0.13f, (float) posZ + 0.5f);

		this.bindEntityTexture(circle);

		GlStateManager.translate(-0.5f, 0, -0.5f);

		float renderAge = circle.age + partialTickTime;

		GlStateManager.scale(2, 1, 2);
		if (renderAge < 40)
		{
			GlStateManager.color(1, 1, 1, renderAge * 0.05F);
		}

		GlStateManager.rotate(renderAge * renderAge / 50, 0f, 1, 0f);
		GlStateManager.translate(0.5f, 0, 0.5f);
		GlStateManager.rotate(90, 1, 0, 0);

		int i = 1;
		float f2 = 1;
		float f3 = 0;
		float f4 = 0;
		float f5 = 1;

		float f6 = 1.0F;
		float f7 = 1F;
		float f8 = 1F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder renderer = tessellator.getBuffer();
		renderer.begin(7, DefaultVertexFormats.POSITION_TEX);

		renderer.pos(0.0F - f7, 0.0F - f8, 0.0D).tex(f2, f5).endVertex();
		renderer.pos(f6 - f7, 0.0F - f8, 0.0D).tex(f3, f5).endVertex();
		renderer.pos(f6 - f7, 1.0F - f8, 0.0D).tex(f3, f4).endVertex();
		renderer.pos(0.0F - f7, 1.0F - f8, 0.0D).tex(f2, f4).endVertex();

		renderer.pos(f6 - f7, 0.0F - f8, 0.0D).tex(f3, f5).endVertex();
		renderer.pos(0.0F - f7, 0.0F - f8, 0.0D).tex(f2, f5).endVertex();
		renderer.pos(0.0F - f7, 1.0F - f8, 0.0D).tex(f2, f4).endVertex();
		renderer.pos(f6 - f7, 1.0F - f8, 0.0D).tex(f3, f4).endVertex();

		tessellator.draw();

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		Minecraft.getMinecraft().entityRenderer.enableLightmap();
		GlStateManager.popMatrix();
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		doRender((EntityReviveCircle) p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		return texture;
	}

}
