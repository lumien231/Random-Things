package lumien.randomthings.client.gui.elements;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiSlotButton extends GuiButton
{
	ResourceLocation texture = new ResourceLocation("randomthings:textures/gui/slotButton.png");
	Minecraft mc;
	RenderItem itemRender;

	ItemStack stack;
	String tooltip;

	public GuiSlotButton(int buttonId, int x, int y, ItemStack stack, String tooltip)
	{
		super(buttonId, x, y, 18, 18, "");

		this.mc = Minecraft.getMinecraft();
		this.itemRender = Minecraft.getMinecraft().getRenderItem();

		this.stack = stack;
		this.tooltip = tooltip;
	}


	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		GlStateManager.disableLighting();
		FontRenderer fontrenderer = mc.fontRendererObj;
		mc.getTextureManager().bindTexture(texture);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
		int i = this.getHoverState(this.hovered);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0 + (i - 1) * 18, this.width, this.height);
		this.mouseDragged(mc, mouseX, mouseY);

		if (stack != null)
		{
			RenderHelper.enableGUIStandardItemLighting();

			this.zLevel = 100.0F;
			this.itemRender.zLevel = 100.0F;

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.thePlayer, stack, this.xPosition + 1, this.yPosition + 1);
			this.itemRender.renderItemOverlayIntoGUI(fontrenderer, stack, this.xPosition + 1, this.yPosition + 1, "");

			this.itemRender.zLevel = 0.0F;
			this.zLevel = 0.0F;

			RenderHelper.disableStandardItemLighting();
		}
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		if (tooltip != null)
		{
			GuiUtils.drawHoveringText(Arrays.<String> asList(new String[] { tooltip }), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRendererObj);
		}
	}
}
