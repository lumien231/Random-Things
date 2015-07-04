package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lumien.randomthings.client.RenderReference;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IFlexibleBakedModel;

import com.google.common.primitives.Ints;

public class ModelCubeOverlay implements IFlexibleBakedModel
{
	IBakedModel original;
	boolean isAmbientOcclusion;
	boolean itemModel;

	HashMap<EnumFacing, TextureAtlasSprite> overlays;

	public ModelCubeOverlay(IBakedModel original, HashMap<EnumFacing, TextureAtlasSprite> overlays, boolean isAmbientOcclusion, boolean itemModel)
	{
		this.original = original;
		this.isAmbientOcclusion = isAmbientOcclusion;
		this.overlays = overlays;
		this.itemModel = itemModel;
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_)
	{
		List<BakedQuad> ret = new ArrayList<BakedQuad>();

		EnumWorldBlockLayer layer = MinecraftForgeClient.getRenderLayer();

		if (itemModel || layer == EnumWorldBlockLayer.SOLID)
		{
			ret.addAll(original.getFaceQuads(p_177551_1_));
		}

		if (itemModel || layer == EnumWorldBlockLayer.TRANSLUCENT)
		{
			if (overlays.containsKey(p_177551_1_))
			{
				ret.add(createSidedBakedQuad(-0.0001F, 1.0001F, -0.0001F, 1.0001F, 1.0001F, overlays.get(p_177551_1_), p_177551_1_));
			}
		}

		return ret;
	}

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
	{
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

	private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, TextureAtlasSprite texture, EnumFacing side)
	{
		Vec3 c1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);
		Vec3 c2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3 c3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3 c4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);

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

		return new BakedQuad(Ints.concat(vertexToInts((float) c1.xCoord, (float) c1.yCoord, (float) c1.zCoord, -1, texture, 0, 0), vertexToInts((float) c2.xCoord, (float) c2.yCoord, (float) c2.zCoord, -1, texture, 0, 16), vertexToInts((float) c3.xCoord, (float) c3.yCoord, (float) c3.zCoord, -1, texture, 16, 16), vertexToInts((float) c4.xCoord, (float) c4.yCoord, (float) c4.zCoord, -1, texture, 16, 0)), -1, side);
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		List<BakedQuad> ret = new ArrayList<BakedQuad>();
		EnumWorldBlockLayer layer = MinecraftForgeClient.getRenderLayer();

		if (itemModel || layer == EnumWorldBlockLayer.SOLID)
		{
			ret.addAll(original.getGeneralQuads());
		}

		if (itemModel || layer == EnumWorldBlockLayer.TRANSLUCENT)
		{
			for (EnumFacing f : EnumFacing.values())
			{
				if (overlays.containsKey(f))
				{
					ret.add(createSidedBakedQuad(-0.0001F, 1.0001F, -0.0001F, 1.0001F, 1.0001F, overlays.get(f), f));
				}
			}
		}

		return ret;
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
	public TextureAtlasSprite getTexture()
	{
		return original.getTexture();
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return RenderReference.BLOCK_ITEM_TRANSFORM;
	}

	private static Vec3 rotate(Vec3 vec, EnumFacing side)
	{
		switch (side)
		{
			case DOWN:
				return new Vec3(vec.xCoord, -vec.yCoord, -vec.zCoord);
			case UP:
				return new Vec3(vec.xCoord, vec.yCoord, vec.zCoord);
			case NORTH:
				return new Vec3(vec.xCoord, vec.zCoord, -vec.yCoord);
			case SOUTH:
				return new Vec3(vec.xCoord, -vec.zCoord, vec.yCoord);
			case WEST:
				return new Vec3(-vec.yCoord, vec.xCoord, vec.zCoord);
			case EAST:
				return new Vec3(vec.yCoord, -vec.xCoord, vec.zCoord);
		}
		return null;
	}

	private static Vec3 revRotate(Vec3 vec, EnumFacing side)
	{
		switch (side)
		{
			case DOWN:
				return new Vec3(vec.xCoord, -vec.yCoord, -vec.zCoord);
			case UP:
				return new Vec3(vec.xCoord, vec.yCoord, vec.zCoord);
			case NORTH:
				return new Vec3(vec.xCoord, -vec.zCoord, vec.yCoord);
			case SOUTH:
				return new Vec3(vec.xCoord, vec.zCoord, -vec.yCoord);
			case WEST:
				return new Vec3(vec.yCoord, -vec.xCoord, vec.zCoord);
			case EAST:
				return new Vec3(-vec.yCoord, vec.xCoord, vec.zCoord);
		}
		return null;
	}

	@Override
	public VertexFormat getFormat()
	{
		return new VertexFormat();
	}

}
