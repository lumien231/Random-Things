package lumien.randomthings.client.render;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.tileentity.TileEntityAncientFurnace;
import lumien.randomthings.tileentity.TileEntityAncientFurnace.STATE;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class RenderAncientFurnace extends TileEntitySpecialRenderer<TileEntityAncientFurnace>
{
	static final Pair<Integer, Integer>[] offsets = new Pair[] { Pair.of(-1, 0), Pair.of(-1, -1), Pair.of(0, -1), Pair.of(1, -1), Pair.of(1, 0), Pair.of(1, 1), Pair.of(0, 1), Pair.of(-1, 1) };

	static final ResourceLocation[] textures = new ResourceLocation[] { new ResourceLocation("randomthings:textures/blocks/ancientbrick_o0.png"), new ResourceLocation("randomthings:textures/blocks/ancientbrick_o1.png"), new ResourceLocation("randomthings:textures/blocks/ancientbrick_o2.png"), new ResourceLocation("randomthings:textures/blocks/ancientbrick_o3.png") };

	static final int[][] textureIndices = new int[][] { { 2, 3, 0, 1 }, { 1, 2, 3, 0 }, { 0, 1, 2, 3 }, { 3, 0, 1, 2 } };

	static final List<Overlay> toDraw = new ArrayList<Overlay>();
	static
	{
		toDraw.add(new Overlay(Pair.of(0, -1), new EnumFacing[] { EnumFacing.NORTH }));
		toDraw.add(new Overlay(Pair.of(0, 1), new EnumFacing[] { EnumFacing.SOUTH }));
		toDraw.add(new Overlay(Pair.of(1, 0), new EnumFacing[] { EnumFacing.EAST }));
		toDraw.add(new Overlay(Pair.of(-1, 0), new EnumFacing[] { EnumFacing.WEST }));

		toDraw.add(new Overlay(Pair.of(1, -1), new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST }));
		toDraw.add(new Overlay(Pair.of(1, 1), new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.EAST }));
		toDraw.add(new Overlay(Pair.of(-1, 1), new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST }));
		toDraw.add(new Overlay(Pair.of(-1, -1), new EnumFacing[] { EnumFacing.NORTH, EnumFacing.WEST }));
	}

	@Override
	public void render(TileEntityAncientFurnace te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		STATE state = te.getState();
		int counter = te.getStartingCounter();
		float transparency = 0;

		if (state == STATE.RUNNING)
		{
			transparency = 1;
		}
		else if (state == STATE.STARTING && counter > 100)
		{
			transparency = Math.min(1, -1 * (float) Math.cos(((counter + partialTicks) - 100) / 300D * (Math.PI / 2)) + 1);
		}

		if (state == STATE.RUNNING || (state == STATE.STARTING && counter > 100))
		{
			GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

			this.setLightmapDisabled(true);
			GlStateManager.disableLighting();

			float yellow = ((float) Math.sin((RTEventHandler.clientAnimationCounter + partialTicks) / 25f) + 1) / 5 + 0.1f;

			GlStateManager.color(1f, yellow, 0, transparency);

			for (Overlay o : toDraw)
			{
				Pair<Integer, Integer> offset = o.offset;

				long rdm = MathHelper.getPositionRandom(new Vec3i(te.getPos().getX() + offset.getLeft(), te.getPos().getY(), te.getPos().getZ() + offset.getRight()));
				int model = getRandomInteger(4, Math.abs((int) rdm >> 16) % 4);

				for (EnumFacing f : o.facings)
				{
					ResourceLocation texture = textures[textureIndices[model][f.getHorizontalIndex()]];

					this.bindTexture(texture);

					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder renderer = tessellator.getBuffer();
					renderer.begin(7, DefaultVertexFormats.POSITION_TEX);

					double iX = x + offset.getLeft();
					double iZ = z + offset.getRight();

					switch (f)
					{
						case NORTH:
							renderer.pos(iX, y, iZ - 0.001).tex(1, 1).endVertex();
							renderer.pos(iX, y + 1, iZ - 0.001).tex(1, 0).endVertex();
							renderer.pos(iX + 1, y + 1, iZ - 0.001).tex(0, 0).endVertex();
							renderer.pos(iX + 1, y, iZ - 0.001).tex(0, 1).endVertex();
							break;
						case SOUTH:
							renderer.pos(iX, y, iZ + 1.001).tex(0, 1).endVertex();
							renderer.pos(iX + 1, y, iZ + 1.001).tex(1, 1).endVertex();
							renderer.pos(iX + 1, y + 1, iZ + 1.001).tex(1, 0).endVertex();
							renderer.pos(iX, y + 1, iZ + 1.001).tex(0, 0).endVertex();
							break;
						case WEST:
							renderer.pos(iX - 0.001, y, iZ).tex(0, 1).endVertex();
							renderer.pos(iX - 0.001, y, iZ + 1).tex(1, 1).endVertex();
							renderer.pos(iX - 0.001, y + 1, iZ + 1).tex(1, 0).endVertex();
							renderer.pos(iX - 0.001, y + 1, iZ).tex(0, 0).endVertex();
							break;
						case EAST:
							renderer.pos(iX + 1.001, y, iZ).tex(1, 1).endVertex();
							renderer.pos(iX + 1.001, y + 1, iZ).tex(1, 0).endVertex();
							renderer.pos(iX + 1.001, y + 1, iZ + 1).tex(0, 0).endVertex();
							renderer.pos(iX + 1.001, y, iZ + 1).tex(0, 1).endVertex();
							break;
						default:
							break;
					}

					tessellator.draw();

				}
			}

			this.setLightmapDisabled(false);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();

			GlStateManager.alphaFunc(516, 0.1F);
		}
	}

	public static int getRandomInteger(int count, int weight)
	{
		int i = 0;

		for (int j = count; i < j; ++i)
		{
			weight -= 1;

			if (weight < 0)
			{
				return i;
			}
		}

		return 0;
	}

	static class Overlay
	{
		Pair<Integer, Integer> offset;
		EnumFacing[] facings;

		public Overlay(Pair<Integer, Integer> offset, EnumFacing[] facings)
		{
			this.offset = offset;
			this.facings = facings;
		}
	}
}
