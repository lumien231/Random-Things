package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerAdvancedItemCollector;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageAdvancedItemCollector;
import lumien.randomthings.tileentity.TileEntityAdvancedItemCollector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiAdvancedItemCollector extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/advancedItemCollector.png");

	GuiButton minusX;
	GuiButton plusX;

	GuiButton minusY;
	GuiButton plusY;

	GuiButton minusZ;
	GuiButton plusZ;

	TileEntityAdvancedItemCollector advancedItemCollector;

	public GuiAdvancedItemCollector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerAdvancedItemCollector(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 235;
		this.advancedItemCollector = (TileEntityAdvancedItemCollector) world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		MessageAdvancedItemCollector message = new MessageAdvancedItemCollector(button.id, advancedItemCollector.getPos());

		PacketHandler.INSTANCE.sendToServer(message);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		minusX = new GuiButton(0, this.guiLeft + 15 + 14, this.guiTop + 20, 20, 20, "-");
		plusX = new GuiButton(1, this.guiLeft + 15 + 90 + 14, this.guiTop + 20, 20, 20, "+");

		minusY = new GuiButton(2, this.guiLeft + 15 + 14, this.guiTop + 45, 20, 20, "-");
		plusY = new GuiButton(3, this.guiLeft + 15 + 90 + 14, this.guiTop + 45, 20, 20, "+");

		minusZ = new GuiButton(4, this.guiLeft + 15 + 14, this.guiTop + 70, 20, 20, "-");
		plusZ = new GuiButton(5, this.guiLeft + 15 + 90 + 14, this.guiTop + 70, 20, 20, "+");

		this.buttonList.add(minusX);
		this.buttonList.add(plusX);

		this.buttonList.add(minusY);
		this.buttonList.add(plusY);

		this.buttonList.add(minusZ);
		this.buttonList.add(plusZ);
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

		fontRendererObj.drawString(I18n.format("tile.advancedItemCollector.name", new Object[0]), 8 + 14, 6, 4210752);

		String radiusX = I18n.format("gui.entityDetector.radiusX", advancedItemCollector.getRangeX());
		fontRendererObj.drawString(radiusX, xSize / 2 - fontRendererObj.getStringWidth(radiusX) / 2 - 3, 26, 4210752);

		String radiusY = I18n.format("gui.entityDetector.radiusY", advancedItemCollector.getRangeY());
		fontRendererObj.drawString(radiusY, xSize / 2 - fontRendererObj.getStringWidth(radiusY) / 2 - 3, 51, 4210752);

		String radiusZ = I18n.format("gui.entityDetector.radiusZ", advancedItemCollector.getRangeZ());
		fontRendererObj.drawString(radiusZ, xSize / 2 - fontRendererObj.getStringWidth(radiusZ) / 2 - 3, 76, 4210752);

		ItemStack filter;
		if ((filter = this.advancedItemCollector.getInventory().getStackInSlot(0)) != null)
		{
			this.mc.renderEngine.bindTexture(background);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(75, 120, 176, 0, 20, 20);
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}
}
