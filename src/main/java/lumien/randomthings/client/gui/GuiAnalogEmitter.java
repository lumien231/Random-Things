package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerEmptyContainer;
import lumien.randomthings.lib.Colors;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageAnalogEmitter;
import lumien.randomthings.tileentity.TileEntityAnalogEmitter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiAnalogEmitter extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/analogEmitter.png");

	TileEntityAnalogEmitter analogEmitter;

	public GuiAnalogEmitter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEmptyContainer(player, world, x, y, z));

		this.xSize = 78;
		this.ySize = 50;
		analogEmitter = (TileEntityAnalogEmitter) world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiButton(543, guiLeft + 5, guiTop + ySize / 2 - 20 / 2 + 5, 20, 20, "<"));
		this.buttonList.add(new GuiButton(544, guiLeft + 60 - 5, guiTop + ySize / 2 - 20 / 2 + 5, 20, 20, ">"));
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
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		fontRenderer.drawString(I18n.format("tile.analogEmitter.name", new Object[0]), 4, 6, 4210752);

		if (analogEmitter != null)
		{
			int stringWidth = fontRenderer.getStringWidth(analogEmitter.emitLevel + "");
			fontRenderer.drawString(analogEmitter.emitLevel + "", xSize / 2 - stringWidth / 2 + 3, ySize / 2 - fontRenderer.FONT_HEIGHT / 2 + 5, Colors.RED_INT);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		int newLevel = analogEmitter.emitLevel;

		if (button.id == 543)
		{
			newLevel--;
		}
		else if (button.id == 544)
		{
			newLevel++;
		}

		if (newLevel > 0 && newLevel < 16 && newLevel != analogEmitter.emitLevel)
		{
			PacketHandler.INSTANCE.sendToServer(new MessageAnalogEmitter(analogEmitter.getPos(), newLevel));
		}
	}
}
