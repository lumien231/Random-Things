package lumien.randomthings.client.render;

import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.BlockBlockLuminous;
import lumien.randomthings.block.BlockBlockLuminousTranslucent;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.tileentity.TileEntityBlockDiaphanous;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.VertexBufferConsumer;

public class RenderBlockDiaphanous extends TileEntitySpecialRenderer<TileEntityBlockDiaphanous>
{
	static Field rawIntBuffer;

	static
	{
		try
		{
			rawIntBuffer = BufferBuilder.class.getDeclaredField(MCPNames.field("field_178999_b"));
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		rawIntBuffer.setAccessible(true);
	}

	@Override
	public void render(TileEntityBlockDiaphanous te, double x, double y, double z, float partialTicks, int destroyStage, float wat)
	{
		BlockPos tePos = te.getPos();
		EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;

		double dX = tePos.getX() + 0.5 - TileEntityRendererDispatcher.staticPlayerX;
		double dY = tePos.getY() + 0.5 - TileEntityRendererDispatcher.staticPlayerY;
		double dZ = tePos.getZ() + 0.5 - TileEntityRendererDispatcher.staticPlayerZ;

		double distance = Math.max(0, Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2) - 5);

		float red = 1;
		float green = 1;
		float blue = 1;

		float alpha;

		if (te.isItem())
		{
			alpha = (float) (Math.sin(RTEventHandler.clientAnimationCounter / 20D) * 0.3 + 0.3 + 0.4);
		}
		else
		{
			if (te.isInverted())
			{
				alpha = 1 - (-1F / 2 * ((float) Math.cos(Math.PI * Math.min(Math.max(0, distance - 8), 25) / 25) - 1));
			}
			else
			{
				alpha = -1F / 2 * ((float) Math.cos(Math.PI * Math.min(Math.max(0, distance - 8), 25) / 25) - 1);
			}
		}

		if (!te.isItem() && thePlayer != null)
		{
			for (EnumHand hand : EnumHand.values())
			{
				ItemStack held = thePlayer.getHeldItem(hand);

				if (!held.isEmpty() && held.getItem() == Item.getItemFromBlock(ModBlocks.blockDiaphanous))
				{
					alpha = 1;
				}
			}
		}

		// Cube Start
		IBlockState iblockstate = te.getDisplayState();

		if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL)
		{
			boolean luminous = iblockstate.getBlock() instanceof BlockBlockLuminous || iblockstate.getBlock() instanceof BlockBlockLuminousTranslucent;

			World world = te.getWorld();

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			GlStateManager.pushMatrix();

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();

			bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);

			GlStateManager.translate(x - te.getPos().getX(), y - te.getPos().getY(), z - te.getPos().getZ());
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

			if (te.isItem())
			{
				blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, tePos, bufferbuilder, false, MathHelper.getPositionRandom(te.getPos()));
			}
			else
			{
				RenderUtils.renderModelCustomSides(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, tePos, bufferbuilder, te.getFacingMap(), MathHelper.getPositionRandom(te.getPos()));
			}

			IntBuffer buf = null;
			try
			{
				buf = (IntBuffer) rawIntBuffer.get(bufferbuilder);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			for (int vertexIndex = 0; vertexIndex < bufferbuilder.getVertexCount() + 1; vertexIndex++)
			{
				int i = bufferbuilder.getColorIndex(vertexIndex);
				int j = -1;

				if (!bufferbuilder.isColorDisabled())
				{
					j = buf.get(i);

					if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
					{
						int k = (int) ((float) (j & 255) * red);
						int l = (int) ((float) (j >> 8 & 255) * green);
						int i1 = (int) ((float) (j >> 16 & 255) * blue);

						if (luminous || te.isItem())
						{
							k = l = i1 = 255;
						}

						j = j & ((int) (alpha * 255) << 24);
						j = j | i1 << 16 | l << 8 | k;
					}
					else
					{
						int j1 = (int) ((float) (j >> 24 & 255) * red);
						int k1 = (int) ((float) (j >> 16 & 255) * green);
						int l1 = (int) ((float) (j >> 8 & 255) * blue);

						if (luminous || te.isItem())
						{
							j1 = k1 = l1 = 255;
						}

						j = j & (int) (alpha * 255);

						j = j | j1 << 24 | k1 << 16 | l1 << 8;
					}
				}

				buf.put(i, j);
			}

			GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
			RenderUtils.enableDefaultBlending();

			if (!te.isItem())
			{
				if (Minecraft.isAmbientOcclusionEnabled())
				{
					GlStateManager.shadeModel(GL11.GL_SMOOTH);
				}
				else
				{
					GlStateManager.shadeModel(GL11.GL_FLAT);
				}

				GlStateManager.disableLighting();
			}
			else
			{
				GlStateManager.disableLighting();
				Minecraft.getMinecraft().entityRenderer.disableLightmap();
			}

			if (luminous && !te.isItem())
			{
				Minecraft.getMinecraft().entityRenderer.disableLightmap();
			}

			GlStateManager.disableLighting();
			tessellator.draw();

			if (te.isItem())
			{
				GlStateManager.enableLighting();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			}
			else
			{
				GlStateManager.enableLighting();
				GlStateManager.disableBlend();
			}

			if (luminous && !te.isItem())
			{
				Minecraft.getMinecraft().entityRenderer.enableLightmap();
			}

			GlStateManager.popMatrix();

			GlStateManager.alphaFunc(516, 0.1F);
		}
		// Cube End
	}
}
