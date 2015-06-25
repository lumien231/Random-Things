package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.config.PotionIds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImbueExperience extends Potion
{
	ResourceLocation icon = new ResourceLocation("randomthings:textures/gui/imbues/experience.png");

	public ImbueExperience()
	{
		super(PotionIds.IMBUE_EXPERIENCE, new ResourceLocation("imbue_experience"), false, Color.YELLOW.getRGB());

		this.setPotionName("Experience Imbue");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		super.renderInventoryEffect(x, y, effect, mc);

		mc.renderEngine.bindTexture(icon);

		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}
}
