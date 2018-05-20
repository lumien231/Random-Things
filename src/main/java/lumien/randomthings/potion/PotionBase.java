package lumien.randomthings.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBase extends Potion
{
	private ResourceLocation icon;

	protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);

		this.setRegistryName(new ResourceLocation("randomthings:" + name));
		ForgeRegistries.POTIONS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
	{
		super.renderHUDEffect(x, y, effect, mc, alpha);

		mc.renderEngine.bindTexture(getIcon());

		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		super.renderInventoryEffect(x, y, effect, mc);

		mc.renderEngine.bindTexture(getIcon());

		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
	}

	public ResourceLocation getIcon()
	{
		return icon;
	}

	public void setIcon(ResourceLocation icon)
	{
		this.icon = icon;
	}
}
