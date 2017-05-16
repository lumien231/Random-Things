package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class ImbueWither extends PotionBase
{
	public ImbueWither()
	{
		super("imbue_wither", false, Color.BLACK.brighter().getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/wither.png"));
		this.setPotionName("Wither Imbue");
	}
}
