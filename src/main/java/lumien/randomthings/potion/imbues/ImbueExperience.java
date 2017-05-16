package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImbueExperience extends PotionBase
{
	public ImbueExperience()
	{
		super("imbue_experience", false, Color.YELLOW.getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/experience.png"));
		this.setPotionName("Experience Imbue");
	}
}
