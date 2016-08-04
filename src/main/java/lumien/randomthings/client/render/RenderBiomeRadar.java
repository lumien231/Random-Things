package lumien.randomthings.client.render;

import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.tileentity.TileEntityBiomeRadar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class RenderBiomeRadar extends TileEntitySpecialRenderer<TileEntityBiomeRadar>
{
	EntityItem entityItem;

	public RenderBiomeRadar()
	{
		entityItem = new EntityItem(null)
		{
			@Override
			public int getAge()
			{
				return RTEventHandler.clientAnimationCounter;
			}
		};
	}

	@Override
	public void renderTileEntityAt(TileEntityBiomeRadar te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		ItemStack currentCrystal;

		if ((currentCrystal = te.getCurrentCrystal()) != null)
		{
			entityItem.setEntityItemStack(currentCrystal);
			entityItem.setWorld(te.getWorld());
			entityItem.setPosition(x + 0.5, y - 0.1, z + 0.5);

			double pi2 = Math.PI / 2;

			entityItem.hoverStart = (float) (pi2 - (entityItem.getAge() + partialTicks) / 10.0F);

			Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, x + 0.5, y - 0.1, z + 0.5, 0, partialTicks, true);
		}
	}

}
