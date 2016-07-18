package lumien.randomthings.client.gui.elements;

import java.lang.reflect.Field;
import java.util.Arrays;

import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiBoolButton extends GuiButton
{
	Field field;
	ResourceLocation textureLocation;
	Object instance;

	String[] tooltips;

	public GuiBoolButton(int buttonId, int x, int y, int widthIn, int heightIn, ResourceLocation textureLocation, Class clazz, String fieldName, Object instance)
	{
		super(buttonId, x, y, widthIn, heightIn, "");

		try
		{
			field = clazz.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}

		field.setAccessible(true);

		this.instance = instance;
		this.textureLocation = textureLocation;
	}

	public void setTooltips(String[] tooltips)
	{
		this.tooltips = tooltips;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if (super.mousePressed(mc, mouseX, mouseY))
		{
			MessageContainerSignal message = new MessageContainerSignal(this.id);
			PacketHandler.INSTANCE.sendToServer(message);

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRendererObj;
			mc.getTextureManager().bindTexture(textureLocation);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			boolean value = getValue();

			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, (value?1:0) * width, (i - 1) * height, this.width, this.height);
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

			this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
		}
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		if (this.tooltips != null)
		{
			try
			{
				String toolTip = this.tooltips[field.getBoolean(instance)?1:0];

				Minecraft mc = Minecraft.getMinecraft();
				FontRenderer fontrenderer = mc.fontRendererObj;

				toolTip = I18n.format(toolTip);
				GuiUtils.drawHoveringText(Arrays.<String> asList(new String[] { toolTip }), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, fontrenderer);
			}
			catch (Exception e)
			{

			}
		}
	}

	private boolean getValue()
	{
		try
		{
			return field.getBoolean(instance);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return false;
	}
}
