package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiStringList;
import lumien.randomthings.container.ContainerSoundRecorder;
import lumien.randomthings.item.ItemSoundRecorder;
import lumien.randomthings.lib.IStringCallback;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageSelectSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiSoundRecorder extends GuiContainerBase implements IStringCallback
{
	ItemStack soundRecorderStack;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/soundRecorder.png");

	GuiStringList recordedSounds;

	public GuiSoundRecorder(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerSoundRecorder(player, world, x, y, z));

		soundRecorderStack = player.inventory.getCurrentItem();
		this.xSize = 190;
		this.ySize = 186;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("item.soundRecorder.name", new Object[0]), 8, 6, 4210752);
		fontRenderer.drawString(I18n.format("container.inventory"), 15, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		recordedSounds.handleMouseInput(mouseX, mouseY);
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
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		recordedSounds.drawScreen(mouseX, mouseY, partialTicks);

		this.renderHoveredToolTip(mouseX, mouseY);
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

		recordedSounds = new GuiStringList(this, Minecraft.getMinecraft(), 180, 50, this.guiLeft + 5, this.guiTop + 17, width, height, ItemSoundRecorder.getRecordedSounds(soundRecorderStack));
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
	}

	@Override
	public void pressed(String string)
	{
		MessageSelectSound msg = new MessageSelectSound(string);

		PacketHandler.INSTANCE.sendToServer(msg);
	}
}
