package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerFilteredRedirectorPlate;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiFilteredRedirectorPlate extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/filteredredirectorplate.png");

	public GuiFilteredRedirectorPlate(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerFilteredRedirectorPlate(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 129;
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

		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, 37, 4210752);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}
}
