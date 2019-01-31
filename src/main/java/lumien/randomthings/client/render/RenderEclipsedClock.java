package lumien.randomthings.client.render;

import java.awt.Color;

import javax.annotation.Nullable;

import lumien.randomthings.entitys.EntityEclipsedClock;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.util.client.MKRRenderUtil;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEclipsedClock extends Render<EntityEclipsedClock>
{
	private final Minecraft mc = Minecraft.getMinecraft();

	private final RenderItem itemRenderer;

	public RenderEclipsedClock(RenderManager renderManagerIn, RenderItem itemRendererIn)
	{
		super(renderManagerIn);
		this.itemRenderer = itemRendererIn;
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityEclipsedClock entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		BlockPos blockpos = entity.getHangingPosition();
		double d0 = (double) blockpos.getX() - entity.posX + x;
		double d1 = (double) blockpos.getY() - entity.posY + y;
		double d2 = (double) blockpos.getZ() - entity.posZ + z;
		GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
		GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);

		this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
		ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
		GlStateManager.translate(0.0F, 0.0F, 0.4375F);
		this.renderItem(entity);

		EnumFacing facing = entity.facingDirection;
		float rotX = facing.getFrontOffsetX();
		float rotY = facing.getFrontOffsetY();
		float rotZ = facing.getFrontOffsetZ();

		float horRotation = (entity.facingDirection.getHorizontalIndex() - 2) % 4 * 90;

		GlStateManager.rotate(horRotation, 0, 1, 0);
		GlStateManager.rotate(90, rotZ, rotY, rotX);

		RenderUtils.enableDefaultBlending();
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		
		int animationCounter;
		if ((animationCounter = entity.getAnimationCounter()) > 0)
		{
			float animation = animationCounter - partialTicks;

			float mod = (100 - animation) / 50;

			mod = Math.min(0.25F, mod);

			MKRRenderUtil.renderCircleDecTriPart5Tri(0.2F + mod, 0, (i) -> {

				Color c = Color.getHSBColor(1 / 360F * 270, 1F, (float) Math.sin(((Math.PI * 4) / 50F) * i + animation / 5) / 10F + 0.1F);

				float alpha = animation <= 10 ? animation / 10 : 1;
				alpha = Math.max(alpha, 0);

				return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (255 * alpha));
			}, 50);
		}
		
		Minecraft.getMinecraft().entityRenderer.enableLightmap();
		
		GlStateManager.disableBlend();

		GlStateManager.rotate(-90, rotZ, rotY, rotX);
		GlStateManager.rotate(-horRotation, 0, 1, 0);

		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();


		GlStateManager.popMatrix();


		if (entity.shouldDisplayTime())
			EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), entity.getStringTargetTime(), (float) x, (float) ((float) y + 0.45), (float) z, 0, (entity.facingDirection.getHorizontalIndex() - 2) % 4 * 90, 0, false, false);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Nullable
	protected ResourceLocation getEntityTexture(EntityEclipsedClock entity)
	{
		return null;
	}

	private void renderItem(EntityEclipsedClock clock)
	{
		ItemStack itemstack = new ItemStack(ModItems.eclipsedClock);
		itemstack.setTagCompound(new NBTTagCompound());
		itemstack.getTagCompound().setInteger("targetTime", clock.getTargetTime());

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	protected void renderName(EntityEclipsedClock entity, double x, double y, double z)
	{
		if (this.renderManager.pointedEntity == entity)
		{
			double d0 = entity.getDistanceSq(this.renderManager.renderViewEntity);
			float f = entity.isSneaking() ? 32.0F : 64.0F;

			if (d0 < (double) (f * f))
			{
				String s = entity.getStringTargetTime();
				this.renderLivingLabel(entity, s, x, y, z, 64);
			}
		}
	}
}