package lumien.randomthings.client.gui.elements;

import lumien.randomthings.handler.chunkanalyzer.ChunkAnalyzerResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiScanResultList extends GuiScrollingList
{
	ChunkAnalyzerResult results;

	public GuiScanResultList(Minecraft client, int width, int height, int top, int bottom, int left, int screenWidth, int screenHeight)
	{
		super(client, width, height, top, bottom, left, 20, screenWidth, screenHeight);
	}

	public void setResults(ChunkAnalyzerResult results)
	{
		this.results = results;
	}

	@Override
	protected int getSize()
	{
		return results != null ? results.blockCounts.size() : 0;
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick)
	{

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
	protected void drawSlot(int id, int right, int top, int height, Tessellator tess)
	{
		if (results != null && results.blockCounts.size() > id)
		{
			int count = results.blockCounts.get(id);
			String description = results.blockDescriptions.get(id);
			ItemStack stack = results.displayStacks.get(id);

			Minecraft mc = Minecraft.getMinecraft();
			RenderItem ir = mc.getRenderItem();

			RenderHelper.enableGUIStandardItemLighting();

			ir.renderItemAndEffectIntoGUI(mc.player, stack, this.left + 2, top + 2);

			FontRenderer fr = mc.fontRenderer;

			fr.drawString(count + "x ", this.left + 2 + 16 + 4, top + 2 + 4, 16777215);

			if (description.length() > 20)
			{
				description = description.substring(0, 19)+"...";
			}

			fr.drawString(description, this.left + 2 + 16 + 4 + 50, top + 2 + 4, 16777215);
		}
	}

}
