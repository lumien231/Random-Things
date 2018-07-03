package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiEnumButton;
import lumien.randomthings.client.gui.elements.GuiFacingButton;
import lumien.randomthings.container.ContainerAdvancedRedstoneRepeater;
import lumien.randomthings.container.ContainerProcessingPlate;
import lumien.randomthings.container.ContainerTE;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneRepeater;
import lumien.randomthings.tileentity.TileEntityProcessingPlate;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiProcessingPlate extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/processingPlate.png");
	final TileEntity te;

	public GuiProcessingPlate(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerProcessingPlate(player, world, x, y, z));

		this.xSize = 120;
		this.ySize = 77;
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiFacingButton(0, this.guiLeft + 10, this.guiTop + 15, 100, 20, TileEntityProcessingPlate.class, "insertFacing", this.te));
		this.buttonList.add(new GuiFacingButton(1, this.guiLeft + 10, this.guiTop + 48, 100, 20, TileEntityProcessingPlate.class, "extractFacing", this.te));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		MessageContainerSignal message = new MessageContainerSignal(button.id);
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
		fontRenderer.drawString(I18n.format("Insertion Facing", new Object[0]), 7, 5, 0);
		fontRenderer.drawString(I18n.format("Extraction Facing", new Object[0]), 7, 38, 0);

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
