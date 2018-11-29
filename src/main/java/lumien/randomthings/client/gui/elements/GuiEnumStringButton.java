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

public class GuiEnumStringButton<E extends Enum> extends GuiButton
{
	Field field;
	Object instance;

	String[] tooltips;
	String[] text;

	public GuiEnumStringButton(int buttonId, int x, int y, int widthIn, int heightIn, Class clazz, String fieldName, Object instance, String[] text)
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
		this.text = text;
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
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

			E value = getValue();
			int ordinal = value.ordinal();

			int i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
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

			this.drawCenteredString(fontrenderer, I18n.format(this.text[ordinal]), this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
		}
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY)
	{
		if (this.tooltips != null)
		{
			try
			{
				String toolTip = this.tooltips[((Enum) field.get(instance)).ordinal()];

				Minecraft mc = Minecraft.getMinecraft();
				FontRenderer fontrenderer = mc.fontRenderer;

				toolTip = I18n.format(toolTip);
				GuiUtils.drawHoveringText(Arrays.<String>asList(new String[] { toolTip }), mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, fontrenderer);
			}
			catch (Exception e)
			{

			}
		}
	}

	private E getValue()
	{
		try
		{
			return (E) field.get(instance);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
