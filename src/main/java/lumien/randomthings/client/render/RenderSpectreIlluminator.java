package lumien.randomthings.client.render;

import java.awt.Color;
import java.util.Random;

import lumien.randomthings.client.render.magiccircles.ColorFunctions;
import lumien.randomthings.client.render.magiccircles.IColorFunction;
import lumien.randomthings.entitys.EntitySpectreIlluminator;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.util.client.MKRRenderUtil;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpectreIlluminator extends Render<EntitySpectreIlluminator>
{
	public RenderSpectreIlluminator(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntitySpectreIlluminator entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.disableTexture2D();

		RenderUtils.enableDefaultBlending();

		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		GlStateManager.disableCull();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		float progress = (2) * (RTEventHandler.clientAnimationCounter + partialTicks);

		IColorFunction innerFunction = ColorFunctions.alternate(new Color(100, 100, 100, 0), new Color(50, 150, 255, 255));
		IColorFunction outerFunction = ColorFunctions.alternate(new Color(100, 100, 100, 0), new Color(0, 150, 200, 100)).next(ColorFunctions.limit(ColorFunctions.constant(new Color(0, 0, 0, 0)), (i) -> {
			return (i + 2) % 3 == 0;
		}));


		Random rng = new Random(5000);

		for (int c = 0; c < 5; c++)
		{
			float rotX = rng.nextInt(360) + progress / (rng.nextInt(4) + 1);
			float rotZ = rng.nextInt(360) + progress / (rng.nextInt(4) + 1);

			float radius = rng.nextFloat() * 0.5F + 0.07F;


			// x - Axis
			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotX, 1, 0, 0);
			GlStateManager.rotate(rotZ, 0, 0, 1);
			GlStateManager.rotate(progress, 0, 1, 0);
			MKRRenderUtil.renderCircleDecTriInner(0.04, (i) -> {
				Color clr = Color.getHSBColor(0.5F, 1, (float) Math.sin(((Math.PI * 4) / 50F) * i + progress / 10) * 0.1F + 0.9F);

				return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), 255);
			}, 33, (i) -> {
				return 3;
			});
			MKRRenderUtil.renderCircleDecTriPart3Tri(radius, 0.04, outerFunction.next(ColorFunctions.flicker(rng.nextInt(1000), 40)).tt(progress), 30);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();

		GlStateManager.enableCull();

		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.enableLightmap();

		GlStateManager.color(1, 1, 1, 1);

		GlStateManager.enableTexture2D();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySpectreIlluminator entity)
	{
		return null;
	}
}
