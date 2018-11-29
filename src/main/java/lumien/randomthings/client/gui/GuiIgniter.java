package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiBoolButton;
import lumien.randomthings.client.gui.elements.GuiEnumButton;
import lumien.randomthings.client.gui.elements.GuiEnumStringButton;
import lumien.randomthings.container.ContainerBlockDestabilizer;
import lumien.randomthings.container.ContainerIgniter;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import lumien.randomthings.tileentity.TileEntityBlockDestabilizer;
import lumien.randomthings.tileentity.TileEntityIgniter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiIgniter extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/igniter.png");
	final TileEntity te;

	public GuiIgniter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerIgniter(player, world, x, y, z));

		this.xSize = 78;
		this.ySize = 50;
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		GuiEnumStringButton<TileEntityIgniter.MODE> modeButton = new GuiEnumStringButton<>(0, this.guiLeft + 5, this.guiTop + 4, 68, 20, TileEntityIgniter.class, "mode", te, new String[] {"gui.igniter.toggle","gui.igniter.ignite","gui.igniter.keepIgnited"});
		this.buttonList.add(modeButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button.id == 0)
		{
			MessageContainerSignal message = new MessageContainerSignal(2);
			PacketHandler.INSTANCE.sendToServer(message);
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
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		// fontRenderer.drawString(I18n.format("tile.blockDestabilizer.name", new
		// Object[0]), 8, 6, 4210752);

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
