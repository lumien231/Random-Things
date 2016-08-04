package lumien.randomthings.client.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import lumien.randomthings.client.gui.elements.GuiCustomButton;
import lumien.randomthings.client.gui.elements.GuiStringList;
import lumien.randomthings.container.ContainerVoxelProjector;
import lumien.randomthings.lib.IStringCallback;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageVoxelProjector;
import lumien.randomthings.tileentity.TileEntityVoxelProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiVoxelProjector extends GuiContainer implements IStringCallback
{
	TileEntityVoxelProjector te;
	final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/voxelProjector.png");
	final ResourceLocation buttons = new ResourceLocation("randomthings:textures/gui/voxelProjectorButtons.png");

	GuiCustomButton toggleAmbientLight;
	GuiCustomButton toggleRandomizer;

	GuiStringList availableModels;

	int oldRotation;
	int oldScale;
	int oldRotationSpeed;

	public GuiVoxelProjector(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerVoxelProjector(player, world, x, y, z));

		this.te = (TileEntityVoxelProjector) world.getTileEntity(new BlockPos(x, y, z));
		this.oldRotation = this.te.getModelRotation();
		this.oldScale = this.te.getScale();
		oldRotationSpeed = this.te.getRotationSpeed();

		this.xSize = 230;
		this.ySize = 219;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.buttonList.add(new GuiSlider(0, this.guiLeft + 5, 90, 120, 20, "Model Rotation: ", "", 0, 360, this.te.getModelRotation(), false, true, new GuiSlider.ISlider()
		{
			@Override
			public void onChangeSliderValue(GuiSlider slider)
			{
				int rotation = (int) Math.floor(slider.sliderValue * 360);
				GuiVoxelProjector.this.te.setModelRotation(rotation);

				if (rotation != GuiVoxelProjector.this.oldRotation)
				{
					GuiVoxelProjector.this.oldRotation = (int) Math.floor(slider.sliderValue * 360);

					MessageVoxelProjector message = new MessageVoxelProjector(GuiVoxelProjector.this.te.getPos());
					message.setModelRotation(rotation);

					PacketHandler.INSTANCE.sendToServer(message);
				}
			}
		}));

		this.buttonList.add(new GuiSlider(1, this.guiLeft + 5, 60, 120, 20, "Scale: ", "", 1, 20, this.te.getScale(), false, true, new GuiSlider.ISlider()
		{
			@Override
			public void onChangeSliderValue(GuiSlider slider)
			{
				int scale = Math.min(20, (int) Math.floor((slider.sliderValue + 1F / 20f) * 20));
				GuiVoxelProjector.this.te.setScale(scale);

				if (scale != GuiVoxelProjector.this.oldScale)
				{
					GuiVoxelProjector.this.oldScale = scale;

					MessageVoxelProjector message = new MessageVoxelProjector(GuiVoxelProjector.this.te.getPos());
					message.setScale(scale);

					PacketHandler.INSTANCE.sendToServer(message);
				}
			}
		}));

		this.buttonList.add(new GuiSlider(2, this.guiLeft + 5, 120, 120, 20, "Rotation Speed: ", "", 0, 40, this.te.getRotationSpeed(), false, true, new GuiSlider.ISlider()
		{
			@Override
			public void onChangeSliderValue(GuiSlider slider)
			{
				int rotationSpeed = (int) Math.floor(slider.sliderValue * 40);
				GuiVoxelProjector.this.te.setRotationSpeed(rotationSpeed);

				if (rotationSpeed != GuiVoxelProjector.this.oldRotationSpeed)
				{
					GuiVoxelProjector.this.oldRotationSpeed = rotationSpeed;

					MessageVoxelProjector message = new MessageVoxelProjector(GuiVoxelProjector.this.te.getPos());
					message.setRotationSpeed(rotationSpeed);

					PacketHandler.INSTANCE.sendToServer(message);
				}
			}
		}));

		toggleAmbientLight = new GuiCustomButton(this, 3, te.ambientLight(), this.guiLeft + xSize - 25, 60, 20, 20, "", buttons, 0, 0);
		toggleRandomizer = new GuiCustomButton(this, 4, te.randomize(), this.guiLeft + xSize - 25, 85, 20, 20, "", buttons, 42, 0);

		this.buttonList.add(toggleAmbientLight);
		this.buttonList.add(toggleRandomizer);

		availableModels = new GuiStringList(this, Minecraft.getMinecraft(), 120, 50, this.guiLeft + 5, 150, width, height, Lists.<String>newArrayList());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
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
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		fontRendererObj.drawString(I18n.format("tile.voxelProjector.name", new Object[0]), 3, 6, 4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		availableModels.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void pressed(String string)
	{
		MessageVoxelProjector message = new MessageVoxelProjector(this.te.getPos());
		message.setModel(string);
		PacketHandler.INSTANCE.sendToServer(message);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button == toggleAmbientLight)
		{
			toggleAmbientLight.toggle();
			MessageVoxelProjector message = new MessageVoxelProjector(this.te.getPos());
			message.setAmbientLight(toggleAmbientLight.getValue());
			PacketHandler.INSTANCE.sendToServer(message);
		}

		if (button == toggleRandomizer)
		{
			toggleRandomizer.toggle();
			MessageVoxelProjector message = new MessageVoxelProjector(this.te.getPos());
			message.setRandomize(toggleRandomizer.getValue());
			PacketHandler.INSTANCE.sendToServer(message);
		}
	}

	public void setModelList(List<String> modelList)
	{
		this.availableModels.setList(modelList);
	}
}
