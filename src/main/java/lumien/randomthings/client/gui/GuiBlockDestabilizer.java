package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiBoolButton;
import lumien.randomthings.container.ContainerBlockDestabilizer;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageContainerSignal;
import lumien.randomthings.tileentity.TileEntityBlockDestabilizer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiBlockDestabilizer extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/blockDestabilizer.png");
	final TileEntity te;

	public GuiBlockDestabilizer(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerBlockDestabilizer(player, world, x, y, z));

		this.xSize = 85;
		this.ySize = 35;
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		GuiBoolButton lazyButton = new GuiBoolButton(0, this.guiLeft + 7, this.guiTop + 7, 20, 20, new ResourceLocation("randomthings", "textures/gui/blockDestabilizer/lazy.png"), TileEntityBlockDestabilizer.class, "lazy", te);
		this.buttonList.add(lazyButton);

		GuiBoolButton fuzzyButton = new GuiBoolButton(1, this.guiLeft + 33, this.guiTop + 7, 20, 20, new ResourceLocation("randomthings", "textures/gui/blockDestabilizer/fuzzy.png"), TileEntityBlockDestabilizer.class, "fuzzy", te);
		this.buttonList.add(fuzzyButton);

		GuiButton resetButton = new GuiButtonImage(2, this.guiLeft + 58, this.guiTop + 7, 20, 20, 85, 0, 20, background);
		this.buttonList.add(resetButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button.id == 2)
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
