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
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModelFluidDisplay implements IBakedModel
{
	HashMap<String, ModelCubeAll> modelCache;
	HashMap<String, ModelCubeAll> modelCacheFlowing;

	ModelCubeAll defaultModel;

	@AtlasSprite(resource = "randomthings:blocks/fluidDisplay")
	static TextureAtlasSprite defaultSprite;

	public ModelFluidDisplay()
	{
		modelCache = new HashMap<String, ModelCubeAll>();
		modelCacheFlowing = new HashMap<String, ModelCubeAll>();
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

		String fluidName = extendedState.getValue(BlockFluidDisplay.FLUID);
		if (fluidName != null)
		{
			boolean flowing = extendedState.getValue(BlockFluidDisplay.FLOWING);

			HashMap<String, ModelCubeAll> cache = flowing ? modelCacheFlowing : modelCache;

			if (cache.containsKey(fluidName))
			{
				model = cache.get(fluidName);
			}
			else
			{
				Fluid fluid = FluidRegistry.getFluid(fluidName);

				if (fluid != null)
				{
					TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();

					cache.put(fluidName, new ModelCubeAll(flowing ? textureMap.getAtlasSprite(fluid.getFlowing().toString()) : textureMap.getAtlasSprite(fluid.getStill().toString()), true));
					model = cache.get(fluidName);
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
}
