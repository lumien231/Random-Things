package lumien.randomthings.client.vfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.util.math.Vec3d;

public class VFXHandler
{
	public static VFXHandler INSTANCE = new VFXHandler();
	
	private List<VisualEffect> currentEffects;
	
	public VFXHandler()
	{
		this.currentEffects = new ArrayList<VisualEffect>();
	}
	
	public void addEffect(VisualEffect ve) {
		ve.init();
		this.currentEffects.add(ve);
	}
	
	public void tick() {
		Iterator<VisualEffect> iterator = this.currentEffects.iterator();
		
		while (iterator.hasNext()) {
			VisualEffect next = iterator.next();
			
			if (next.tick()) {
				iterator.remove();
			}
		}
	}
	
	public void render(float partialTicks) {
		ActiveRenderInfo ari = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();

		Vec3d pos = ari.getProjectedView();

		double playerX = pos.getX();
		double playerY = pos.getY();
		double playerZ = pos.getZ();

		GlStateManager.disableTexture();
		GlStateManager.translated(-playerX, -playerY, -playerZ);
		
		this.currentEffects.forEach(ve -> ve.renderInternal(partialTicks));

		// Undo
		GlStateManager.enableTexture();
		GlStateManager.translated(playerX, playerY, playerZ);
	}
}
