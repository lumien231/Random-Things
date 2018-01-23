package lumien.randomthings.client.notifications;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotificationToast implements IToast
{
	private final String title;
	private final String body;
	private final ItemStack icon;

	private long firstDrawTime;

	public NotificationToast(String title, String body, ItemStack icon)
	{
		this.title = title;
		this.body = body;
		this.icon = icon;
	}

	@Override
	public IToast.Visibility draw(GuiToast toastGui, long delta)
	{
		toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
		toastGui.getMinecraft().fontRenderer.drawString(title, 30, 7, -11534256);
		toastGui.getMinecraft().fontRenderer.drawString(body, 30, 18, -16777216);
		RenderHelper.enableGUIStandardItemLighting();
		toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null, icon, 8, 8);
		return delta - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
	}
}