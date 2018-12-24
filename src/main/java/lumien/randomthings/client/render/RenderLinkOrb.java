package lumien.randomthings.client.render;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.tileentity.TileEntityBiomeRadar;
import lumien.randomthings.tileentity.TileEntityLinkOrb;
import lumien.randomthings.util.client.MKRRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class RenderLinkOrb extends TileEntitySpecialRenderer<TileEntityLinkOrb>
{

	public RenderLinkOrb()
	{

	}

	@Override
	public void render(TileEntityLinkOrb te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		MKRRenderUtil.renderLinkOrb(te.getPos(), x + 0.5, y + 0.5, z + 0.5);
	}

}
