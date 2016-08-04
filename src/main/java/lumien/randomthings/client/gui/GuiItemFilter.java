package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiCustomButton;
import lumien.randomthings.container.ContainerItemFilter;
import lumien.randomthings.item.ItemItemFilter.ItemFilterRepresentation;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageItemFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiItemFilter extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/itemFilter.png");

	GuiCustomButton metadataButton;
	GuiCustomButton oreDictButton;
	GuiCustomButton nbtButton;
	GuiCustomButton listTypeButton;

	ItemFilterRepresentation repres;

	public GuiItemFilter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerItemFilter(player, world, x, y, z));

		xSize = 220;
		ySize = 133;

		repres = ((ContainerItemFilter) this.inventorySlots).repres;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		metadataButton = new GuiCustomButton(this, 0, repres.respectMetadata(), guiLeft + 173, guiTop + 4, 20, 20, "", background, 0, 133).setToolTips("tooltip.itemFilter.nometadata", "tooltip.itemFilter.metadata");
		this.buttonList.add(metadataButton);

		oreDictButton = new GuiCustomButton(this, 1, repres.respectOreDictionary(), guiLeft + 195, guiTop + 4, 20, 20, "", background, 40, 133).setToolTips("tooltip.itemFilter.nooredict", "tooltip.itemFilter.oredict");
		this.buttonList.add(oreDictButton);

		nbtButton = new GuiCustomButton(this, 2, repres.respectNBT(), guiLeft + 173, guiTop + 4 + 22, 20, 20, "", background, 80, 133).setToolTips("tooltip.itemFilter.nonbt", "tooltip.itemFilter.nbt");
		this.buttonList.add(nbtButton);

		listTypeButton = new GuiCustomButton(this, 3, repres.getListType() == 0 ? false : true, guiLeft + 195, guiTop + 4 + 22, 20, 20, "", background, 120, 133).setToolTips("tooltip.itemFilter.whitelist", "tooltip.itemFilter.blacklist");
		this.buttonList.add(listTypeButton);

		// listTypeButton = new GuiCustomButton(this, 1, guiLeft + 173, guiTop +
		// 4 + 22);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	protected void actionPerformed(GuiButton pressedButton)
	{
		if (pressedButton instanceof GuiCustomButton)
		{
			((GuiCustomButton) pressedButton).toggle();
		}

		MessageItemFilter message = new MessageItemFilter(pressedButton.id);
		PacketHandler.INSTANCE.sendToServer(message);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
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
		for (GuiButton guibutton : this.buttonList)
        {
            if (guibutton.isMouseOver())
            {
                guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
		
		fontRendererObj.drawString(I18n.format("item.itemFilter.name", new Object[0]), 8, 6, 4210752);
	}


}
