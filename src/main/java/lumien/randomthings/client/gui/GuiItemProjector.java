package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiEnumButton;
import lumien.randomthings.container.ContainerItemProjector;
import lumien.randomthings.tileentity.TileEntityItemProjector;
import lumien.randomthings.tileentity.TileEntityItemProjector.SELECTION_MODE;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiItemProjector extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/itemProjector.png");
	final TileEntity te;

	public GuiItemProjector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerItemProjector(player, world, x, y, z));
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();

		GuiEnumButton<SELECTION_MODE> redstoneModeButton = new GuiEnumButton<SELECTION_MODE>(0, this.guiLeft + 125, this.guiTop + 16, 20, 20, new ResourceLocation("randomthings", "textures/gui/itemProjector/selectionMode.png"), TileEntityItemProjector.class, "selectionMode", te);
		redstoneModeButton.setTooltips(new String[] { "tooltip.itemProjector.selectionMode.first", "tooltip.itemProjector.selectionMode.last", "tooltip.itemProjector.selectionMode.biggest" });
		this.buttonList.add(redstoneModeButton);
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
		fontRendererObj.drawString(I18n.format("tile.itemProjector.name", new Object[0]), 8, 6, 4210752);

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
