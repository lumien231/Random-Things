package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerImbuingStation;
import lumien.randomthings.tileentity.TileEntityImbuingStation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiImbuingStation extends GuiContainerBase
{
	TileEntityImbuingStation te;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/imbuingStation.png");

	int progressBubble;

	GuiButton filter;

	public GuiImbuingStation(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerImbuingStation(player, world, x, y, z));

		this.te = (TileEntityImbuingStation) world.getTileEntity(new BlockPos(x, y, z));

		this.xSize = 176;
		this.ySize = 208;

		this.progressBubble = 0;
	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		ContainerImbuingStation container = (ContainerImbuingStation) this.inventorySlots;
		int imbuingProgress = te.imbuingProgress;

		if (imbuingProgress > 0)
		{
			int progressArrow = (int) (22F / TileEntityImbuingStation.IMBUING_LENGTH * imbuingProgress) + 1;

			this.drawTexturedModalRect(x + 99, y + 54, 189, 13, progressArrow, 16);

			if (container.getSlot(0).getHasStack())
			{
				this.drawTexturedModalRect(x + 82, y + 28, 176, 0, 12, progressBubble);
			}
			if (container.getSlot(1).getHasStack())
			{
				this.drawTexturedModalRect(x + 54, y + 56, 189, 0, progressBubble, 12);
			}
			if (container.getSlot(2).getHasStack())
			{
				this.drawTexturedModalRect(x + 82, y + 72, 176, 25, 12, 24);
				this.drawTexturedModalRect(x + 82, y + 72, 176, 50, 12, 24 - progressBubble);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		fontRenderer.drawString(I18n.format("tile.imbuingStation.name", new Object[0]), 3, 6, 4210752);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (te.imbuingProgress > 0)
		{
			progressBubble += 2;

			if (progressBubble > 24)
			{
				progressBubble = 0;
			}
		}
		else
		{
			this.progressBubble = 0;
		}
	}

}
