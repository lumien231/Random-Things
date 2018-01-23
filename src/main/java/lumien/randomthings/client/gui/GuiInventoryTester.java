package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiBoolButton;
import lumien.randomthings.container.ContainerInventoryTester;
import lumien.randomthings.tileentity.TileEntityInventoryTester;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiInventoryTester extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/inventoryTester.png");
	final TileEntity te;

	public GuiInventoryTester(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerInventoryTester(player, world, x, y, z));

		this.te = world.getTileEntity(new BlockPos(x, y, z));

		this.xSize = 176;
		this.ySize = 136;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		GuiBoolButton invertSignalButton = new GuiBoolButton(0, this.guiLeft + 93, this.guiTop + 16, 20, 20, new ResourceLocation("randomthings", "textures/gui/inventoryTester/invertSignal.png"), TileEntityInventoryTester.class, "invertSignal", te);
		invertSignalButton.setTooltips(new String[] { "tooltip.inventoryTester.invertSignal.no", "tooltip.inventoryTester.invertSignal.yes" });
		this.buttonList.add(invertSignalButton);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
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
		fontRenderer.drawString(I18n.format("tile.inventoryTester.name", new Object[0]), 33, 6, 4210752);

		for (GuiButton guibutton : this.buttonList)
		{
			if (guibutton.isMouseOver())
			{
				guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
				break;
			}
		}
	}

}
