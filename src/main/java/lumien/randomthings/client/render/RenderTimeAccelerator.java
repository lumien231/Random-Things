package lumien.randomthings.client.render;

import java.awt.Color;
import java.util.Random;

import lumien.randomthings.client.ClientProxy;
import lumien.randomthings.client.render.magiccircles.ColorFunctions;
import lumien.randomthings.client.render.magiccircles.IColorFunction;
import lumien.randomthings.entitys.EntityGoldenChicken;
import lumien.randomthings.entitys.EntityTimeAccelerator;
import lumien.randomthings.entitys.EntityWeatherCloud;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.item.ItemWeatherEgg;
import lumien.randomthings.util.client.MKRRenderUtil;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTimeAccelerator extends Render<EntityTimeAccelerator>
{
	public RenderTimeAccelerator(RenderManager renderManager)
	{
		super(renderManager);
	}
	
	IColorFunction innerFunction = ColorFunctions.alternateN(new Color(100, 100, 100, 80), Color.LIGHT_GRAY, 6, 2);

	IColorFunction outerFunction1 = ColorFunctions.constant(Color.LIGHT_GRAY).next(ColorFunctions.flicker(50, 100)).next(ColorFunctions.limit(ColorFunctions.constant(new Color(0, 0, 0, 0)), (i) -> {
		return (i + 2) % 6 != 0;
	}));
	
	IColorFunction outerFunction2 = ColorFunctions.constant(Color.LIGHT_GRAY).next(ColorFunctions.limit(ColorFunctions.constant(new Color(0, 0, 0, 0)), (i) -> {
		return (i + 2) % 6 == 0 || (i+4) % 6 == 0 || (i+6) % 6 == 0;
	})).next(ColorFunctions.flicker(400, 100));

	IColorFunction outerFunction4 = ColorFunctions.alternateN(new Color(100, 100, 100, 80), Color.LIGHT_GRAY, 2, 5).next(ColorFunctions.flicker(800, 100)).next(ColorFunctions.limit(ColorFunctions.constant(new Color(100, 100, 100, 80)), (i) -> {
		return (i) % 2 == 0 || ((i) / 5) % 2 == 1;
	}));

	@Override
	public void doRender(EntityTimeAccelerator entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.disableTexture2D();

		RenderUtils.enableDefaultBlending();

		GlStateManager.color(1, 1, 1, 1);
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		GlStateManager.disableCull();

		GlStateManager.translate(x, y, z);

		int timeRate = entity.getTimeRate();

		float progress = (2) * (RTEventHandler.clientAnimationCounter + partialTicks);

		for (EnumFacing facing : EnumFacing.VALUES)
		{
			GlStateManager.translate(facing.getFrontOffsetX() / 1.9D, facing.getFrontOffsetY() / 1.9D, facing.getFrontOffsetZ() / 1.9D);

			float rotX = facing.getFrontOffsetX();
			float rotY = facing.getFrontOffsetY();
			float rotZ = facing.getFrontOffsetZ();

			GlStateManager.rotate(90, rotZ, rotY, rotX);
			GlStateManager.rotate(progress, 0, 1, 0);

			MKRRenderUtil.renderCircleDecTriInner(0.05, innerFunction.tt(progress), 33, (i) -> {
				return 3;
			});

			if (timeRate >= 2)
			{
				MKRRenderUtil.renderCircleDecTriPartCross(0.07, 0.05, ColorFunctions.constant(new Color(100, 100, 100, 80)).next(ColorFunctions.limit(ColorFunctions.constant(Color.LIGHT_GRAY), (i) -> {

					i += 6;
					return i % 6 < 4 && i / 6 % 2 != 0;
				})).tt(progress), 60);
			}

			if (timeRate >= 4)
			{
				MKRRenderUtil.renderCircleDecTriPart3Tri(0.1, 0.07, outerFunction1.tt(progress), 30);
			}

			if (timeRate >= 8)
			{
				MKRRenderUtil.renderCircleDecTriPart3Tri(0.12, 0.09, outerFunction2.tt(progress), 30);
			}

			if (timeRate >= 16)
			{
				MKRRenderUtil.renderCircleDecTriPart5Tri(0.15, 0.12, outerFunction4.tt(progress), 50);
			}

			if (timeRate >= 32)
			{
				MKRRenderUtil.renderCircleDecTriPart5Tri(0.16, 0.15, (i) -> {

					Color c = Color.getHSBColor(0, 0, (float) Math.sin(((Math.PI * 4) / 50F) * i + progress / 10) / 2.5F + 0.6F);

					return new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
				}, 50);
			}

			GlStateManager.rotate(-progress, 0, 1, 0);
			GlStateManager.rotate(-90, rotZ, rotY, rotX);

			GlStateManager.translate(-facing.getFrontOffsetX() / 1.9D, -facing.getFrontOffsetY() / 1.9D, -facing.getFrontOffsetZ() / 1.9D);
		}

		GlStateManager.translate(-(x), -(y), -(z));

		GlStateManager.enableCull();

		Minecraft.getMinecraft().entityRenderer.enableLightmap();

		GlStateManager.color(1, 1, 1, 1);

		GlStateManager.enableTexture2D();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTimeAccelerator entity)
	{
		return null;
	}
}