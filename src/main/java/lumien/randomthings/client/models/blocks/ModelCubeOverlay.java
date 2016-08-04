package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.primitives.Ints;

import lumien.randomthings.client.RenderReference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;

public class ModelCubeOverlay implements IBakedModel
{
	boolean isAmbientOcclusion;

	TextureAtlasSprite particleTexture;


	HashMap<EnumFacing, List<BakedQuad>> itemQuads;

	Map<EnumFacing, List<BakedQuad>> originalQuads;

	Map<EnumFacing, List<BakedQuad>> overlayQuads;

	public ModelCubeOverlay(Map<EnumFacing, List<BakedQuad>> originalQuads, HashMap<EnumFacing, TextureAtlasSprite> overlays, TextureAtlasSprite particleTexture, boolean isAmbientOcclusion)
	{
		this.originalQuads = originalQuads;
		this.itemQuads = new HashMap<EnumFacing, List<BakedQuad>>();

		this.particleTexture = particleTexture;
		this.isAmbientOcclusion = isAmbientOcclusion;

		overlayQuads = new HashMap<EnumFacing, List<BakedQuad>>();

		itemQuads.put(null, originalQuads.get(null));

		for (EnumFacing f : EnumFacing.values())
		{
			List<BakedQuad> facingQuads = originalQuads.get(f);
			
			itemQuads.put(f, new ArrayList<BakedQuad>(originalQuads.get(f)));
			
			originalQuads.put(f, new ArrayList<BakedQuad>(originalQuads.get(f)));
		}

		for (EnumFacing f : EnumFacing.values())
		{
			if (overlays.containsKey(f))
			{
				BakedQuad overlayQuad = createSidedBakedQuad(0F, 1F, 0F, 1F, 1F, overlays.get(f), f);

				List<BakedQuad> sideQuadList;
				overlayQuads.put(f, sideQuadList = new ArrayList<BakedQuad>());
				sideQuadList.add(overlayQuad);

				itemQuads.get(f).add(overlayQuad);
			}
		}
	}

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v, EnumFacing side)
	{
		int normal;

		int xN = ((byte) (side.getFrontOffsetX() * 127)) & 0xFF;
		int yN = ((byte) (side.getFrontOffsetY() * 127)) & 0xFF;
		int zN = ((byte) (side.getFrontOffsetZ() * 127)) & 0xFF;

		normal = xN | (yN << 0x08) | (zN << 0x10);

		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), normal };
	}

	private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, TextureAtlasSprite texture, EnumFacing side)
	{
		Vec3d c1 = rotate(new Vec3d(x1 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);
		Vec3d c2 = rotate(new Vec3d(x1 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3d c3 = rotate(new Vec3d(x2 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3d c4 = rotate(new Vec3d(x2 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);

		EnumFacing rotation = EnumFacing.SOUTH;

		if (side == EnumFacing.WEST || side == EnumFacing.EAST || side == EnumFacing.SOUTH)
		{
			rotation = EnumFacing.SOUTH;
		}
		else if (side == EnumFacing.NORTH)
		{
			rotation = EnumFacing.WEST;
			c1 = rotate(c1.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c2 = rotate(c2.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c3 = rotate(c3.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c4 = rotate(c4.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
		}

		if (side != EnumFacing.UP && side != EnumFacing.SOUTH)
		{
			c1 = rotate(c1.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c2 = rotate(c2.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c3 = rotate(c3.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c4 = rotate(c4.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
		}

		return new BakedQuad(Ints.concat(vertexToInts((float) c1.xCoord, (float) c1.yCoord, (float) c1.zCoord, -1, texture, 0, 0, side), vertexToInts((float) c2.xCoord, (float) c2.yCoord, (float) c2.zCoord, -1, texture, 0, 16, side), vertexToInts((float) c3.xCoord, (float) c3.yCoord, (float) c3.zCoord, -1, texture, 16, 16, side), vertexToInts((float) c4.xCoord, (float) c4.yCoord, (float) c4.zCoord, -1, texture, 16, 0, side)), -1, side, texture, false, DefaultVertexFormats.ITEM);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return isAmbientOcclusion;
	}

	@Override
	public boolean isGui3d()
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return particleTexture;
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return RenderReference.BLOCK_ITEM_TRANSFORM;
	}

	private static Vec3d rotate(Vec3d vec, EnumFacing side)
	{
		switch (side)
		{
			case DOWN:
				return new Vec3d(vec.xCoord, -vec.yCoord, -vec.zCoord);
			case UP:
				return new Vec3d(vec.xCoord, vec.yCoord, vec.zCoord);
			case NORTH:
				return new Vec3d(vec.xCoord, vec.zCoord, -vec.yCoord);
			case SOUTH:
				return new Vec3d(vec.xCoord, -vec.zCoord, vec.yCoord);
			case WEST:
				return new Vec3d(-vec.yCoord, vec.xCoord, vec.zCoord);
			case EAST:
				return new Vec3d(vec.yCoord, -vec.xCoord, vec.zCoord);
		}
		return null;
	}

	private static Vec3d revRotate(Vec3d vec, EnumFacing side)
	{
		switch (side)
		{
			case DOWN:
				return new Vec3d(vec.xCoord, -vec.yCoord, -vec.zCoord);
			case UP:
				return new Vec3d(vec.xCoord, vec.yCoord, vec.zCoord);
			case NORTH:
				return new Vec3d(vec.xCoord, -vec.zCoord, vec.yCoord);
			case SOUTH:
				return new Vec3d(vec.xCoord, vec.zCoord, -vec.yCoord);
			case WEST:
				return new Vec3d(vec.yCoord, -vec.xCoord, vec.zCoord);
			case EAST:
				return new Vec3d(-vec.yCoord, vec.xCoord, vec.zCoord);
		}
		return null;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();

		if (state == null)
		{
			return itemQuads.get(side);
		}

		if (layer == BlockRenderLayer.SOLID)
		{
			if (originalQuads.containsKey(side))
			{
				return originalQuads.get(side);
			}
		}
		else if (layer == BlockRenderLayer.TRANSLUCENT)
		{
			if (overlayQuads.containsKey(side))
			{
				return overlayQuads.get(side);
			}
		}

		return new ArrayList<BakedQuad>();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}

}
