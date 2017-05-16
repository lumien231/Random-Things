package lumien.randomthings.potion.effects;

import java.awt.Color;

import lumien.randomthings.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
