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

public class GuiSoundRecorder extends GuiContainerBase
{
	ItemStack soundRecorderStack;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/enderLetter.png");

	public GuiSoundRecorder(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEnderLetter(player, world, x, y, z));
		
		soundRecorderStack = player.inventory.getCurrentItem();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("item.enderLetter.name", new Object[0]), 8, 6, 4210752);
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

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1, par2, par3);
	}
}
