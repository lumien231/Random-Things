package lumien.randomthings.potion.effects;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

public class EffectCollapse extends PotionBase
{
	public EffectCollapse()
	{
		super("collapse", false, Color.PINK.getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/effects/collapse.png"));
		this.setPotionName("Collapse");
	}

	@Override
	public boolean isBadEffect()
	{
		return true;
	}
}
