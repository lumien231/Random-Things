package lumien.randomthings.client.gui;

import org.lwjgl.opengl.GL11;

import lumien.randomthings.client.gui.elements.GuiCustomButton;
import lumien.randomthings.container.ContainerItemFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.network.PacketHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiItemFilter extends GuiContainer
{
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/itemFilter.png");
	EntityPlayer player;
	ItemStack itemFilter;

	GuiCustomButton oreDictButton;
	GuiCustomButton metadataButton;
	GuiCustomButton listTypeButton;

	public GuiItemFilter(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerItemFilter(player, world, x, y, z));

		xSize = 220;
		ySize = 133;

		this.player = player;
		this.itemFilter = player.getCurrentEquippedItem();
	}

	@Override
	public void initGui()
	{
		super.initGui();

		
	}

	@Override
	protected void actionPerformed(GuiButton pressedButton)
	{
		
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
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRendererObj.drawString(I18n.format("item.itemFilter.name", new Object[0]), 8, 6, 4210752);
	}


}
