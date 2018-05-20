package lumien.randomthings.client.render;

import lumien.randomthings.tileentity.TileEntitySpecialChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpecialChest extends TileEntitySpecialRenderer
{
	private static final ResourceLocation textureNatureChest = new ResourceLocation("randomthings:textures/blocks/specialChest/nature.png");
	private static final ResourceLocation textureWaterChest = new ResourceLocation("randomthings:textures/blocks/specialChest/water.png");

	private ModelChest simpleChest = new ModelChest();
	private boolean isChristams;
	private static final String __OBFID = "CL_00000965";

	public RenderSpecialChest()
	{

	}

	public void func_180538_a(TileEntitySpecialChest p_180538_1_, double p_180538_2_, double p_180538_4_, double p_180538_6_, float p_180538_8_, int p_180538_9_)
	{
		switch (p_180538_1_.getChestType())
		{
		case 0:
			Minecraft.getMinecraft().renderEngine.bindTexture(textureNatureChest);
			break;
		case 1:
			Minecraft.getMinecraft().renderEngine.bindTexture(textureWaterChest);
			break;
		}
		int j;

		if (!p_180538_1_.hasWorld())
		{
			j = 0;
		}
		else
		{
			Block block = p_180538_1_.getBlockType();
			j = p_180538_1_.getBlockMetadata();

			if (block instanceof BlockChest && j == 0)
			{
				((BlockChest) block).checkForSurroundingChests(p_180538_1_.getWorld(), p_180538_1_.getPos(), p_180538_1_.getWorld().getBlockState(p_180538_1_.getPos()));
				j = p_180538_1_.getBlockMetadata();
			}
		}

		ModelChest modelchest;

		modelchest = this.simpleChest;

		if (p_180538_9_ >= 0)
		{
			this.bindTexture(DESTROY_STAGES[p_180538_9_]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();

		if (p_180538_9_ < 0)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		}

		GlStateManager.translate((float) p_180538_2_, (float) p_180538_4_ + 1.0F, (float) p_180538_6_ + 1.0F);

		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		short short1 = 0;

		if (j == 2)
		{
			short1 = 180;
		}

		if (j == 3)
		{
			short1 = 0;
		}

		if (j == 4)
		{
			short1 = 90;
		}

		if (j == 5)
		{
			short1 = -90;
		}

		GlStateManager.rotate(short1, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		float f1 = p_180538_1_.prevLidAngle + (p_180538_1_.lidAngle - p_180538_1_.prevLidAngle) * p_180538_8_;
		float f2;

		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
		modelchest.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);
		modelchest.renderAll();
		GlStateManager.disableRescaleNormal();

		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (p_180538_9_ >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}

	@Override
	public void render(TileEntity te, double x, double y, double z, float p_180535_8_, int p_180535_9_, float alpha)
	{
		this.func_180538_a((TileEntitySpecialChest) te, x, y, z, p_180535_8_, p_180535_9_);
	}
}