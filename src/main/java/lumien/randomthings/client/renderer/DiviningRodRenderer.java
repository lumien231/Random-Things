package lumien.randomthings.client.renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import lumien.randomthings.client.util.RenderUtils;
import lumien.randomthings.item.DiviningRodItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DiviningRodRenderer
{
	public static DiviningRodRenderer INSTANCE;

	LinkedHashSet<BlockPos> positionsToCheck;

	List<Indicator> indicators;

	HashMap<Block, Color> blockColorMap;

	public DiviningRodRenderer()
	{
		positionsToCheck = new LinkedHashSet<BlockPos>();
		indicators = new ArrayList<Indicator>();
	}

	public boolean shouldGlow(DiviningRodItem rodType)
	{
		return !indicators.isEmpty() && (indicators.stream().anyMatch((i) -> i.type == rodType));
	}

	int modX, modY, modZ;

	public void render()
	{
		float partialTicks = Minecraft.getInstance().getRenderPartialTicks();

		ActiveRenderInfo ari = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

		Vec3d pos = ari.getProjectedView();

		double playerX = pos.getX();
		double playerY = pos.getY();
		double playerZ = pos.getZ();

		GlStateManager.disableTexture();
		GlStateManager.disableDepthTest();
		RenderUtils.enableDefaultBlending();

		GlStateManager.translated(-playerX, -playerY, -playerZ);
		for (Indicator indicator : indicators)
		{
			float size = (1 - (indicator.duration / 160F)) * 0.2F + 0.1F;
			Color c = indicator.color;
			RenderUtils.drawCube((float) (indicator.target.getX() + 0.5 - size / 2), (float) (indicator.target.getY() + 0.5 - size / 2), (float) (indicator.target.getZ() + 0.5 - size / 2), size, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		}

		// Undo

		RenderUtils.enableDefaultBlending();
		GlStateManager.enableTexture();

		GlStateManager.translated(playerX, playerY, playerZ);

		GlStateManager.enableDepthTest();
	}

	private void renderWat()
	{
		float renderX = -698;
		float renderY = 73;
		float renderZ = -92;

		float partialTicks = Minecraft.getInstance().getRenderPartialTicks();

		ActiveRenderInfo ari = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();


		int height = 5;

		int lineLength = 200;

		float time = ari.getRenderViewEntity().world.getGameTime() + partialTicks;

		Random rng = new Random(5);

		int loopHeight = height * 100 + lineLength;

		float circleRadius = 1F;

		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.lineWidth(2f);
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
		for (float p = 0; p < Math.PI * 2; p += Math.PI / 4F)
		{
			float myProgress = (float) ((time / 100F + rng.nextFloat() * 5) % (Math.PI * 0.5));
			int startY = (int) (Math.sin((myProgress)) * loopHeight);

			float g = rng.nextFloat();
			float b = rng.nextFloat();

			int endY = Math.min(loopHeight, startY + lineLength);

			if (startY < lineLength)
			{
				startY = Math.max(0, startY - lineLength);
				endY = Math.max(0, endY - lineLength);
			}
			else
			{
				startY -= lineLength;
				endY -= lineLength;
			}

			GlStateManager.begin(GL11.GL_LINE_STRIP);
			for (int modY = startY; modY <= endY; modY += 1)
			{
				float radius = (float) Math.sin(Math.PI / height * modY / 100F) * circleRadius;
				float modX = (float) Math.sin(p) * radius;
				float modZ = (float) Math.cos(p) * radius;

				float alpha = endY - modY < 10 ? (1 / 10F * (endY - modY)) : 1;

				GlStateManager.color4f(0.2F, g, b, alpha);

				GlStateManager.vertex3f(renderX + modX, renderY + modY / 100F, renderZ + modZ);
			}
			GlStateManager.end();
		}

		RenderUtils.enableDefaultBlending();
		GlStateManager.enableTexture();
	}

	public void tick()
	{
		Iterator<Indicator> indicatorIterator = indicators.iterator();

		while (indicatorIterator.hasNext())
		{
			Indicator i = indicatorIterator.next();

			i.duration--;

			if (i.duration == 0)
			{
				indicatorIterator.remove();
			}
		}

		PlayerEntity player = Minecraft.getInstance().player;

		if (player != null)
		{
			World world = player.world;

			if (world != null)
			{
				ItemStack main = player.getHeldItemMainhand();
				ItemStack off = player.getHeldItemOffhand();

				ItemStack rod = ItemStack.EMPTY;
				DiviningRodItem type;

				if (!main.isEmpty() && main.getItem() instanceof DiviningRodItem)
				{
					rod = main;
				}
				else if (!off.isEmpty() && off.getItem() instanceof DiviningRodItem)
				{
					rod = off;
				}

				if (!rod.isEmpty())
				{
					type = (DiviningRodItem) rod.getItem();
					BlockPos playerPos = player.getPosition();

					for (int i = 0; i < 60; i++)
					{
						modX++;

						if (modX == 6)
						{
							modX = -5;
							modZ++;

							if (modZ == 6)
							{
								modZ = -5;
								modY++;

								if (modY == 6)
								{
									modY = -5;
								}
							}
						}

						BlockPos target = playerPos.add(modX, modY, modZ);
						BlockState blockState = world.getBlockState(target);

						if (world.isAreaLoaded(target, 0))
						{
							int matchIndex = type.matches(blockState);
							if (matchIndex != -1)
							{
								Indicator indicator = new Indicator(target, 160, type.getColor(matchIndex), type);

								indicators.add(indicator);
							}
						}
					}
				}
			}
		}
	}

	private static class Indicator
	{
		BlockPos target;
		int duration;
		Color color;

		DiviningRodItem type;

		public Indicator(BlockPos target, int duration, Color color, DiviningRodItem type)
		{
			super();
			this.target = target;
			this.duration = duration;
			this.color = color;
			this.type = type;
		}
	}

	public static DiviningRodRenderer get()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DiviningRodRenderer();
		}
		return INSTANCE;
	}
}
