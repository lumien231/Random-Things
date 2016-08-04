package lumien.randomthings.client.gui.redstoneinterface;

import lumien.randomthings.container.ContainerEmptyContainer;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiBasicRedstoneInterface extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/redstoneinterface/basicRedstoneInterface.png");

	TileEntityBasicRedstoneInterface te;

	public GuiBasicRedstoneInterface(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerEmptyContainer(player, world, x, y, z));

		this.xSize = 136;
		this.ySize = 54;
		this.te = (TileEntityBasicRedstoneInterface) world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRendererObj.drawString(I18n.format("tile.basicRedstoneInterface.name", new Object[0]), 4, 6, 13107220);

		BlockPos target = te.getTarget();

		if (target == null)
		{
			String center = I18n.format("gui.basicRedstoneInterface.notarget");
			fontRendererObj.drawString(center, 136 / 2 - fontRendererObj.getStringWidth(center) / 2, 54 / 2 - fontRendererObj.FONT_HEIGHT / 2 + 3, 9830400);
		}
		else
		{
			fontRendererObj.drawString(I18n.format("gui.basicRedstoneInterface.targetX", target.getX()), 8, 18, 1310740);
			fontRendererObj.drawString(I18n.format("gui.basicRedstoneInterface.targetY", target.getY()), 8, 28, 1310740);
			fontRendererObj.drawString(I18n.format("gui.basicRedstoneInterface.targetZ", target.getZ()), 8, 38, 1310740);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
