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

public class ImbuePoison extends PotionBase
{
	public ImbuePoison()
	{
		super("imbue_poison", false, Color.GREEN.darker().getRGB());

		this.setIcon(new ResourceLocation("randomthings:textures/gui/imbues/poison.png"));
		this.setPotionName("Poison Imbue");
	}
}
