package lumien.randomthings.client.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.container.ContainerDyeingMachine;
import lumien.randomthings.util.DyeUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class GuiDyeingMachine extends GuiContainerBase
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/dyeingMachine.png");

	int targetColor;
	int currentColor;
	float p;

	static final float colorMod = 1F / 255F;

	public GuiDyeingMachine(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerDyeingMachine(player, world, x, y, z));

		this.xSize = 176; // 176
		this.ySize = 141;

		this.p = 1;

		this.targetColor = this.currentColor = 9145227;
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (this.currentColor != this.targetColor)
		{
			this.p -= 0.02f;
			Color currentObj = new Color(currentColor);
			Color targetObj = new Color(targetColor);

			currentColor = new Color(colorMod * (currentObj.getRed() * p + targetObj.getRed() * (1 - p)), colorMod * (currentObj.getGreen() * p + targetObj.getGreen() * (1 - p)), colorMod * (currentObj.getBlue() * p + targetObj.getBlue() * (1 - p))).getRGB();
		}

		if (p <= 0f)
		{
			this.currentColor = this.targetColor;
			this.p = 1;
		}

		if (inventorySlots.getSlot(1).getHasStack() && p == 1)
		{
			ItemStack dye = inventorySlots.getSlot(1).getStack();

			int[] oreDictIds = OreDictionary.getOreIDs(dye);

			int checkDye = DyeUtil.getDyeColor(dye);

			if (checkDye != 0)
			{
				this.targetColor = checkDye;
			}
		}
		else if (!inventorySlots.getSlot(1).getHasStack() && p == 1)
		{
			targetColor = 9145227;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRenderer.drawString(I18n.format("tile.dyeingMachine.name", new Object[0]), 8, 6, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		this.mc.renderEngine.bindTexture(background);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		Color c = new Color(currentColor);
		GL11.glColor3f(colorMod * c.getRed(), colorMod * c.getGreen(), colorMod * c.getBlue());
		this.drawTexturedModalRect(x + 102, y + 23, 176, 21, 22, 15);
	}
}
