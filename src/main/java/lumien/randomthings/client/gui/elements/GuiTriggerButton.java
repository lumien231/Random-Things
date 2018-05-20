package lumien.randomthings.client.gui.elements;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiTriggerButton extends GuiButton
{
	ResourceLocation textureLocation;

	String tooltip;
	int shiftOffset;

	public GuiTriggerButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation textureLocation, int shiftOffset)
	{
		super(buttonId, x, y, widthIn, heightIn, "");

		this.textureLocation = textureLocation;
		this.shiftOffset = shiftOffset;
	}

	public void setTooltip(String tooltip)
	{
		this.tooltip = tooltip;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if (super.mousePressed(mc, mouseX, mouseY))
		{
			MessageContainerSignal message = new MessageContainerSignal(this.id + (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? shiftOffset : 0));
			PacketHandler.INSTANCE.sendToServer(message);

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(textureLocation);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 0, (i - 1) * height, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;

			if (packedFGColour != 0)
			{
				j = packedFGColour;
			}
			else if (!this.enabled)
			{
				j = 10526880;
			}
			else if (this.hovered)
			{
				j = 16777120;
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		if (this.tooltip != null)
		{
			try
			{
				Minecraft mc = Minecraft.getMinecraft();
				FontRenderer fontrenderer = mc.fontRenderer;

				String localizedToolTip = I18n.format(tooltip);
				GuiUtils.drawHoveringText(Arrays.<String>asList(new String[] { localizedToolTip }), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, fontrenderer);
			}
			catch (Exception e)
			{

			}
		}
	}
}
