package lumien.randomthings.client.render;

import java.util.Random;

import lumien.randomthings.client.ClientProxy;
import lumien.randomthings.entitys.EntityGoldenChicken;
import lumien.randomthings.entitys.EntityWeatherCloud;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.item.ItemWeatherEgg;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWeatherCloud extends Render<EntityWeatherCloud>
{
	private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("randomthings:textures/entitys/goldenchicken.png");

	public RenderWeatherCloud(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityWeatherCloud entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.getEggType() == ItemWeatherEgg.TYPE.SUN)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();
			float f = ((float) (RTEventHandler.clientAnimationCounter + partialTicks)) / 200.0F;
			
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
				float f2 = random.nextFloat() * 0.8F + 0.5F + f1 * 10.0F;
				float f3 = random.nextFloat() * 0.3F + f1 * 2.0F;
				bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
				bufferbuilder.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int) (255.0F * (1.0F - f1))).endVertex();
				bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 255, 0, 0).endVertex();
				bufferbuilder.pos(0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 255, 0, 0).endVertex();
				bufferbuilder.pos(0.0D, (double) f2, (double) (1.0F * f3)).color(255, 255, 0, 0).endVertex();
				bufferbuilder.pos(-0.866D * (double) f3, (double) f2, (double) (-0.5F * f3)).color(255, 255, 0, 0).endVertex();
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

	@Override
	protected ResourceLocation getEntityTexture(EntityWeatherCloud entity)
	{
		return null;
	}
}