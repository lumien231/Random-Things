package lumien.randomthings.client.gui.redstoneremote;

import lumien.randomthings.client.gui.GuiContainerBase;
import lumien.randomthings.container.ContainerRedstoneRemoteStill;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GuiRedstoneRemoteStillEdit extends GuiContainerBase {
    final ResourceLocation background = new ResourceLocation("randomthings:textures/gui/redstoneRemote/redstoneRemoteEdit.png");

    public GuiRedstoneRemoteStillEdit(EntityPlayer player, World world, int x, int y, int z)
    {
        super(new ContainerRedstoneRemoteStill(player, world, x, y, z));

        this.xSize = 176;
        this.ySize = 150;
    }

    @Override
    public void initGui()
    {
        super.initGui();
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
        fontRenderer.drawString(I18n.format("item.redstoneRemoteStill.name", new Object[0]), 8, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 95 + 2, 4210752);
    }
}
