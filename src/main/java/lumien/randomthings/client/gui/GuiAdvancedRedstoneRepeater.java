package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerAdvancedRedstoneRepeater;
import lumien.randomthings.container.ContainerTE;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneRepeater;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiAdvancedRedstoneRepeater extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/advancedRedstoneRepeater.png");
	final TileEntity te;

	public GuiAdvancedRedstoneRepeater(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerAdvancedRedstoneRepeater(player, world, x, y, z));

		this.xSize = 90;
		this.ySize = 56;
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiButtonExt(0, this.guiLeft + 5, this.guiTop + 15, 10, 10, "-"));
		this.buttonList.add(new GuiButtonExt(1, this.guiLeft + 5 + 70, this.guiTop + 15, 10, 10, "+"));

		this.buttonList.add(new GuiButtonExt(2, this.guiLeft + 5, this.guiTop + 39, 10, 10, "-"));
		this.buttonList.add(new GuiButtonExt(3, this.guiLeft + 5 + 70, this.guiTop + 39, 10, 10, "+"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		int mod = 0;

		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		
		if (shift && ctrl)
		{
			mod = 12;
		}
		else if (ctrl)
		{
			mod = 8;
		}
		else if (shift)
		{
			mod = 4;
		}

		MessageContainerSignal message = new MessageContainerSignal(button.id + mod);
		PacketHandler.INSTANCE.sendToServer(message);
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
		fontRenderer.drawString(I18n.format("Turn Off Delay", new Object[0]), 7, 5, 0);
		fontRenderer.drawString(I18n.format("Turn On Delay", new Object[0]), 9, 29, 0);

		String turnOnDelayString = ((ContainerTE<TileEntityAdvancedRedstoneRepeater>) inventorySlots).getTE().getTurnOnDelay() + "";
		fontRenderer.drawString(turnOnDelayString, xSize / 2 - fontRenderer.getStringWidth(turnOnDelayString) / 2, 40, 9830400);

		String turnOffDelayString = ((ContainerTE<TileEntityAdvancedRedstoneRepeater>) inventorySlots).getTE().getTurnOffDelay() + "";
		fontRenderer.drawString(turnOffDelayString, xSize / 2 - fontRenderer.getStringWidth(turnOffDelayString) / 2, 16, 9830400);

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
