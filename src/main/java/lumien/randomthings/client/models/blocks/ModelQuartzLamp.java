package lumien.randomthings.client.models.blocks;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Function;

import lumien.randomthings.client.RenderReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;

public class ModelQuartzLamp implements IFlexibleBakedModel
{
	ResourceLocation normal;
	ResourceLocation glowing;

	IFlexibleBakedModel normalModel;
	IFlexibleBakedModel glowingModel;

	// @AtlasSprite(resource = "randomthings:blocks/quartzLampNormal")
	static TextureAtlasSprite tex1;

	// @AtlasSprite(resource = "randomthings:blocks/quartzLampGlow")
	static TextureAtlasSprite tex2;

	public ModelQuartzLamp(ModelLoader modelLoader, ResourceLocation normal, ResourceLocation glowing)
	{
		Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
		{
			@Override
			public TextureAtlasSprite apply(ResourceLocation location)
			{
				return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
			}
		};

		IModel normalM = null;
		try
		{
			normalM = modelLoader.getModel(normal);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		normalModel = normalM.bake(normalM.getDefaultState(), this.getFormat(), textureGetter);

		IModel glowingM = null;
		try
		{
			glowingM = modelLoader.getModel(glowing);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		glowingModel = glowingM.bake(glowingM.getDefaultState(), this.getFormat(), textureGetter);
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_)
	{
		if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID)
		{
			return normalModel.getFaceQuads(p_177551_1_);
		}
		else
		{
			return glowingModel.getFaceQuads(p_177551_1_);
		}
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID)
		{
			return normalModel.getGeneralQuads();
		}
		else
		{
			return glowingModel.getGeneralQuads();
		}
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
		return tex2;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return RenderReference.BLOCK_ITEM_TRANSFORM;
	}

	@Override
	public VertexFormat getFormat()
	{
		return DefaultVertexFormats.BLOCK;
	}

}
