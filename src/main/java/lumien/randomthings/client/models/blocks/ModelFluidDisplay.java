package lumien.randomthings.client.models.blocks;

import java.util.HashMap;
import java.util.List;

import lumien.randomthings.block.BlockFluidDisplay;
import lumien.randomthings.lib.AtlasSprite;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModelFluidDisplay implements IBakedModel
{
	HashMap<CacheEntry, ModelCubeAll> modelCache;
	HashMap<CacheEntry, ModelCubeAll> modelCacheFlowing;

	ModelCubeAll defaultModel;

	@AtlasSprite(resource = "randomthings:blocks/fluidDisplay")
	static TextureAtlasSprite defaultSprite;

	public ModelFluidDisplay()
	{
		modelCache = new HashMap<>();
		modelCacheFlowing = new HashMap<>();
		defaultModel = new ModelCubeAll(defaultSprite, true);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if (state == null)
		{
			return defaultModel.getQuads(state, side, rand);
		}

		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		IBakedModel model = defaultModel;

		FluidStack fluidStack = extendedState.getValue(BlockFluidDisplay.FLUID);
		if (fluidStack != null)
		{
			boolean flowing = extendedState.getValue(BlockFluidDisplay.FLOWING);
			Rotation rotation = extendedState.getValue(BlockFluidDisplay.ROTATION);

			CacheEntry entry = new CacheEntry(fluidStack, rotation);
			HashMap<CacheEntry, ModelCubeAll> cache = flowing ? modelCacheFlowing : modelCache;

			if (cache.containsKey(entry))
			{
				model = cache.get(entry);
			}
			else
			{
				Fluid fluid = fluidStack.getFluid();

				if (fluid != null)
				{
					TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();

					cache.put(new CacheEntry(fluidStack, rotation), new ModelCubeAll(flowing ? textureMap.getAtlasSprite(fluid.getFlowing().toString()) : textureMap.getAtlasSprite(fluid.getStill().toString()), true, rotation));
					model = cache.get(entry);
				}
			}
		}

		return model.getQuads(extendedState, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return defaultModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return defaultModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return defaultModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return defaultSprite;
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return defaultModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}

	private class CacheEntry
	{
		FluidStack fluidStack;
		Rotation rotation;

		public CacheEntry(FluidStack fluidStack, Rotation rotation)
		{
			this.fluidStack = fluidStack;
			this.rotation = rotation;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((fluidStack == null) ? 0 : fluidStack.hashCode());
			result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
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
			if (fluidStack == null)
			{
				if (other.fluidStack != null)
					return false;
			}
			else if (!fluidStack.equals(other.fluidStack))
				return false;
			if (rotation != other.rotation)
				return false;
			return true;
		}

		private ModelFluidDisplay getOuterType()
		{
			return ModelFluidDisplay.this;
		}
	}
}
