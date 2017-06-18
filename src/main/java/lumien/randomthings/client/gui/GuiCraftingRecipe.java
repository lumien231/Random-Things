package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerCraftingRecipe;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiCraftingRecipe extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/craftingRecipe.png");

	public GuiCraftingRecipe(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerCraftingRecipe(player, world, x, y, z));

		this.width = 176;
		this.height = 166;
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
		fontRenderer.drawString(I18n.format("item.craftingRecipe.name", new Object[0]), 8, 6, 4210752);
	}
}
