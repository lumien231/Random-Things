package lumien.randomthings.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import lumien.randomthings.client.gui.elements.GuiScanResultList;
import lumien.randomthings.container.ContainerChunkAnalyzer;
import lumien.randomthings.handler.RTEventHandler;
import lumien.randomthings.handler.chunkanalyzer.ChunkAnalyzerResult;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.RandomThingsNetworkWrapper;
import lumien.randomthings.network.messages.MessageChunkAnalyzer;
import lumien.randomthings.network.messages.MessageChunkAnalyzer.ACTION;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiChunkAnalyzer extends GuiContainerBase
{
	ItemStack analyzerStack;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/chunkAnalyzer.png");

	EntityPlayer player;

	GuiScanResultList scanResultList;

	boolean scanning = false;

	public GuiChunkAnalyzer(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerChunkAnalyzer(player, world, x, y, z));

		this.player = player;
		
		((ContainerChunkAnalyzer)this.inventorySlots).setParent(this);

		analyzerStack = player.inventory.getCurrentItem();
		this.xSize = 190;
		this.ySize = 124;
	}

	public void setScanning(boolean value)
	{
		if (value)
		{
			this.buttonList.get(0).enabled = false;
			
			ItemStack is = player.getHeldItemMainhand();

			if (is.getSubCompound("result") != null)
			{
				ChunkAnalyzerResult results = new ChunkAnalyzerResult();
				results.readFromNBT(is.getSubCompound("result"));

				this.scanResultList.setResults(results);
			}
			else
			{
				this.scanResultList.setResults(null);
			}
		}
		else
		{
			this.buttonList.get(0).enabled = true;
			
			ItemStack is = player.getHeldItemMainhand();

			if (is.getSubCompound("result") != null)
			{
				ChunkAnalyzerResult results = new ChunkAnalyzerResult();
				results.readFromNBT(is.getSubCompound("result"));

				this.scanResultList.setResults(results);
			}
			else
			{
				this.scanResultList.setResults(null);
			}
		}
		
		this.scanning = value;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiButtonExt(0, this.guiLeft + 136, this.guiTop + 5, 50, 14, "Scan"));

		this.scanResultList = new GuiScanResultList(Minecraft.getMinecraft(), 182, 100, this.guiTop + 20, this.guiTop + 120, this.guiLeft + 4, width, height);
	
		ItemStack is = player.getHeldItemMainhand();

		if (is.getSubCompound("result") != null)
		{
			ChunkAnalyzerResult results = new ChunkAnalyzerResult();
			results.readFromNBT(is.getSubCompound("result"));

			this.scanResultList.setResults(results);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button.id == 0)
		{
			// Start
			MessageChunkAnalyzer msg = new MessageChunkAnalyzer(ACTION.START);

			PacketHandler.INSTANCE.sendToServer(msg);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		fontRenderer.drawString(I18n.format("item.chunkAnalyzer.name", new Object[0]), 8, 6, 4210752);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		
		scanResultList.handleMouseInput(mouseX, mouseY);
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

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		this.renderHoveredToolTip(mouseX, mouseY);

		this.scanResultList.drawScreen(mouseX, mouseY, partialTicks);
		
		if (scanning)
		{
			String scanning = "Scanning Chunk";

			for (int i = 0; i < (RTEventHandler.clientAnimationCounter / 15) % 3; i++)
			{
				scanning += ".";
			}

			fontRenderer.drawString(scanning, guiLeft + (int) (95 - fontRenderer.getStringWidth(scanning) / 2F), guiTop + 65, 16777215);
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1, par2, par3);
	}
}
