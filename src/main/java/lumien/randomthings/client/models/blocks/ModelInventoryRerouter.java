package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.primitives.Ints;

import lumien.randomthings.block.BlockInventoryRerouter;
import lumien.randomthings.lib.AtlasSprite;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelInventoryRerouter implements IBakedModel
{
	Cache<CacheEntry, HashMap<EnumFacing, List<BakedQuad>>> modelCache;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/side")
	public static TextureAtlasSprite interfaceBase;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/side_con")
	public static TextureAtlasSprite interfaceCon;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_down")
	public static TextureAtlasSprite overlay_down;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_up")
	public static TextureAtlasSprite overlay_up;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_north")
	public static TextureAtlasSprite overlay_north;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_south")
	public static TextureAtlasSprite overlay_south;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_west")
	public static TextureAtlasSprite overlay_west;

	@AtlasSprite(resource = "randomthings:blocks/inventoryrerouter/overlay_east")
	public static TextureAtlasSprite overlay_east;

	ArrayList<BakedQuad> emptyList;

	static final float part = 1F / 16F;;

	public ModelInventoryRerouter()
	{
		modelCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(120, TimeUnit.SECONDS).build();
		emptyList = new ArrayList<>();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (side == null || state == null)
		{
			return emptyList;
		}

		HashMap<EnumFacing, EnumFacing> overrideData = ((IExtendedBlockState) state).getValue(BlockInventoryRerouter.OVERRIDE_DATA);

		if (overrideData == null)
		{
			return emptyList;
		}

		EnumFacing blockFacing = state.getValue(BlockInventoryRerouter.FACING);

		CacheEntry entry = new CacheEntry(overrideData, blockFacing);

		HashMap<EnumFacing, List<BakedQuad>> quadMap = modelCache.getIfPresent(entry);
		if (quadMap != null)
		{
			return quadMap.get(side);
		}
		else
		{
			quadMap = new HashMap<>();

			for (EnumFacing facing : EnumFacing.VALUES)
			{
				List<BakedQuad> quadList = new ArrayList<>();
				quadMap.put(facing, quadList);

				if (facing == blockFacing)
				{
					BakedQuad baseQuad = createSidedBakedQuad(0, 1, 0, 1, 1, 0, 0, 16, 16, -1, interfaceCon, facing);
					quadList.add(baseQuad);
				}
				else
				{
					BakedQuad baseQuad = createSidedBakedQuad(0, 1, 0, 1, 1, 0, 0, 16, 16, -1, interfaceBase, facing);
					quadList.add(baseQuad);

					EnumFacing override = overrideData.get(facing);

					if (override != null)
					{
						TextureAtlasSprite overlaySprite = null;

						if (override == EnumFacing.DOWN)
						{
							overlaySprite = overlay_down;
						}
						else if (override == EnumFacing.UP)
						{
							overlaySprite = overlay_up;
						}
						else if (override == EnumFacing.NORTH)
						{
							overlaySprite = overlay_north;
						}
						else if (override == EnumFacing.SOUTH)
						{
							overlaySprite = overlay_south;
						}
						else if (override == EnumFacing.WEST)
						{
							overlaySprite = overlay_west;
						}
						else if (override == EnumFacing.EAST)
						{
							overlaySprite = overlay_east;
						}

						if (overlaySprite != null)
						{
							BakedQuad overrideQuad = createSidedBakedQuad(0, 1, 0, 1, 1, 0, 0, 16, 16, -1, overlaySprite, facing);
							quadList.add(overrideQuad);
						}
					}
				}
			}

			modelCache.put(new CacheEntry(overrideData, blockFacing), quadMap);

			return quadMap.get(side);
		}

	}

	private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, TextureAtlasSprite texture, EnumFacing side)
	{
		Vec3d v1 = rotate(new Vec3d(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
		Vec3d v2 = rotate(new Vec3d(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
		Vec3d v3 = rotate(new Vec3d(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
		Vec3d v4 = rotate(new Vec3d(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);

		return new BakedQuad(Ints.concat(vertexToInts((float) v1.x, (float) v1.y, (float) v1.z, -1, texture, 0, 0, side), vertexToInts((float) v2.x, (float) v2.y, (float) v2.z, -1, texture, 0, 16, side), vertexToInts((float) v3.x, (float) v3.y, (float) v3.z, -1, texture, 16, 16, side), vertexToInts((float) v4.x, (float) v4.y, (float) v4.z, -1, texture, 16, 0, side)), -1, side, texture, false, DefaultVertexFormats.ITEM);
	}

	private BakedQuad createSidedBakedQuad(float x1, float x2, float z1, float z2, float y, float u1, float v1, float u2, float v2, int tintIndex, TextureAtlasSprite texture, EnumFacing side)
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

		if (side != EnumFacing.UP && side != EnumFacing.SOUTH && side != EnumFacing.DOWN)
		{
			c1 = rotate(c1.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c2 = rotate(c2.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c3 = rotate(c3.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c4 = rotate(c4.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
		}

		return new BakedQuad(Ints.concat(vertexToInts((float) c1.x, (float) c1.y, (float) c1.z, -1, texture, u1, v1, side), vertexToInts((float) c2.x, (float) c2.y, (float) c2.z, -1, texture, u1, v2, side), vertexToInts((float) c3.x, (float) c3.y, (float) c3.z, -1, texture, u2, v2, side), vertexToInts((float) c4.x, (float) c4.y, (float) c4.z, -1, texture, u2, v1, side)), tintIndex, side, texture, false, DefaultVertexFormats.ITEM);
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

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v, EnumFacing side)
	{
		int normal;

		int xN = ((byte) (side.getFrontOffsetX() * 127)) & 0xFF;
		int yN = ((byte) (side.getFrontOffsetY() * 127)) & 0xFF;
		int zN = ((byte) (side.getFrontOffsetZ() * 127)) & 0xFF;

		normal = xN | (yN << 0x08) | (zN << 0x10);

		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color, Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), normal };
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
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
		return interfaceBase;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}

	private class CacheEntry
	{
		HashMap<EnumFacing, EnumFacing> overrideData;
		EnumFacing facing;

		public CacheEntry(HashMap<EnumFacing, EnumFacing> overrideData, EnumFacing facing)
		{
			this.overrideData = overrideData;
			this.facing = facing;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((facing == null) ? 0 : facing.hashCode());
			result = prime * result + ((overrideData == null) ? 0 : overrideData.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheEntry other = (CacheEntry) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (facing != other.facing)
				return false;
			if (overrideData == null)
			{
				if (other.overrideData != null)
					return false;
			}
			else if (!overrideData.equals(other.overrideData))
				return false;
			return true;
		}

		private ModelInventoryRerouter getOuterType()
		{
			return ModelInventoryRerouter.this;
		}
	}
}
