package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerEmptyContainer;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageOnlineDetector;
import lumien.randomthings.tileentity.TileEntityOnlineDetector;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiOnlineDetector extends GuiContainerBase
{
	TileEntityOnlineDetector te;

	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/onlineDetector.png");

	GuiTextField usernameInput;

	public GuiOnlineDetector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEmptyContainer(player, world, x, y, z));

		this.te = (TileEntityOnlineDetector) world.getTileEntity(new BlockPos(x, y, z));
		this.xSize = 136;
		this.ySize = 52;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		usernameInput = new GuiTextField(0, this.fontRenderer, (width / 2 - xSize / 2) + 5, (height / 2), 127, 20);
		usernameInput.setFocused(false);
		usernameInput.setCanLoseFocus(true);
		usernameInput.setText(te.getPlayerName());
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1, par2, par3);
		this.usernameInput.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int pressedKey)
	{
		if (pressedKey == Keyboard.KEY_ESCAPE || (!this.usernameInput.isFocused() && pressedKey == this.mc.gameSettings.keyBindInventory.getKeyCode()))
		{
			this.mc.player.closeScreen();
		}

		if (this.usernameInput.textboxKeyTyped(par1, pressedKey))
		{
			MessageOnlineDetector packet = new MessageOnlineDetector(usernameInput.getText(), te.getPos());
			PacketHandler.INSTANCE.sendToServer(packet);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		usernameInput.drawTextBox();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		fontRenderer.drawString(I18n.format("tile.onlineDetector.name", new Object[0]), 8, 6, 4210752);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.usernameInput.updateCursorCounter();
	}

}
