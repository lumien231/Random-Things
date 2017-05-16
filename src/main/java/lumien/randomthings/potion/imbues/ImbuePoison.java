package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class ImbuePoison extends PotionBase
{
	public ImbuePoison()
	{
		super("imbue_poison", false, Color.GREEN.darker().getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/poison.png"));
		this.setPotionName("Poison Imbue");
	}
}
