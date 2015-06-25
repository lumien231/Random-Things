package lumien.randomthings.client.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCustomButton extends GuiButton
{
	ResourceLocation buttonTextures;
	int uX;
	int uY;
	boolean value;

	public GuiCustomButton(int buttonId, boolean value, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation buttonTextures, int uX, int uY)
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);

		this.buttonTextures = buttonTextures;
		this.uX = uX;
		this.uY = uY;
		this.value = value;
	}

	public void toggle()
	{
		value = !value;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(buttonTextures);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int k = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, uX + (value ? 20 : 0), uY + (k - 1) * 20, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int l = 14737632;

			if (packedFGColour != 0)
			{
				l = packedFGColour;
			}
			else if (!this.enabled)
			{
				l = 10526880;
			}
			else if (this.hovered)
			{
				l = 16777120;
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
		}
	}

	public boolean getValue()
	{
		return value;
	}
}
