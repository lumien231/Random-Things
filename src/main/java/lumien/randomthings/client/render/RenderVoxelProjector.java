package lumien.randomthings.client.render;

import lumien.randomthings.handler.magicavoxel.ClientModelLibrary;
import lumien.randomthings.handler.magicavoxel.MagicaVoxelModel;
import lumien.randomthings.tileentity.TileEntityVoxelProjector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVoxelProjector extends TileEntitySpecialRenderer<TileEntityVoxelProjector>
{
	public RenderVoxelProjector()
	{

	}

	public void func_180538_a(TileEntityVoxelProjector voxelProjector, double p_180538_2_, double p_180538_4_, double p_180538_6_, float p_180538_8_, int p_180538_9_)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.translate((float) p_180538_2_, (float) p_180538_4_ + 2.0F, (float) p_180538_6_);

		MagicaVoxelModel model = ClientModelLibrary.getInstance().getModel(voxelProjector.getModel());
		if (model != null)
		{
			GlStateManager.translate(0.5, 0, 0.5);
			int scale = voxelProjector.getScale();
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(voxelProjector.getRenderModelRotation(p_180538_8_), 0, 1, 0);
			GlStateManager.translate(-model.getSizeX() * 1F / 20F / 2f, 0, -model.getSizeZ() * 1F / 20F / 2f);

			model.getRenderModel(voxelProjector.randomize()).draw(voxelProjector.ambientLight());

			GlStateManager.scale(-scale, -scale, -scale);
		}

		GlStateManager.disableRescaleNormal();

		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (p_180538_9_ >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

	@Override
	public void renderTileEntityAt(TileEntityVoxelProjector te, double x, double y, double z, float p_180535_8_, int p_180535_9_)
	{
		this.func_180538_a(te, x, y, z, p_180535_8_, p_180535_9_);
	}

	@Override
	public boolean isGlobalRenderer(TileEntityVoxelProjector voxelProjector)
    {
        return true;
    }
}