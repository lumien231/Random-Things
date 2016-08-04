package lumien.randomthings.client.render;

import lumien.randomthings.entitys.EntitySpirit;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerSpiritGel implements LayerRenderer<EntitySpirit>
{
    private final RenderSpirit slimeRenderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerSpiritGel(RenderSpirit spiritRenderer)
    {
        this.slimeRenderer = spiritRenderer;
    }

    @Override
	public void doRenderLayer(EntitySpirit entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (!entitylivingbaseIn.isInvisible())
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
            this.slimeModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return true;
    }
}