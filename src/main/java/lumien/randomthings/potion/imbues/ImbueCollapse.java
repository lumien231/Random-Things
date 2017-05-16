package lumien.randomthings.potion.imbues;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class ImbueCollapse extends PotionBase
{
	public ImbueCollapse()
	{
		super("imbue_collapse", false, Color.PINK.getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/collapse.png"));
		this.setPotionName("Collapse Imbue");
	}
}
