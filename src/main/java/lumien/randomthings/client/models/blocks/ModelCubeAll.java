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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Vec3d;

public class ModelCubeAll implements IBakedModel
{
	TextureAtlasSprite texture;
	boolean isAmbientOcclusion;

	List<BakedQuad> generalQuads;
	Map<EnumFacing, List<BakedQuad>> faceQuads;

	Rotation rotation;

	static int[][][] uvArrays;
	static
	{
		uvArrays = new int[][][] { { { 0, 0 }, { 0, 16 }, { 16, 16 }, { 16, 0 } }, { { 0, 16 }, { 16, 16 }, { 16, 0 }, { 0, 0 } }, { { 16, 16 }, { 16, 0 }, { 0, 0 }, { 0, 16 } }, { { 16, 0 }, { 0, 0 }, { 0, 16 }, { 16, 16 } } };
	}

	public ModelCubeAll(TextureAtlasSprite texture, boolean isAmbientOcclusion, Rotation rotation)
	{
		this.texture = texture;
		this.isAmbientOcclusion = isAmbientOcclusion;
		this.rotation = rotation;

		generalQuads = new ArrayList<>();
		faceQuads = new HashMap<>();

		for (EnumFacing f : EnumFacing.values())
		{
			List<BakedQuad> faceQuadList;
			faceQuads.put(f, faceQuadList = new ArrayList<>());
			BakedQuad quad = createSidedBakedQuad(0, 1, 0, 1, 1, texture, f);

			generalQuads.add(quad);
			faceQuadList.add(quad);
		}
	}

	public ModelCubeAll(TextureAtlasSprite texture, boolean isAmbientOcclusion)
	{
		this(texture, isAmbientOcclusion, Rotation.NONE);
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
		int[][] uvs = uvArrays[rotation.ordinal()];

		Vec3d v1 = rotate(new Vec3d(x1 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);
		Vec3d v2 = rotate(new Vec3d(x1 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3d v3 = rotate(new Vec3d(x2 - .5, y - .5, z2 - .5), side).addVector(.5, 0.5, .5);
		Vec3d v4 = rotate(new Vec3d(x2 - .5, y - .5, z1 - .5), side).addVector(.5, 0.5, .5);

		EnumFacing currentRotation = EnumFacing.SOUTH;

		if (side == EnumFacing.WEST || side == EnumFacing.EAST || side == EnumFacing.SOUTH)
		{
			currentRotation = EnumFacing.SOUTH;
		}
		else if (side == EnumFacing.NORTH)
		{
			currentRotation = EnumFacing.WEST;
			v1 = rotate(v1.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v2 = rotate(v2.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v3 = rotate(v3.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v4 = rotate(v4.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
		}

		if (side != EnumFacing.UP && side != EnumFacing.SOUTH && side != EnumFacing.DOWN)
		{
			v1 = rotate(v1.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v2 = rotate(v2.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v3 = rotate(v3.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
			v4 = rotate(v4.addVector(-.5, -.5, -.5), currentRotation).addVector(.5, 0.5, .5);
		}

		return new BakedQuad(Ints.concat(vertexToInts((float) v1.x, (float) v1.y, (float) v1.z, -1, texture, uvs[0][0], uvs[0][1], side), vertexToInts((float) v2.x, (float) v2.y, (float) v2.z, -1, texture, uvs[1][0], uvs[1][1], side), vertexToInts((float) v3.x, (float) v3.y, (float) v3.z, -1, texture, uvs[2][0], uvs[2][1], side), vertexToInts((float) v4.x, (float) v4.y, (float) v4.z, -1, texture, uvs[3][0], uvs[3][1], side)), 0, side, texture, false, DefaultVertexFormats.ITEM);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (side == null)
		{
			return generalQuads;
		}

		return faceQuads.get(side);
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
		return texture;
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
			return new Vec3d(vec.x, -vec.y, -vec.z);
		case UP:
			return new Vec3d(vec.x, vec.y, vec.z);
		case NORTH:
			return new Vec3d(vec.x, vec.z, -vec.y);
		case SOUTH:
			return new Vec3d(vec.x, -vec.z, vec.y);
		case WEST:
			return new Vec3d(-vec.y, vec.x, vec.z);
		case EAST:
			return new Vec3d(vec.y, -vec.x, vec.z);
		}
		return null;
	}

	private static Vec3d revRotate(Vec3d vec, EnumFacing side)
	{
		switch (side)
		{
		case DOWN:
			return new Vec3d(vec.x, -vec.y, -vec.z);
		case UP:
			return new Vec3d(vec.x, vec.y, vec.z);
		case NORTH:
			return new Vec3d(vec.x, -vec.z, vec.y);
		case SOUTH:
			return new Vec3d(vec.x, vec.z, -vec.y);
		case WEST:
			return new Vec3d(vec.y, -vec.x, vec.z);
		case EAST:
			return new Vec3d(-vec.y, vec.x, vec.z);
		}
		return null;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}
