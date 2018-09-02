package lumien.randomthings.asm.confirmer;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.client.model.pipeline.VertexLighterFlat;

public class ClientConfirmer extends ServerConfirmer
{
	@Override
	public void confirm()
	{
		super.confirm();

		fun(BlockRendererDispatcher.class);
		fun(RenderLivingBase.class);
		fun(RenderItem.class);
		fun(LayerArmorBase.class);
		fun(EntityRenderer.class);
		fun(VertexLighterFlat.class);
		fun(PlayerControllerMP.class);
	}
}
