package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class ImbueExperience extends PotionBase
{
	public ImbueExperience()
	{
		super("imbue_experience", false, Color.YELLOW.getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/experience.png"));
		this.setPotionName("Experience Imbue");
	}
}
