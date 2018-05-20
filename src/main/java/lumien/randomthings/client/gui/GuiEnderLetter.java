package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerEnderLetter;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageEnderLetter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiEnderLetter extends GuiContainerBase
{
	ItemStack enderLetterStack;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/enderLetter.png");

	GuiTextField receiverName;

	String oldReceiver;

	boolean received;

	public GuiEnderLetter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEnderLetter(player, world, x, y, z));

		enderLetterStack = player.inventory.getCurrentItem();
		oldReceiver = "";

		if (enderLetterStack.hasTagCompound())
		{
			received = enderLetterStack.getTagCompound().getBoolean("received");
		}

		this.xSize = 176;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("item.enderLetter.name", new Object[0]), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		this.mc.renderEngine.bindTexture(background);
		// this.mc.renderEngine.bindTexture("/gui/demo_bg.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		receiverName.drawTextBox();
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (!oldReceiver.equals(receiverName.getText()))
		{
			oldReceiver = receiverName.getText();
			PacketHandler.INSTANCE.sendToServer(new MessageEnderLetter(oldReceiver));
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();

		receiverName = new GuiTextField(0, this.fontRenderer, (width - xSize) / 2 + 92, (height - ySize) / 2 + 5, 76, 10);
		receiverName.setFocused(false);
		receiverName.setCanLoseFocus(true);
		receiverName.setEnabled(!received);
		Keyboard.enableRepeatEvents(true);

		if (enderLetterStack.hasTagCompound() && enderLetterStack.getTagCompound().hasKey("receiver"))
		{
			this.oldReceiver = enderLetterStack.getTagCompound().getString("receiver");
		}

		receiverName.setText(this.oldReceiver);
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
		this.receiverName.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int pressedKey)
	{
		if (pressedKey == Keyboard.KEY_ESCAPE || (!this.receiverName.isFocused() && pressedKey == this.mc.gameSettings.keyBindInventory.getKeyCode()))
		{
			this.mc.player.closeScreen();
		}

		if (receiverName.isFocused())
		{
			this.receiverName.textboxKeyTyped(par1, pressedKey);
		}
	}
}
