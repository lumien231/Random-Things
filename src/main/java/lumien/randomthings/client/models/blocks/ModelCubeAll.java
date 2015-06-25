package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.IFlexibleBakedModel;

import com.google.common.primitives.Ints;

public class ModelCubeAll implements IFlexibleBakedModel
{
	TextureAtlasSprite texture;
	boolean isAmbientOcclusion;

	public ModelCubeAll(TextureAtlasSprite texture, boolean isAmbientOcclusion)
	{
		this.texture = texture;
		this.isAmbientOcclusion = isAmbientOcclusion;
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_)
	{
		return new ArrayList();
	}

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
	{
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

	private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, TextureAtlasSprite texture, EnumFacing side)
	{
		Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
		Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
		Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
		Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
		return new BakedQuad(Ints.concat(vertexToInts((float) v1.xCoord, (float) v1.yCoord, (float) v1.zCoord, -1, texture, 0, 0), vertexToInts((float) v2.xCoord, (float) v2.yCoord, (float) v2.zCoord, -1, texture, 0, 16), vertexToInts((float) v3.xCoord, (float) v3.yCoord, (float) v3.zCoord, -1, texture, 16, 16), vertexToInts((float) v4.xCoord, (float) v4.yCoord, (float) v4.zCoord, -1, texture, 16, 0)), -1, side);
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		int len = 1 * 5 + 1;
		List<BakedQuad> ret = new ArrayList<BakedQuad>();
		for (EnumFacing f : EnumFacing.values())
		{
			ret.add(createSidedBakedQuad(0, 1, 0, 1, 1, getTexture(), f));
		}
		return ret;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
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
		return texture;
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
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
