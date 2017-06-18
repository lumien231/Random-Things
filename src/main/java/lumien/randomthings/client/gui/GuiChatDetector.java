package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiCustomButton;
import lumien.randomthings.container.ContainerEmptyContainer;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageChatDetector;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiChatDetector extends GuiContainerBase
{
	TileEntityChatDetector te;

	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/chatDetector.png");

	GuiTextField chatMessageInput;

	GuiCustomButton consumeButton;

	public GuiChatDetector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEmptyContainer(player, world, x, y, z));

		this.te = (TileEntityChatDetector) world.getTileEntity(new BlockPos(x, y, z));
		this.xSize = 136;
		this.ySize = 54;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		chatMessageInput = new GuiTextField(0, this.fontRenderer, (width / 2 - xSize / 2) + 5, (height / 2), 127, 20);
		chatMessageInput.setFocused(false);
		chatMessageInput.setCanLoseFocus(true);
		chatMessageInput.setText(te.getChatMessage());

		consumeButton = new GuiCustomButton(this, 0, te.consume(), guiLeft + 112, guiTop + 5, 20, 20, "", background, 136, 0);
		this.buttonList.add(consumeButton);
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
		this.chatMessageInput.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int pressedKey)
	{
		if (pressedKey == Keyboard.KEY_ESCAPE || (!this.chatMessageInput.isFocused() && pressedKey == this.mc.gameSettings.keyBindInventory.getKeyCode()))
		{
			this.mc.player.closeScreen();
		}

		if (this.chatMessageInput.textboxKeyTyped(par1, pressedKey))
		{
			updateTE();
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
		chatMessageInput.drawTextBox();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		fontRenderer.drawString(I18n.format("tile.chatDetector.name", new Object[0]), 8, 6, 4210752);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		this.chatMessageInput.updateCursorCounter();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button == consumeButton)
		{
			consumeButton.toggle();
			updateTE();
		}
	}

	private void updateTE()
	{
		MessageChatDetector packet = new MessageChatDetector(chatMessageInput.getText(), consumeButton.getValue(), te.getPos());
		PacketHandler.INSTANCE.sendToServer(packet);
	}
}
