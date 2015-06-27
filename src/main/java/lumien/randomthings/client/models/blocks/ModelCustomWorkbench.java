package lumien.randomthings.client.models.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lumien.randomthings.block.BlockCustomWorkbench;
import lumien.randomthings.lib.AtlasSprite;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelCustomWorkbench implements ISmartBlockModel, ISmartItemModel
{

	@Override
	public List getFaceQuads(EnumFacing p_177551_1_)
	{
		return new ArrayList();
	}

	@Override
	public List getGeneralQuads()
	{
		return new ArrayList();
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
	public TextureAtlasSprite getTexture()
	{
		return null;
	}

	@Override
	@Deprecated
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack)
	{
		return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(Blocks.planks.getDefaultState());
	}

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_front")
	static TextureAtlasSprite overlayFront;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_side")
	static TextureAtlasSprite overlaySide;

	@AtlasSprite(resource = "randomthings:blocks/workbench/crafting_top")
	static TextureAtlasSprite overlayTop;

	HashMap<EnumFacing, TextureAtlasSprite> overlays;

	@Override
	public IBakedModel handleBlockState(IBlockState state)
	{
		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		IBlockState woodState = extendedState.getValue(BlockCustomWorkbench.WOOD_STATE);

		if (overlays == null)
		{
			overlays = new HashMap<EnumFacing, TextureAtlasSprite>();
			
			overlays.put(EnumFacing.UP, overlayTop);
			
			overlays.put(EnumFacing.NORTH, overlayFront);
			overlays.put(EnumFacing.EAST, overlaySide);
			overlays.put(EnumFacing.WEST, overlayFront);
			overlays.put(EnumFacing.SOUTH, overlaySide);
		}

		return new ModelCubeOverlay(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(Blocks.planks.getDefaultState()), overlays, true);
	}
}
