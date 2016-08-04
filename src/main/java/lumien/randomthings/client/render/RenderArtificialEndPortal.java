package lumien.randomthings.client.render;

import java.nio.FloatBuffer;
import java.util.Random;

import lumien.randomthings.entitys.EntityArtificialEndPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderArtificialEndPortal extends Render
{
	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random RANDOM = new Random(31100L);
	FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

	public RenderArtificialEndPortal(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager renderManager = mc.getRenderManager();

		float f = (float) (mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks);
		float f1 = (float) (mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks);
		float f2 = (float) (mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks);

		EntityArtificialEndPortal portalEntity = (EntityArtificialEndPortal) entity;

		y += 1;

		if (portalEntity.actionTimer > 85)
		{
			double size = Math.min(3, 3f / 115 * (portalEntity.actionTimer + partialTicks - 85));
			GlStateManager.disableLighting();
			RANDOM.setSeed(31100L);
			float f3 = 0F;

			for (int i = 0; i < 16; ++i)
			{
				GlStateManager.pushMatrix();
				float f4 = 16 - i;
				float f5 = 0.0625F;
				float f6 = 1F / (f4 + 1.0F); // 2 instead of 1 For more color

				if (i == 0)
				{
					this.bindTexture(END_SKY_TEXTURE);
					f6 = 0.1F;
					f4 = 65.0F;
					f5 = 0.125F;
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				}

				if (i >= 1)
				{
					this.bindTexture(END_PORTAL_TEXTURE);
				}

				if (i == 1)
				{
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
					f5 = 0.5F;
				}

				float f7 = (float) (-(y + f3));
				float f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
				float f9 = f7 + f4 + (float) ActiveRenderInfo.getPosition().yCoord;
				float f10 = f8 / f9;
				f10 = (float) (y + f3) + f10;
				GlStateManager.translate(f, f10, f2);
				GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
				GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
				GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
				GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
				GlStateManager.texGen(GlStateManager.TexGen.S, 9473, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
				GlStateManager.texGen(GlStateManager.TexGen.T, 9473, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
				GlStateManager.texGen(GlStateManager.TexGen.R, 9473, this.getBuffer(0.0F, 0.0F, 0.0F, 1.0F));
				GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
				GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
				GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
				GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
				GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5890);
				GlStateManager.pushMatrix();
				GlStateManager.loadIdentity();
				GlStateManager.translate(0.0F, Minecraft.getSystemTime() % 700000L / 700000.0F, 0.0F);
				GlStateManager.scale(f5, f5, f5);
				GlStateManager.translate(0.5F, 0.5F, 0.0F);
				GlStateManager.rotate((i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.translate(-0.5F, -0.5F, 0.0F);
				GlStateManager.translate(-f, -f2, -f1);
				f8 = f7 + (float) ActiveRenderInfo.getPosition().yCoord;
				GlStateManager.translate((float) ActiveRenderInfo.getPosition().xCoord * f4 / f8, (float) ActiveRenderInfo.getPosition().zCoord * f4 / f8, -f1);
				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
				float f11 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f6;
				float f12 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f6;
				float f13 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f6;

				if (i == 0)
				{
					f11 = f12 = f13 = 1.0F * f6;
				}

				vertexbuffer.pos(x - (size / 2), y + f3, z - (size / 2)).color(f11, f12, f13, 1.0F).endVertex();
				vertexbuffer.pos(x - (size / 2), y + f3, z + (size / 2)).color(f11, f12, f13, 1.0F).endVertex();
				vertexbuffer.pos(x + (size / 2), y + f3, z + (size / 2)).color(f11, f12, f13, 1.0F).endVertex();
				vertexbuffer.pos(x + (size / 2), y + f3, z - (size / 2)).color(f11, f12, f13, 1.0F).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
				this.bindTexture(END_SKY_TEXTURE);
			}

			GlStateManager.disableBlend();
			GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
			GlStateManager.enableLighting();
		}
	}

	private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
	{
		this.buffer.clear();
		this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		this.buffer.flip();
		return this.buffer;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}
