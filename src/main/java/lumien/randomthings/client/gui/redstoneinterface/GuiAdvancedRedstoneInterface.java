package lumien.randomthings.client.gui.redstoneinterface;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.redstoneinterface.ContainerAdvancedRedstoneInterface;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiAdvancedRedstoneInterface extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/redstoneinterface/advancedRedstoneInterface.png");

	public GuiAdvancedRedstoneInterface(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerAdvancedRedstoneInterface(player, world, x, y, z));
		
		xSize = 176;
		ySize = 133;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRendererObj.drawString(I18n.format("tile.advancedRedstoneInterface.name", new Object[0]), 8, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
