package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiBoolButton;
import lumien.randomthings.client.gui.elements.GuiEnumButton;
import lumien.randomthings.container.ContainerIronDropper;
import lumien.randomthings.tileentity.TileEntityIronDropper;
import lumien.randomthings.tileentity.TileEntityIronDropper.EFFECTS;
import lumien.randomthings.tileentity.TileEntityIronDropper.PICKUP_DELAY;
import lumien.randomthings.tileentity.TileEntityIronDropper.REDSTONE_MODE;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiIronDropper extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/ironDropper.png");
	final TileEntity te;

	public GuiIronDropper(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerIronDropper(player, world, x, y, z));

		this.xSize = 176;
		this.ySize = 166;
		this.te = world.getTileEntity(new BlockPos(x, y, z));
	}

	@Override
	public void initGui()
	{
		super.initGui();

		GuiEnumButton<REDSTONE_MODE> redstoneModeButton = new GuiEnumButton<TileEntityIronDropper.REDSTONE_MODE>(0, this.guiLeft + 125, this.guiTop + 16, 20, 20, new ResourceLocation("randomthings", "textures/gui/ironDropper/redstoneMode.png"), TileEntityIronDropper.class, "redstoneMode", te);
		redstoneModeButton.setTooltips(new String[] { "tooltip.ironDropper.redstoneMode.pulse", "tooltip.ironDropper.redstoneMode.repeatPowered", "tooltip.ironDropper.redstoneMode.repeat" });
		this.buttonList.add(redstoneModeButton);

		GuiEnumButton<PICKUP_DELAY> pickupDelayButton = new GuiEnumButton<TileEntityIronDropper.PICKUP_DELAY>(1, this.guiLeft + 150, this.guiTop + 16, 20, 20, new ResourceLocation("randomthings", "textures/gui/ironDropper/pickupDelay.png"), TileEntityIronDropper.class, "pickupDelay", te);
		pickupDelayButton.setTooltips(new String[] { "tooltip.ironDropper.pickupDelay.none", "tooltip.ironDropper.pickupDelay.5", "tooltip.ironDropper.pickupDelay.20" });
		this.buttonList.add(pickupDelayButton);

		GuiBoolButton randomMotionButton = new GuiBoolButton(2, this.guiLeft + 125, this.guiTop + 41, 20, 20, new ResourceLocation("randomthings", "textures/gui/ironDropper/randomMotion.png"), TileEntityIronDropper.class, "randomMotion", te);
		randomMotionButton.setTooltips(new String[] { "tooltip.ironDropper.randomMotion.no", "tooltip.ironDropper.randomMotion.yes" });
		this.buttonList.add(randomMotionButton);

		GuiEnumButton<EFFECTS> effectsButton = new GuiEnumButton<EFFECTS>(3, this.guiLeft + 150, this.guiTop + 41, 20, 20, new ResourceLocation("randomthings", "textures/gui/ironDropper/effects.png"), TileEntityIronDropper.class, "effects", te);
		effectsButton.setTooltips(new String[] { "tooltip.ironDropper.effects.none", "tooltip.ironDropper.effects.soundOnly", "tooltip.ironDropper.effects.particlesOnly", "tooltip.ironDropper.effects.both" });
		this.buttonList.add(effectsButton);
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
		fontRendererObj.drawString(I18n.format("tile.ironDropper.name", new Object[0]), 8, 6, 4210752);

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
