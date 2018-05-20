package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiCustomButton;
import lumien.randomthings.container.ContainerEntityDetector;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageEntityDetector;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import lumien.randomthings.tileentity.TileEntityEntityDetector.FILTER;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiEntityDetector extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/entityDetector.png");

	GuiButton minusX;
	GuiButton plusX;

	GuiButton minusY;
	GuiButton plusY;

	GuiButton minusZ;
	GuiButton plusZ;

	GuiButton filter;

	GuiCustomButton invert;
	GuiCustomButton strongOutput;

	TileEntityEntityDetector entityDetector;
	TileEntityEntityDetector.FILTER displayedFilter;

	public GuiEntityDetector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEntityDetector(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 204;
		this.entityDetector = (TileEntityEntityDetector) world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		MessageEntityDetector message = new MessageEntityDetector(button.id, entityDetector.getPos());

		PacketHandler.INSTANCE.sendToServer(message);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		minusX = new GuiButtonExt(0, this.guiLeft + 15 + 24, this.guiTop + 25, 10, 10, "-");
		plusX = new GuiButtonExt(1, this.guiLeft + 15 + 90 + 14, this.guiTop + 25, 10, 10, "+");

		minusY = new GuiButtonExt(2, this.guiLeft + 15 + 24, this.guiTop + 45, 10, 10, "-");
		plusY = new GuiButtonExt(3, this.guiLeft + 15 + 90 + 14, this.guiTop + 45, 10, 10, "+");

		minusZ = new GuiButtonExt(4, this.guiLeft + 15 + 24, this.guiTop + 65, 10, 10, "-");
		plusZ = new GuiButtonExt(5, this.guiLeft + 15 + 90 + 14, this.guiTop + 65, 10, 10, "+");

		filter = new GuiButtonExt(6, this.guiLeft + 15 + 5, this.guiTop + 95, 70, 16, "");

		invert = new GuiCustomButton(this, 7, entityDetector.invert(), this.guiLeft + 15 + 77, this.guiTop + 93, 20, 20, "", background, 176, 0, 20, 20);
		invert.setToolTips("tooltip.entityDetector.normalOutput", "tooltip.entityDetector.invertedOutput");

		strongOutput = new GuiCustomButton(this, 8, entityDetector.strongOutput(), this.guiLeft + 15 + 100, this.guiTop + 93, 20, 20, "", background, 216, 0, 20, 20);
		strongOutput.setToolTips("tooltip.entityDetector.weakOutput", "tooltip.entityDetector.strongOutput");

		this.buttonList.add(minusX);
		this.buttonList.add(plusX);

		this.buttonList.add(minusY);
		this.buttonList.add(plusY);

		this.buttonList.add(minusZ);
		this.buttonList.add(plusZ);

		this.buttonList.add(filter);

		this.buttonList.add(strongOutput);
		this.buttonList.add(invert);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		if (this.entityDetector.getFilter() == FILTER.CUSTOM)
		{
			this.mc.renderEngine.bindTexture(background);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(138, 93, 176, 40, 20, 20);
		}

		for (GuiButton guibutton : this.buttonList)
		{
			if (guibutton.isMouseOver())
			{
				guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
				break;
			}
		}

		fontRenderer.drawString(I18n.format("tile.entityDetector.name", new Object[0]), 8 + 14, 6, 4210752);

		String radiusX = I18n.format("gui.entityDetector.radiusX", entityDetector.getRangeX());
		fontRenderer.drawString(radiusX, xSize / 2 - fontRenderer.getStringWidth(radiusX) / 2 - 3, 26, 4210752);

		String radiusY = I18n.format("gui.entityDetector.radiusY", entityDetector.getRangeY());
		fontRenderer.drawString(radiusY, xSize / 2 - fontRenderer.getStringWidth(radiusY) / 2 - 3, 46, 4210752);

		String radiusZ = I18n.format("gui.entityDetector.radiusZ", entityDetector.getRangeZ());
		fontRenderer.drawString(radiusZ, xSize / 2 - fontRenderer.getStringWidth(radiusZ) / 2 - 3, 66, 4210752);

		// this.fontRenderer.drawString(I18n.format("container.inventory",
		// new Object[0]), 8, this.ySize - 128 + 2, 4210752);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (filter.displayString.isEmpty() || displayedFilter != entityDetector.getFilter())
		{
			displayedFilter = entityDetector.getFilter();

			filter.displayString = I18n.format(displayedFilter.getLanguageKey());
		}

		if (invert.getValue() != entityDetector.invert())
		{
			invert.toggle();
		}

		if (strongOutput.getValue() != entityDetector.strongOutput())
		{
			strongOutput.toggle();
		}
	}
}
