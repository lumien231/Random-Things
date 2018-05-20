package lumien.randomthings.client.render;

import lumien.randomthings.entitys.EntitySoul;
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
public class RenderSoul extends Render
{
	ResourceLocation soul1 = new ResourceLocation("RandomThings:textures/entitys/soul1.png");
	ResourceLocation soul2 = new ResourceLocation("RandomThings:textures/entitys/soul2.png");

	public RenderSoul(RenderManager renderManager)
	{
		super(renderManager);
	}

	private void doRender(EntitySoul soul, double posX, double posY, double posZ, float p_76986_8_, float p_76986_9_)
	{
		if (soul.render)
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			RenderUtils.enableDefaultBlending();
			Minecraft.getMinecraft().entityRenderer.disableLightmap();

			GlStateManager.translate((float) posX + 0.15f, (float) posY + 0.3F, (float) posZ);

			GlStateManager.scale(0.3f, 0.3f, 0.3f);
			this.bindEntityTexture(soul);

			GlStateManager.translate(-0.5F, -0.5F, 0);
			GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(0.5F, 0.5F, 0);

			int i = 1;
			float f2 = 1;
			float f3 = 0;
			float f4 = 0;
			float f5 = 1;

			float f6 = 1.0F;
			float f7 = 1F;
			float f8 = 1F;
			GlStateManager.color(1, 1, 1);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder worldRenderer = tessellator.getBuffer();
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

			worldRenderer.pos(0.0F - f7, 0.0F - f8, 0.0D).tex(f2, f5).endVertex();
			worldRenderer.pos(f6 - f7, 0.0F - f8, 0.0D).tex(f3, f5).endVertex();
			worldRenderer.pos(f6 - f7, 1.0F - f8, 0.0D).tex(f3, f4).endVertex();
			worldRenderer.pos(0.0F - f7, 1.0F - f8, 0.0D).tex(f2, f4).endVertex();

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.disableRescaleNormal();
			Minecraft.getMinecraft().entityRenderer.enableLightmap();
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void doRender(Entity entity, double posX, double posY, double posZ, float p_76986_8_, float p_76986_9_)
	{
		doRender((EntitySoul) entity, posX, posY, posZ, p_76986_8_, p_76986_9_);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		switch (((EntitySoul) entity).type)
		{
		case 0:
			return soul1;
		case 1:
			return soul2;
		}
		return soul1;
	}

}
