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

	public GuiCustomButton(GuiScreen parent, int buttonId, boolean value, int x, int y, int widthIn, int heightIn, String buttonText, ResourceLocation buttonTextures, int uX, int uY)
	{
		super(buttonId, x, y, widthIn, heightIn, buttonText);

		this.parent = parent;
		this.buttonTextures = buttonTextures;
		this.uX = uX;
		this.uY = uY;
		this.value = value;

		this.tooltips = Pair.of(null, null);
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
	
	public GuiCustomButton setToolTips(String falseTooltip,String trueTooltip)
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
