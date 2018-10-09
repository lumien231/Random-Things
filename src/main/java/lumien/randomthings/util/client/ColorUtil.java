package lumien.randomthings.util.client;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;

public class ColorUtil
{

	public static Color brighter(Color c1, float amount)
	{
		return new Color((int) Math.min(255, c1.getRed() + amount), (int) Math.min(255, c1.getGreen() + amount), (int) Math.min(255, c1.getBlue() + amount), c1.getAlpha());
	}

	public static void applyColor(Color c)
	{
		GlStateManager.color(1F / 255 * c.getRed(), 1F / 255 * c.getGreen(), 1F / 255 * c.getBlue(), 1F / 255 * c.getAlpha());
	}
}
