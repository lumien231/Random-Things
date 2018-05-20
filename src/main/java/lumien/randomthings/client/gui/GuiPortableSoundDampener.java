package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerPortableSoundDampener;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiPortableSoundDampener extends GuiContainerBase
{
	ItemStack dampenerStack;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/portableSoundDampener.png");

	public GuiPortableSoundDampener(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerPortableSoundDampener(player, world, x, y, z));

		dampenerStack = player.inventory.getCurrentItem();

		this.xSize = 176;
		this.ySize = 133;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("item.portableSoundDampener.name", new Object[0]), 8, 6, 4210752);
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
	}
}
