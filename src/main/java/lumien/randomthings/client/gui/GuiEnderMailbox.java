package lumien.randomthings.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerEnderMailbox;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiEnderMailbox extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/enderMailbox.png");

	public GuiEnderMailbox(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEnderMailbox(player, world, x, y, z));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("tile.enderMailbox.name", new Object[0]), 8, 6, 4210752);
		this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 128 + 2, 4210752);
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

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
}
