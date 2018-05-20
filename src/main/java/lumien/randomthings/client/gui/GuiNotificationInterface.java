package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerNotificationInterface;
import lumien.randomthings.container.ContainerTE;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageNotificationInterface;
import lumien.randomthings.tileentity.TileEntityNotificationInterface;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiNotificationInterface extends GuiContainerBase implements GuiResponder
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/notificationInterface.png");

	GuiTextField titleField;
	GuiTextField descriptionField;

	public GuiNotificationInterface(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerNotificationInterface(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 146;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("tile.notificationInterface.name", new Object[0]), 8, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		this.mc.renderEngine.bindTexture(background);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		titleField.drawTextBox();
		descriptionField.drawTextBox();
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		titleField.updateCursorCounter();
		descriptionField.updateCursorCounter();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		Keyboard.enableRepeatEvents(true);

		titleField = new GuiTextField(0, fontRenderer, this.guiLeft + 34, this.guiTop + 18, 130, 15);
		descriptionField = new GuiTextField(1, fontRenderer, this.guiLeft + 34, this.guiTop + 18 + 22, 130, 15);

		titleField.setGuiResponder(this);
		descriptionField.setGuiResponder(this);

		TileEntityNotificationInterface te = (TileEntityNotificationInterface) ((ContainerTE) this.inventorySlots).getTE();
		titleField.setText(te.getTitle());
		descriptionField.setText(te.getDescription());
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		titleField.mouseClicked(mouseX, mouseY, mouseButton);
		descriptionField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode)
	{
		if (keyCode == Keyboard.KEY_ESCAPE || (!(this.titleField.isFocused() || this.descriptionField.isFocused()) && keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()))
		{
			this.mc.player.closeScreen();
		}

		if (titleField.isFocused())
		{
			this.titleField.textboxKeyTyped(typedChar, keyCode);
		}

		if (descriptionField.isFocused())
		{
			this.descriptionField.textboxKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void setEntryValue(int id, boolean value)
	{
	}

	@Override
	public void setEntryValue(int id, float value)
	{
	}

	@Override
	public void setEntryValue(int id, String value)
	{
		String newTitle = titleField.getText();
		String newDescription = descriptionField.getText();

		TileEntityNotificationInterface te = (TileEntityNotificationInterface) ((ContainerTE) this.inventorySlots).getTE();

		MessageNotificationInterface message = new MessageNotificationInterface(newTitle, newDescription, te.getPos());
		PacketHandler.INSTANCE.sendToServer(message);
	}
}
