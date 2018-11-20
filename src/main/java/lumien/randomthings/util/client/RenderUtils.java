package lumien.randomthings.util.client;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jcraft.jorbis.Block;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.handler.RTEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BlockModelRenderer.AmbientOcclusionFace;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class RenderUtils
{
	static Gui gui = new Gui();

	static Cache<Biome, Integer> biomeColorCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();

	

	public static int getBiomeColor(IBlockAccess worldIn, final Biome biome, final BlockPos pos)
	{
		if (worldIn != null)
		{
			int i = 0;
			int j = 0;
			int k = 0;

			for (final BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
			{
				final Biome biomeA = worldIn.getBiome(blockpos$mutableblockpos);
				int l = Color.WHITE.getRGB();
				try
				{
					l = biomeColorCache.get(biomeA, new Callable<Integer>()
					{
						@Override
						public Integer call() throws Exception
						{
							Set<Type> types = BiomeDictionary.getTypes(biomeA);

							Color foliageColor = new Color(biomeA.getFoliageColorAtPos(blockpos$mutableblockpos));
							Color waterColorMultiplier = new Color(biomeA.getWaterColorMultiplier());
							Color grassColor = new Color(biomeA.getGrassColorAtPos(blockpos$mutableblockpos));

							Color colorResult = blend(blend(foliageColor, waterColorMultiplier, 0.5f), grassColor, 0.5f);

							for (Type t : types)
							{
								switch (t.getName())
								{
									case "BEACH":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.35f)), Math.min(255, (int) (colorResult.getGreen() * 1.3f)), Math.min(255, (int) (colorResult.getBlue() * 1.1f)));
										break;
									case "COLD":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (colorResult.getGreen())), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
										break;
									case "CONIFEROUS":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "DEAD":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
										break;
									case "DENSE":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1f)), Math.min(255, (int) (colorResult.getGreen() * 1.5f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "DRY":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
										break;
									case "END":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.4f)));
										break;
									case "FOREST":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.9f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
										break;
									case "HILLS":
										colorResult = new Color(Math.min(255, colorResult.getRed() + 40), Math.min(255, colorResult.getGreen() + 40), Math.min(255, colorResult.getBlue() + 40));
										break;
									case "HOT":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.1f)), Math.min(255, (int) (colorResult.getGreen() * 1f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
										break;
									case "JUNGLE":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.1f)), Math.min(255, (int) (colorResult.getGreen() * 1.5f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
										break;
									case "LUSH":
										colorResult = new Color(Math.min(255, (colorResult.getRed())), Math.min(255, (int) (colorResult.getGreen() * 1.4f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "MAGICAL":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.5f)), Math.min(255, (int) (colorResult.getGreen() * 1.3f)), Math.min(255, (int) (colorResult.getBlue() * 1.5f)));
										break;
									case "MESA":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.9f)), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (int) (colorResult.getBlue() * 0.5f)));
										break;
									case "MOUNTAIN":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.2f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
										break;
									case "MUSHROOM":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.3f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (int) (colorResult.getBlue() * 1.3f)));
										break;
									case "NETHER":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (int) (colorResult.getBlue() * 0.3f)));
										break;
									case "OCEAN":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.4f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "PLAINS":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.9f)), Math.min(255, (int) (colorResult.getGreen() * 0.9f)), Math.min(255, (int) (colorResult.getBlue() * 0.9f)));
										break;
									case "RIVER":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.6f)), Math.min(255, (int) (colorResult.getGreen() * 0.6f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "SANDY":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8)), Math.min(255, (int) (colorResult.getGreen() * 0.8)), Math.min(255, (int) (colorResult.getBlue() * 0.7f)));
										break;
									case "SAVANNA":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.9f)));
										break;
									case "SNOWY":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.4f)), Math.min(255, (int) (colorResult.getGreen() * 1.4f)), Math.min(255, (int) (colorResult.getBlue() * 1.5f)));
										break;
									case "SPARSE":
										colorResult = new Color(Math.min(255, (colorResult.getRed())), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "SPOOKY":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.7f)), Math.min(255, (int) (colorResult.getGreen() * 0.7f)), Math.min(255, (int) (colorResult.getBlue() * 0.7f)));
										break;
									case "SWAMP":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.6f)), Math.min(255, (int) (colorResult.getBlue() * 0.4f)));
										break;
									case "WASTELAND":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.2f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
										break;
									case "WATER":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.5f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (colorResult.getBlue())));
										break;
									case "WET":
										colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.6f)), Math.min(255, (colorResult.getGreen())), Math.min(255, (colorResult.getBlue())));
										break;
									default:
										break;
								}
							}

							return colorResult.getRGB();
						}

					});
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
				i += (l & 16711680) >> 16;
				j += (l & 65280) >> 8;
				k += l & 255;
			}

			return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
		}
		else
		{
			try
			{
				return biomeColorCache.get(biome, new Callable<Integer>()
				{
					@Override
					public Integer call() throws Exception
					{
						Set<Type> types = BiomeDictionary.getTypes(biome);

						Color foliageColor = new Color(biome.getFoliageColorAtPos(pos));
						Color waterColorMultiplier = new Color(biome.getWaterColorMultiplier());
						Color grassColor = new Color(biome.getGrassColorAtPos(pos));

						Color colorResult = blend(blend(foliageColor, waterColorMultiplier, 0.5f), grassColor, 0.5f);

						for (Type t : types)
						{
							switch (t.getName())
							{
								case "BEACH":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.35f)), Math.min(255, (int) (colorResult.getGreen() * 1.3f)), Math.min(255, (int) (colorResult.getBlue() * 1.1f)));
									break;
								case "COLD":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (colorResult.getGreen())), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
									break;
								case "CONIFEROUS":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "DEAD":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
									break;
								case "DENSE":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1f)), Math.min(255, (int) (colorResult.getGreen() * 1.5f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "DRY":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
									break;
								case "END":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.4f)));
									break;
								case "FOREST":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.9f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
									break;
								case "HILLS":
									colorResult = new Color(Math.min(255, colorResult.getRed() + 40), Math.min(255, colorResult.getGreen() + 40), Math.min(255, colorResult.getBlue() + 40));
									break;
								case "HOT":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.1f)), Math.min(255, (int) (colorResult.getGreen() * 1f)), Math.min(255, (int) (colorResult.getBlue() * 0.8f)));
									break;
								case "JUNGLE":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.1f)), Math.min(255, (int) (colorResult.getGreen() * 1.5f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
									break;
								case "LUSH":
									colorResult = new Color(Math.min(255, (colorResult.getRed())), Math.min(255, (int) (colorResult.getGreen() * 1.4f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "MAGICAL":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.5f)), Math.min(255, (int) (colorResult.getGreen() * 1.3f)), Math.min(255, (int) (colorResult.getBlue() * 1.5f)));
									break;
								case "MESA":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.9f)), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (int) (colorResult.getBlue() * 0.5f)));
									break;
								case "MOUNTAIN":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.2f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
									break;
								case "MUSHROOM":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.3f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (int) (colorResult.getBlue() * 1.3f)));
									break;
								case "NETHER":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.8f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (int) (colorResult.getBlue() * 0.3f)));
									break;
								case "OCEAN":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.4f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "PLAINS":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.9f)), Math.min(255, (int) (colorResult.getGreen() * 0.9f)), Math.min(255, (int) (colorResult.getBlue() * 0.9f)));
									break;
								case "RIVER":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.6f)), Math.min(255, (int) (colorResult.getGreen() * 0.6f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "SANDY":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.8)), Math.min(255, (int) (colorResult.getGreen() * 0.8)), Math.min(255, (int) (colorResult.getBlue() * 0.7f)));
									break;
								case "SAVANNA":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.1f)), Math.min(255, (int) (colorResult.getBlue() * 0.9f)));
									break;
								case "SNOWY":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.4f)), Math.min(255, (int) (colorResult.getGreen() * 1.4f)), Math.min(255, (int) (colorResult.getBlue() * 1.5f)));
									break;
								case "SPARSE":
									colorResult = new Color(Math.min(255, (colorResult.getRed())), Math.min(255, (int) (colorResult.getGreen() * 0.8f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "SPOOKY":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.7f)), Math.min(255, (int) (colorResult.getGreen() * 0.7f)), Math.min(255, (int) (colorResult.getBlue() * 0.7f)));
									break;
								case "SWAMP":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.4f)), Math.min(255, (int) (colorResult.getGreen() * 0.6f)), Math.min(255, (int) (colorResult.getBlue() * 0.4f)));
									break;
								case "WASTELAND":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 1.2f)), Math.min(255, (int) (colorResult.getGreen() * 1.2f)), Math.min(255, (int) (colorResult.getBlue() * 1.2f)));
									break;
								case "WATER":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.5f)), Math.min(255, (int) (colorResult.getGreen() * 0.5f)), Math.min(255, (colorResult.getBlue())));
									break;
								case "WET":
									colorResult = new Color(Math.min(255, (int) (colorResult.getRed() * 0.6f)), Math.min(255, (colorResult.getGreen())), Math.min(255, (colorResult.getBlue())));
									break;
								default:
									break;
							}
						}

						return colorResult.getRGB();
					}

				});
			}
			catch (ExecutionException e)
			{
				e.printStackTrace();

				return Color.WHITE.getRGB();
			}
		}
	}

	public static Color blend(Color c1, Color c2, float ratio)
	{
		if (ratio > 1f)
			ratio = 1f;
		else if (ratio < 0f)
			ratio = 0f;
		float iRatio = 1.0f - ratio;

		int i1 = c1.getRGB();
		int i2 = c2.getRGB();

		int a1 = (i1 >> 24 & 0xff);
		int r1 = ((i1 & 0xff0000) >> 16);
		int g1 = ((i1 & 0xff00) >> 8);
		int b1 = (i1 & 0xff);

		int a2 = (i2 >> 24 & 0xff);
		int r2 = ((i2 & 0xff0000) >> 16);
		int g2 = ((i2 & 0xff00) >> 8);
		int b2 = (i2 & 0xff);

		int a = (int) ((a1 * iRatio) + (a2 * ratio));
		int r = (int) ((r1 * iRatio) + (r2 * ratio));
		int g = (int) ((g1 * iRatio) + (g2 * ratio));
		int b = (int) ((b1 * iRatio) + (b2 * ratio));

		return new Color(a << 24 | r << 16 | g << 8 | b);
	}

	public static void drawCube(float posX, float posY, float posZ, float width, float length, float height, int red, int green, int blue, int alpha)
	{
		Minecraft.getMinecraft().entityRenderer.disableLightmap();

		Tessellator t = Tessellator.getInstance();
		BufferBuilder wr = t.getBuffer();

		GlStateManager.disableTexture2D();

		GlStateManager.translate(posX, posY, posZ);
		
		wr.begin(7, DefaultVertexFormats.POSITION_COLOR);

		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P4

		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P4

		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(width, 0F, length).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, 0F, length).color(red, green, blue, alpha).endVertex(); // P1

		wr.pos(0F, height, 0F).color(red, green, blue, alpha).endVertex(); // P1
		wr.pos(0F, height, length).color(red, green, blue, alpha).endVertex(); // P2
		wr.pos(width, height, length).color(red, green, blue, alpha).endVertex(); // P3
		wr.pos(width, height, 0F).color(red, green, blue, alpha).endVertex(); // P4

		t.draw();

		GlStateManager.translate(-posX, -posY, -posZ);
		GlStateManager.enableTexture2D();

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}

	public static void renderModelCustomSides(IBlockAccess worldIn, IBakedModel modelIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, Map<EnumFacing, Boolean> faceMap, long rand)
	{
		boolean flag = Minecraft.isAmbientOcclusionEnabled() && stateIn.getLightValue(worldIn, posIn) == 0 && modelIn.isAmbientOcclusion();

		BlockModelRenderer bmr = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer();

		if (bmr != null && stateIn != null && modelIn != null)
		{
			try
			{
				if (flag)
				{
					float[] afloat = new float[EnumFacing.values().length * 2];
					BitSet bitset = new BitSet(3);

					AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = bmr.new AmbientOcclusionFace();

					for (EnumFacing enumfacing : EnumFacing.values())
					{
						List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);

						if (!list.isEmpty() && faceMap.get(enumfacing))
						{
							renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list, afloat, bitset, blockmodelrenderer$ambientocclusionface);
						}
					}

					List<BakedQuad> list1 = modelIn.getQuads(stateIn, (EnumFacing) null, rand);

					if (!list1.isEmpty())
					{
						renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list1, afloat, bitset, blockmodelrenderer$ambientocclusionface);
					}
				}
				else
				{
					BitSet bitset = new BitSet(3);

					for (EnumFacing enumfacing : EnumFacing.values())
					{
						List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);

						if (!list.isEmpty() && faceMap.get(enumfacing))
						{
							int i = stateIn.getPackedLightmapCoords(worldIn, posIn.offset(enumfacing));
							renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, list, bitset);
							flag = true;
						}
					}

					List<BakedQuad> list1 = modelIn.getQuads(stateIn, (EnumFacing) null, rand);

					if (!list1.isEmpty())
					{
						renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, list1, bitset);
					}
				}
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
				CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
				crashreportcategory.addCrashSection("Using AO", Boolean.valueOf(flag));
				throw new ReportedException(crashreport);
			}
		}
	}

	public static Map<EnumFacing, List<BakedQuad>> getQuadFaceMapFromState(IBlockState state)
	{
		HashMap<EnumFacing, List<BakedQuad>> map = new HashMap<>();

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(state);

		Random rng = new Random();

		for (EnumFacing f : EnumFacing.values())
		{
			map.put(f, model.getQuads(state, f, rng.nextLong()));
		}

		map.put(null, model.getQuads(state, null, rng.nextLong()));

		return map;
	}

	public static void drawCube(float posX, float posY, float posZ, float size, int red, int green, int blue, int alpha)
	{
		drawCube(posX, posY, posZ, size, size, size, red, green, blue, alpha);
	}

	public static void enableDefaultBlending()
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private static void renderQuadsSmooth(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, BufferBuilder buffer, List<BakedQuad> list, float[] quadBounds, BitSet bitSet, BlockModelRenderer.AmbientOcclusionFace aoFace)
	{
		Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
		double d0 = (double) posIn.getX() + vec3d.x;
		double d1 = (double) posIn.getY() + vec3d.y;
		double d2 = (double) posIn.getZ() + vec3d.z;
		int i = 0;

		for (int j = list.size(); i < j; ++i)
		{
			BakedQuad bakedquad = list.get(i);
			fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
			aoFace.updateVertexBrightness(blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet);
			buffer.addVertexData(bakedquad.getVertexData());
			buffer.putBrightness4(aoFace.vertexBrightness[0], aoFace.vertexBrightness[1], aoFace.vertexBrightness[2], aoFace.vertexBrightness[3]);
			if (bakedquad.shouldApplyDiffuseLighting())
			{
				float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(bakedquad.getFace());
				aoFace.vertexColorMultiplier[0] *= diffuse;
				aoFace.vertexColorMultiplier[1] *= diffuse;
				aoFace.vertexColorMultiplier[2] *= diffuse;
				aoFace.vertexColorMultiplier[3] *= diffuse;
			}
			if (bakedquad.hasTintIndex())
			{
				int k = Minecraft.getMinecraft().getBlockColors().colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					k = TextureUtil.anaglyphColor(k);
				}

				float f = (float) (k >> 16 & 255) / 255.0F;
				float f1 = (float) (k >> 8 & 255) / 255.0F;
				float f2 = (float) (k & 255) / 255.0F;
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0] * f, aoFace.vertexColorMultiplier[0] * f1, aoFace.vertexColorMultiplier[0] * f2, 4);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1] * f, aoFace.vertexColorMultiplier[1] * f1, aoFace.vertexColorMultiplier[1] * f2, 3);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2] * f, aoFace.vertexColorMultiplier[2] * f1, aoFace.vertexColorMultiplier[2] * f2, 2);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3] * f, aoFace.vertexColorMultiplier[3] * f1, aoFace.vertexColorMultiplier[3] * f2, 1);
			}
			else
			{
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[0], 4);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[1], 3);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[2], 2);
				buffer.putColorMultiplier(aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], aoFace.vertexColorMultiplier[3], 1);
			}

			buffer.putPosition(d0, d1, d2);
		}
	}

	private static void fillQuadBounds(IBlockState stateIn, int[] vertexData, EnumFacing face, @Nullable float[] quadBounds, BitSet boundsFlags)
	{
		float f = 32.0F;
		float f1 = 32.0F;
		float f2 = 32.0F;
		float f3 = -32.0F;
		float f4 = -32.0F;
		float f5 = -32.0F;

		for (int i = 0; i < 4; ++i)
		{
			float f6 = Float.intBitsToFloat(vertexData[i * 7]);
			float f7 = Float.intBitsToFloat(vertexData[i * 7 + 1]);
			float f8 = Float.intBitsToFloat(vertexData[i * 7 + 2]);
			f = Math.min(f, f6);
			f1 = Math.min(f1, f7);
			f2 = Math.min(f2, f8);
			f3 = Math.max(f3, f6);
			f4 = Math.max(f4, f7);
			f5 = Math.max(f5, f8);
		}

		if (quadBounds != null)
		{
			quadBounds[EnumFacing.WEST.getIndex()] = f;
			quadBounds[EnumFacing.EAST.getIndex()] = f3;
			quadBounds[EnumFacing.DOWN.getIndex()] = f1;
			quadBounds[EnumFacing.UP.getIndex()] = f4;
			quadBounds[EnumFacing.NORTH.getIndex()] = f2;
			quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
			int j = EnumFacing.values().length;
			quadBounds[EnumFacing.WEST.getIndex() + j] = 1.0F - f;
			quadBounds[EnumFacing.EAST.getIndex() + j] = 1.0F - f3;
			quadBounds[EnumFacing.DOWN.getIndex() + j] = 1.0F - f1;
			quadBounds[EnumFacing.UP.getIndex() + j] = 1.0F - f4;
			quadBounds[EnumFacing.NORTH.getIndex() + j] = 1.0F - f2;
			quadBounds[EnumFacing.SOUTH.getIndex() + j] = 1.0F - f5;
		}

		float f9 = 1.0E-4F;
		float f10 = 0.9999F;

		switch (face)
		{
			case DOWN:
				boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
				boundsFlags.set(0, (f1 < 1.0E-4F || stateIn.isFullCube()) && f1 == f4);
				break;
			case UP:
				boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
				boundsFlags.set(0, (f4 > 0.9999F || stateIn.isFullCube()) && f1 == f4);
				break;
			case NORTH:
				boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
				boundsFlags.set(0, (f2 < 1.0E-4F || stateIn.isFullCube()) && f2 == f5);
				break;
			case SOUTH:
				boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
				boundsFlags.set(0, (f5 > 0.9999F || stateIn.isFullCube()) && f2 == f5);
				break;
			case WEST:
				boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
				boundsFlags.set(0, (f < 1.0E-4F || stateIn.isFullCube()) && f == f3);
				break;
			case EAST:
				boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
				boundsFlags.set(0, (f3 > 0.9999F || stateIn.isFullCube()) && f == f3);
		}
	}

	private static void renderQuadsFlat(IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn, boolean ownBrightness, BufferBuilder buffer, List<BakedQuad> list, BitSet bitSet)
	{
		Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
		double d0 = (double) posIn.getX() + vec3d.x;
		double d1 = (double) posIn.getY() + vec3d.y;
		double d2 = (double) posIn.getZ() + vec3d.z;
		int i = 0;

		for (int j = list.size(); i < j; ++i)
		{
			BakedQuad bakedquad = list.get(i);

			if (ownBrightness)
			{
				fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), (float[]) null, bitSet);
				BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
				brightnessIn = stateIn.getPackedLightmapCoords(blockAccessIn, blockpos);
			}

			buffer.addVertexData(bakedquad.getVertexData());
			buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);

			if (bakedquad.hasTintIndex())
			{
				int k = Minecraft.getMinecraft().getBlockColors().colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable)
				{
					k = TextureUtil.anaglyphColor(k);
				}

				float f = (float) (k >> 16 & 255) / 255.0F;
				float f1 = (float) (k >> 8 & 255) / 255.0F;
				float f2 = (float) (k & 255) / 255.0F;
				if (bakedquad.shouldApplyDiffuseLighting())
				{
					float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(bakedquad.getFace());
					f *= diffuse;
					f1 *= diffuse;
					f2 *= diffuse;
				}
				buffer.putColorMultiplier(f, f1, f2, 4);
				buffer.putColorMultiplier(f, f1, f2, 3);
				buffer.putColorMultiplier(f, f1, f2, 2);
				buffer.putColorMultiplier(f, f1, f2, 1);
			}
			else if (bakedquad.shouldApplyDiffuseLighting())
			{
				float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(bakedquad.getFace());
				buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
				buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
				buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
				buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
			}

			buffer.putPosition(d0, d1, d2);
		}
	}
}
