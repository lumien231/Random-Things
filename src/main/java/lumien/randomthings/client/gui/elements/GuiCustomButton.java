package lumien.randomthings.client.gui.elements;

import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiCustomButton extends GuiButton
{
	GuiScreen parent;
	ResourceLocation buttonTextures;
	int uX;
	int uY;
	boolean value;

	Pair<String, String> tooltips;

	int textureWidth;
	int textureHeight;

	public GuiCustomButton(GuiScreen parent, int buttonId, boolean value, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation buttonTextures, int uX, int uY, int textureWidth, int textureHeight)
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);

		this.parent = parent;
		this.buttonTextures = buttonTextures;
		this.uX = uX;
		this.uY = uY;
		this.value = value;

		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		
		this.tooltips = Pair.of(null, null);
	}
	
	public GuiCustomButton(GuiScreen parent, int buttonId, boolean value, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation buttonTextures, int uX, int uY)
	{
		this(parent, buttonId, value, x, y, widthIn, heightIn, buttonText, buttonTextures, uX, uY, widthIn, heightIn);
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

			GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, uX + (value ? 20 : 0), uY + (k - 1) * 20, this.width, this.height, this.textureWidth, this.textureHeight, 2, 3, 2, 2, this.zLevel);
			// this.drawTexturedModalRect(this.xPosition, this.yPosition, uX +
			// (value ? 20 : 0), uY + (k - 1) * 20, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int color = 14737632;

			if (packedFGColour != 0)
			{
				color = packedFGColour;
			}
			else if (!this.enabled)
			{
				color = 10526880;
			}
			else if (this.hovered)
			{
				color = 16777120;
			}


			String buttonText = this.displayString;
			int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
			int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");

			if (strWidth > width - 6 && strWidth > ellipsisWidth)
				buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

			this.drawCenteredString(mc.fontRendererObj, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);
		}
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		String toolTip;
		if (this.value)
		{
			toolTip = tooltips.getRight();
		}
		else
		{
			toolTip = tooltips.getLeft();
		}

		if (toolTip != null)
		{
			toolTip = I18n.format(toolTip);
			GuiUtils.drawHoveringText(Arrays.<String> asList(new String[] { toolTip }), mouseX, mouseY, parent.mc.displayWidth, parent.mc.displayHeight, -1, parent.mc.fontRendererObj);
		}
	}

	public GuiCustomButton setToolTips(String falseTooltip, String trueTooltip)
	{
		tooltips = Pair.of(falseTooltip, trueTooltip);

		return this;
	}

	public boolean getValue()
	{
		return value;
	}

	public void setValue(boolean value)
	{
		this.value = value;
	}
}
