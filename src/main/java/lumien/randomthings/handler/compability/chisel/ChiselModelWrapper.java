package lumien.randomthings.handler.compability.chisel;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChiselModelWrapper implements IBakedModel
{
	ISmartBlockModel wrappedModel;

	public ChiselModelWrapper(ISmartBlockModel model)
	{
		this.wrappedModel = model;
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_)
	{
		return wrappedModel.getFaceQuads(p_177551_1_);
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		return wrappedModel.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return wrappedModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return wrappedModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return wrappedModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return wrappedModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return wrappedModel.getItemCameraTransforms();
	}

}
