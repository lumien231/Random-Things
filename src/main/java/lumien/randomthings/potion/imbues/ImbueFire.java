package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class ImbueFire extends PotionBase
{
	public ImbueFire()
	{
		super("imbue_fire", false, Color.ORANGE.getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/fire.png"));
		this.setPotionName("Fire Imbue");
	}
}
