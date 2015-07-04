package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lumien.randomthings.block.BlockCustomWorkbench;
import lumien.randomthings.client.RenderReference;
import lumien.randomthings.lib.AtlasSprite;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelCustomWorkbench implements ISmartBlockModel, ISmartItemModel
{
	ModelCubeOverlay defaultModel;

	HashMap<IBlockState, ModelCubeOverlay> modelCache;
	HashMap<IBlockState, ModelCubeOverlay> itemModelCache;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_front")
	static TextureAtlasSprite overlayFront;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_side")
	static TextureAtlasSprite overlaySide;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_top")
	static TextureAtlasSprite overlayTop;

	HashMap<EnumFacing, TextureAtlasSprite> overlays;

	public ModelCustomWorkbench()
	{
		modelCache = new HashMap<IBlockState, ModelCubeOverlay>();
		itemModelCache = new HashMap<IBlockState, ModelCubeOverlay>();

		overlays = new HashMap<EnumFacing, TextureAtlasSprite>();

		overlays.put(EnumFacing.UP, overlayTop);

		overlays.put(EnumFacing.NORTH, overlayFront);
		overlays.put(EnumFacing.EAST, overlaySide);
		overlays.put(EnumFacing.WEST, overlayFront);
		overlays.put(EnumFacing.SOUTH, overlaySide);
	}

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_)
	{
		return defaultModel.getFaceQuads(p_177551_1_);
	}

	@Override
	public List getGeneralQuads()
	{
		return defaultModel.getGeneralQuads();
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
	public TextureAtlasSprite getTexture()
	{
		return defaultModel.getTexture();
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return RenderReference.BLOCK_ITEM_TRANSFORM;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
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
			woodBlock = Blocks.planks;
			meta = 0;
		}

		IBlockState woodState = woodBlock.getStateFromMeta(meta);

		if (woodState == null)
		{
			return defaultModel;
		}

		if (itemModelCache.containsKey(woodState))
		{
			return itemModelCache.get(woodState);
		}
		else
		{
			itemModelCache.put(woodState, new ModelCubeOverlay(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(woodState), overlays, true, true));
			return itemModelCache.get(woodState);
		}
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state)
	{
		if (defaultModel == null)
		{
			defaultModel = new ModelCubeOverlay(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(Blocks.planks.getDefaultState()), overlays, true, false);
		}

		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		IBlockState woodState = extendedState.getValue(BlockCustomWorkbench.WOOD_STATE);

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
			modelCache.put(woodState, new ModelCubeOverlay(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(woodState), overlays, true, false));
			return modelCache.get(woodState);
		}
	}
}
