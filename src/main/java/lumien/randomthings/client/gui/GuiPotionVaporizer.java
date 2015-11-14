package lumien.randomthings.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerEnderLetter;
import lumien.randomthings.container.ContainerPotionVaporizer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class GuiPotionVaporizer extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/potionVaporizer.png");

	public GuiPotionVaporizer(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerPotionVaporizer(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		ContainerPotionVaporizer container = (ContainerPotionVaporizer) inventorySlots;

		if (container.duration != 0)
		{
			int tankProgress = (int) Math.floor(14F - (14F / container.duration * container.durationLeft));

			Color c = new Color(container.color);

			float red = 1F / 255F * c.getRed();
			float green = 1F / 255F * c.getGreen();
			float blue = 1F / 255F * c.getBlue();

			GlStateManager.color(red, green, blue);
			this.drawTexturedModalRect(x + 81, y + 18 + tankProgress, 176, 30, 14, 14 - tankProgress);
			GlStateManager.color(1 / red, 1 / green, 1 / blue);
		}
		
		this.drawTexturedModalRect(x + 80, y + 17, 176, 14, 16, 16);

		if (container.fuelBurnTime > 0)
		{
			int fuelProgress = 15 - (int) Math.floor(14F - (14F / container.fuelBurn * container.fuelBurnTime));
			this.drawTexturedModalRect(x + 81, y + 50 - fuelProgress, 176, 14 - fuelProgress, 14, fuelProgress);
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		fontRendererObj.drawString(I18n.format("tile.potionVaporizer.name", new Object[0]), 8, 6, 4210752);

		// Draw Active Potion

		int guiMouseX = mouseX - guiLeft;
		int guiMouseY = mouseY - guiTop;
		if (guiMouseX > 79 && guiMouseX < 96 && guiMouseY > 16 && guiMouseY < 33)
		{
			ContainerPotionVaporizer container = (ContainerPotionVaporizer) inventorySlots;

			if (container.duration != 0 && container.potionID >= 0 && container.potionID < Potion.potionTypes.length)
			{
				int i = mouseX - guiLeft, j = mouseY - guiTop;
				Potion potion = Potion.potionTypes[container.potionID];

				if (potion != null)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.getTextureManager().bindTexture(inventoryBackground);
					this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

					if (potion.hasStatusIcon())
					{
						int l = potion.getStatusIconIndex();
						this.drawTexturedModalRect(i + 6, j + 7, 0 + l % 8 * 18, 198 + l / 8 * 18, 18, 18);
					}

					String s1 = I18n.format(potion.getName(), new Object[0]);

					int amplifier = container.amplifier;
					if (amplifier == 1)
					{
						s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
					}
					else if (amplifier == 2)
					{
						s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
					}
					else if (amplifier == 3)
					{
						s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
					}

					this.fontRendererObj.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 6), 16777215);
					String s = StringUtils.ticksToElapsedTime(container.durationLeft);
					this.fontRendererObj.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 6 + 10), 8355711);
				}
			}
		}
	}
}
