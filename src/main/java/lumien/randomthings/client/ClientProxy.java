package lumien.randomthings.client;

import java.util.Collection;
import java.util.Iterator;

import lumien.randomthings.CommonProxy;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.models.ItemModels;
import lumien.randomthings.client.models.blocks.BlockModels;
import lumien.randomthings.client.render.RenderReviveCircle;
import lumien.randomthings.client.render.RenderSoul;
import lumien.randomthings.client.render.RenderSpecialChest;
import lumien.randomthings.client.render.RenderVoxelProjector;
import lumien.randomthings.entitys.EntityReviveCircle;
import lumien.randomthings.entitys.EntitySoul;
import lumien.randomthings.item.ItemRezStone;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityRedstoneInterface;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import lumien.randomthings.tileentity.TileEntityVoxelProjector;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy
{
	@Override
	public boolean canBeCollidedWith(EntitySoul soul)
	{
		ItemStack equipped = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
		if (equipped != null && equipped.getItem() instanceof ItemRezStone)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void registerModels()
	{
		ItemModels.register();
		BlockModels.register();
	}

	@Override
	public boolean isPlayerOnline(String username)
	{
		NetHandlerPlayClient netclienthandler = Minecraft.getMinecraft().thePlayer.sendQueue;
		Collection collection = netclienthandler.getPlayerInfoMap();

		Iterator<NetworkPlayerInfo> iterator = collection.iterator();

		while (iterator.hasNext())
		{
			NetworkPlayerInfo info = iterator.next();

			if (info.getGameProfile().getName().toLowerCase().equals(username.toLowerCase()))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySoul.class, new RenderSoul(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityReviveCircle.class, new RenderReviveCircle(Minecraft.getMinecraft().getRenderManager()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpecialChest.class, new RenderSpecialChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoxelProjector.class, new RenderVoxelProjector());
	}

	@Override
	public void renderRedstoneInterfaceStuff(float partialTicks)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack itemStack = player.getCurrentEquippedItem();
		if (itemStack != null)
		{
			Item item = itemStack.getItem();

			if (item == ModItems.redstoneTool)
			{
				drawInterfaceLines(player, partialTicks);
				drawLinkingCube(itemStack, player, partialTicks);
			}
		}
	}

	private void drawLinkingCube(ItemStack itemStack, EntityPlayerSP player, float partialTicks)
	{
		if (itemStack.getTagCompound() != null)
		{
			NBTTagCompound compound = itemStack.getTagCompound();
			if (compound.getBoolean("linking"))
			{
				int oX = compound.getInteger("oX");
				int oY = compound.getInteger("oY");
				int oZ = compound.getInteger("oZ");
				double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
				double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
				double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

				GlStateManager.enableBlend();
				GlStateManager.pushMatrix();
				{
					GlStateManager.translate(-playerX, -playerY, -playerZ);
					RenderUtils.drawCube(oX - 0.01F, oY - 0.01F, oZ - 0.01F, 1.02f, 122, 0, 0, 46);
				}
				GlStateManager.popMatrix();
				GlStateManager.disableBlend();
			}
		}
	}

	private void drawInterfaceLines(EntityPlayerSP player, float partialTicks)
	{
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;

		GlStateManager.pushAttrib();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glLineWidth(10);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		GlStateManager.pushMatrix();
		{
			worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			synchronized (TileEntityRedstoneInterface.interfaces)
			{
				for (TileEntityRedstoneInterface redstoneInterface : TileEntityRedstoneInterface.interfaces)
				{
					if (!redstoneInterface.isInvalid())
					{
						BlockPos position = redstoneInterface.getPos();
						BlockPos target = redstoneInterface.getTarget();

						if (position.distanceSq(player.getPosition()) < 225)
						{
							if (target != null)
							{
								if (redstoneInterface.getWorld().isRemote)
								{
									worldRenderer.pos(target.getX() + 0.5 - playerX, target.getY() + 0.5 - playerY, target.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
									worldRenderer.pos(position.getX() + 0.5 - playerX, position.getY() + 0.5 - playerY, position.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
								}
							}
						}
					}
				}
			}
			tessellator.draw();
		}
		GlStateManager.popMatrix();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
		Minecraft.getMinecraft().entityRenderer.enableLightmap();
	}
}
