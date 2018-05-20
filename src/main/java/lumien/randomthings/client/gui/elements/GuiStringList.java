package lumien.randomthings.client.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import lumien.randomthings.lib.IStringCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiStringList extends GuiScrollingList
{
	private List<String> stringList;
	private IStringCallback parent;

	public GuiStringList(IStringCallback parent, Minecraft client, int width, int height, int posX, int posY, int screenWidth, int screenHeight, ArrayList<String> stringList)
	{
		super(client, width, height, posY, posY + height, posX, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, screenWidth, screenHeight);

		this.stringList = stringList;
		this.parent = parent;
	}

	@Override
	protected int getSize()
	{
		return stringList.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{
		parent.pressed(stringList.get(index));
	}

	@Override
	protected boolean isSelected(int index)
	{
		return false;
	}

	@Override
	protected void drawBackground()
	{

	}

	@Override
	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5)
	{
		GlStateManager.disableLighting();
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int factor = scaledResolution.getScaleFactor();

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(left * factor, Minecraft.getMinecraft().displayHeight - top * factor - listHeight * factor, listWidth * factor, listHeight * factor);
		String string = stringList.get(var1);
		int color = 0xFFFFFF;

		Rectangle slotRect = new Rectangle(this.left + 3, var3, fontRenderer.getStringWidth(string), fontRenderer.FONT_HEIGHT);

		if (slotRect.contains(mouseX, mouseY))
		{
			color = 0xFFD700;
		}

		fontRenderer.drawString(string, this.left + 3, var3, color);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		GlStateManager.enableLighting();
	}

	public void setList(List<String> newList)
	{
		this.stringList = newList;
	}
}
