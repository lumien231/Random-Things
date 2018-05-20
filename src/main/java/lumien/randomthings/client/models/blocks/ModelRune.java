package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.primitives.Ints;

import lumien.randomthings.block.BlockRuneBase;
import lumien.randomthings.config.Features;
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

public class ModelRune implements IBakedModel
{
	@AtlasSprite(resource = "randomthings:blocks/runeBase")
	public static TextureAtlasSprite runeBase;

	@AtlasSprite(resource = "randomthings:blocks/runeBaseFlat")
	public static TextureAtlasSprite runeBaseFlat;

	Cache<CacheEntry, List<BakedQuad>> modelCache;

	public ModelRune()
	{
		modelCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(120, TimeUnit.SECONDS).build();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (side == EnumFacing.UP && state instanceof IExtendedBlockState)
		{
			int[][] runeData = ((IExtendedBlockState) state).getValue(BlockRuneBase.RUNE_DATA);
			boolean[] connectionData = ((IExtendedBlockState) state).getValue(BlockRuneBase.CONNECTION_DATA);

			CacheEntry entry = new CacheEntry(runeData, connectionData);

			List<BakedQuad> quadList = modelCache.getIfPresent(entry);
			if (quadList != null)
			{
				return quadList;
			}
			else
			{
				Random rng = new Random(rand);
				quadList = new ArrayList<>();

				for (int x = 0; x < runeData.length; x++)
				{
					for (int z = 0; z < runeData[0].length; z++)
					{
						int rune = runeData[x][z];

						float u = rng.nextInt(8) * 2;
						float v = rng.nextInt(8) * 2;

						if (rune != -1)
						{
							quadList.add(generateNewRuneQuad(x * 4, z * 4, rune, 2, 2, u, v));

							boolean[] connectedSides = new boolean[4];

							connectedSides[0] = (z == 0 && connectionData[x]) || z != 0 && runeData[x][z - 1] == rune;
							connectedSides[2] = (z == 3 && connectionData[8 + x]) || z != 3 && runeData[x][z + 1] == rune;

							connectedSides[1] = (x == 3 && connectionData[4 + z]) || x != 3 && runeData[x + 1][z] == rune;
							connectedSides[3] = (x == 0 && connectionData[12 + z]) || x != 0 && runeData[x - 1][z] == rune;

							for (int i = 0; i < connectedSides.length; i++)
							{
								u = rng.nextInt(8) * 2;
								v = rng.nextInt(8) * 2;

								if (connectedSides[i])
								{
									int modX = i == 1 ? 2 : (i == 3 ? -1 : 0);
									int modZ = i == 0 ? -1 : (i == 2 ? 2 : 0);

									quadList.add(generateNewRuneQuad(x * 4 + modX, z * 4 + modZ, rune, modZ != 0 ? 2 : 1, modX != 0 ? 2 : 1, u, v));
								}
							}
						}
						else
						{
							for (int i = 0; i < 4; i++)
							{
								u = rng.nextInt(8) * 2;
								v = rng.nextInt(8) * 2;
							}
						}
					}
				}

				modelCache.put(new CacheEntry(runeData, connectionData), quadList);

				return quadList;
			}
		}
		else
		{
			return new ArrayList<>();
		}
	}

	private BakedQuad generateNewRuneQuad(int x, int z, int rune, int width, int height, float u, float v)
	{
		float part = 1F / 16F;

		return createSidedBakedQuad(part + x * (part), part + x * (part) + part * width, part + z * (part), part + z * (part) + part * height, 0.2F * part, u, v, u + width, v + height, rune, Features.FLAT_RUNES ? runeBaseFlat : runeBase, EnumFacing.UP);
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

		if (side != EnumFacing.UP && side != EnumFacing.SOUTH)
		{
			c1 = rotate(c1.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c2 = rotate(c2.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c3 = rotate(c3.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
			c4 = rotate(c4.addVector(-.5, -.5, -.5), rotation).addVector(.5, 0.5, .5);
		}

		return new BakedQuad(Ints.concat(vertexToInts((float) c1.x, (float) c1.y, (float) c1.z, -1, texture, u1, v1, side), vertexToInts((float) c2.x, (float) c2.y, (float) c2.z, -1, texture, u1, v2, side), vertexToInts((float) c3.x, (float) c3.y, (float) c3.z, -1, texture, u2, v2, side), vertexToInts((float) c4.x, (float) c4.y, (float) c4.z, -1, texture, u2, v1, side)), tintIndex, side, texture, false, DefaultVertexFormats.ITEM);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return Features.FLAT_RUNES ? runeBaseFlat : runeBase;
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

	private class CacheEntry
	{
		int[][] runeData;
		boolean[] connectionData;

		public CacheEntry(int[][] runeData, boolean[] connectionData)
		{
			this.runeData = runeData;
			this.connectionData = connectionData;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + java.util.Arrays.hashCode(connectionData);
			result = prime * result + java.util.Arrays.deepHashCode(runeData);
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
			if (!java.util.Arrays.equals(connectionData, other.connectionData))
				return false;
			if (!java.util.Arrays.deepEquals(runeData, other.runeData))
				return false;
			return true;
		}

		private ModelRune getOuterType()
		{
			return ModelRune.this;
		}

	}
}
