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

public class ImbueWither extends PotionBase
{
	public ImbueWither()
	{
		super("imbue_wither", false, Color.BLACK.brighter().getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/wither.png"));
		this.setPotionName("Wither Imbue");
	}
}
