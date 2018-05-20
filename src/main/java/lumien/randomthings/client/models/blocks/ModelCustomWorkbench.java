package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lumien.randomthings.block.BlockCustomWorkbench;
import lumien.randomthings.client.RenderReference;
import lumien.randomthings.lib.AtlasSprite;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelCustomWorkbench implements IBakedModel
{
	ModelCubeOverlay defaultModel;

	HashMap<IBlockState, ModelCubeOverlay> modelCache;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_front")
	static TextureAtlasSprite overlayFront;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_side")
	static TextureAtlasSprite overlaySide;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_top")
	static TextureAtlasSprite overlayTop;

	HashMap<EnumFacing, TextureAtlasSprite> overlays;

	public ModelCustomWorkbench()
	{
		modelCache = new HashMap<>();

		overlays = new HashMap<>();

		overlays.put(EnumFacing.UP, overlayTop);

		overlays.put(EnumFacing.NORTH, overlayFront);
		overlays.put(EnumFacing.EAST, overlaySide);
		overlays.put(EnumFacing.WEST, overlayFront);
		overlays.put(EnumFacing.SOUTH, overlaySide);
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		checkDefault();

		IBakedModel model = defaultModel;

		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		IBlockState woodState = extendedState.getValue(BlockCustomWorkbench.WOOD_STATE);

		if (woodState != null)
		{
			if (modelCache.containsKey(woodState))
			{
				model = modelCache.get(woodState);
			}
			else
			{
				modelCache.put(woodState, new ModelCubeOverlay(RenderUtils.getQuadFaceMapFromState(woodState), overlays, Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.PLANKS.getDefaultState()).getParticleTexture(), true));
				model = modelCache.get(woodState);
			}
		}
		return model.getQuads(woodState, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		checkDefault();
		return defaultModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		checkDefault();
		return defaultModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		checkDefault();
		return defaultModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		checkDefault();
		return defaultModel.getParticleTexture();
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return RenderReference.BLOCK_ITEM_TRANSFORM;
	}

	private void checkDefault()
	{
		if (defaultModel == null)
		{
			defaultModel = new ModelCubeOverlay(RenderUtils.getQuadFaceMapFromState(Blocks.PLANKS.getDefaultState()), overlays, Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.PLANKS.getDefaultState()).getParticleTexture(), true);
		}
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return new ItemOverrideList(new ArrayList<ItemOverride>())
		{
			@Override
			public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
			{
				checkDefault();
				NBTTagCompound compound;

				String woodName;
				int meta;

				if ((compound = stack.getTagCompound()) != null)
				{
					woodName = compound.getString("woodName");
					meta = compound.getInteger("woodMeta");
				}
				else
				{
					woodName = "minecraft:planks";
					meta = 0;
				}

				Block woodBlock = Block.getBlockFromName(woodName);

				if (woodBlock == null)
				{
					woodBlock = Blocks.PLANKS;
					meta = 0;
				}

				IBlockState woodState = woodBlock.getStateFromMeta(meta);

				if (woodState == null)
				{
					return defaultModel;
				}

				if (modelCache.containsKey(woodState))
				{
					return modelCache.get(woodState);
				}
				else
				{
					modelCache.put(woodState, new ModelCubeOverlay(RenderUtils.getQuadFaceMapFromState(woodState), overlays, Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.PLANKS.getDefaultState()).getParticleTexture(), true));
					return modelCache.get(woodState);
				}
			}
		};
	}
}
