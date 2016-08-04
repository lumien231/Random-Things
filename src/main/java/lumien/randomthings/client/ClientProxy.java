package lumien.randomthings.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import lumien.randomthings.CommonProxy;
import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.client.models.ItemModels;
import lumien.randomthings.client.models.blocks.BlockModels;
import lumien.randomthings.client.render.RenderArtificialEndPortal;
import lumien.randomthings.client.render.RenderBiomeRadar;
import lumien.randomthings.client.render.RenderProjectedItem;
import lumien.randomthings.client.render.RenderReviveCircle;
import lumien.randomthings.client.render.RenderSoul;
import lumien.randomthings.client.render.RenderSpecialChest;
import lumien.randomthings.client.render.RenderSpirit;
import lumien.randomthings.client.render.RenderVoxelProjector;
import lumien.randomthings.entitys.EntityArtificialEndPortal;
import lumien.randomthings.entitys.EntityProjectedItem;
import lumien.randomthings.entitys.EntityReviveCircle;
import lumien.randomthings.entitys.EntitySoul;
import lumien.randomthings.entitys.EntitySpirit;
import lumien.randomthings.item.ItemRezStone;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.lib.IRTItemColor;
import lumien.randomthings.tileentity.TileEntityBiomeRadar;
import lumien.randomthings.tileentity.TileEntityRedstoneObserver;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import lumien.randomthings.tileentity.TileEntityVoxelProjector;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityAdvancedRedstoneInterface;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityBasicRedstoneInterface;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityRedstoneInterface;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import org.lwjgl.opengl.GL11;

public class ClientProxy extends CommonProxy
{
	static Field itemColorsField;
	static
	{
		try
		{
			itemColorsField = Minecraft.class.getDeclaredField(MCPNames.field("field_184128_aI"));
			itemColorsField.setAccessible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean canBeCollidedWith(EntitySoul soul)
	{
		ItemStack equipped = Minecraft.getMinecraft().thePlayer.getHeldItemMainhand();
		if (equipped != null && equipped.getItem() instanceof ItemRezStone)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	HashMap<Object, Object> scheduledColorRegister = new HashMap<Object, Object>();

	@Override
	public void scheduleColor(Object o)
	{
		if (o instanceof IRTBlockColor || o instanceof IRTItemColor)
		{
			scheduledColorRegister.put(o, o);
		}
	}

	private void registerColors()
	{
		for (Entry<Object, Object> entry : scheduledColorRegister.entrySet())
		{
			if (entry.getKey() instanceof IRTBlockColor)
			{
				final IRTBlockColor blockColor = (IRTBlockColor) entry.getKey();
				Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
				{
					@Override
					public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
					{
						return blockColor.colorMultiplier(state, p_186720_2_, pos, tintIndex);
					}

				}, (Block) entry.getValue());
			}
			else if (entry.getKey() instanceof IRTItemColor)
			{
				final IRTItemColor itemColor = (IRTItemColor) entry.getKey();
				try
				{
					ItemColors itemColors = (ItemColors) itemColorsField.get(Minecraft.getMinecraft());
					itemColors.registerItemColorHandler(new IItemColor()
					{

						@Override
						public int getColorFromItemstack(ItemStack stack, int tintIndex)
						{
							return itemColor.getColorFromItemstack(stack, tintIndex);
						}

					}, (Item) entry.getValue());
				}
				catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
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
		NetHandlerPlayClient netclienthandler = Minecraft.getMinecraft().thePlayer.connection;
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
		RenderingRegistry.registerEntityRenderingHandler(EntitySpirit.class, new RenderSpirit(Minecraft.getMinecraft().getRenderManager(), new ModelSlime(16), 0.25F));
		RenderingRegistry.registerEntityRenderingHandler(EntityArtificialEndPortal.class, new RenderArtificialEndPortal(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectedItem.class, new RenderProjectedItem(Minecraft.getMinecraft().getRenderManager(),Minecraft.getMinecraft().getRenderItem()));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpecialChest.class, new RenderSpecialChest());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVoxelProjector.class, new RenderVoxelProjector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBiomeRadar.class, new RenderBiomeRadar());

		registerColors();
	}

	@Override
	public void renderRedstoneInterfaceStuff(float partialTicks)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ItemStack itemStack = player.getHeldItemMainhand();
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
		VertexBuffer worldRenderer = tessellator.getBuffer();

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
					if (!redstoneInterface.isInvalid() && redstoneInterface.getWorld().isRemote)
					{
						ArrayList<BlockPos> positions = new ArrayList<BlockPos>();

						if (redstoneInterface instanceof TileEntityBasicRedstoneInterface)
						{
							TileEntityBasicRedstoneInterface simpleRedstoneInterface = (TileEntityBasicRedstoneInterface) redstoneInterface;
							BlockPos position = simpleRedstoneInterface.getPos();
							BlockPos target = simpleRedstoneInterface.getTarget();
							if (target != null)
							{
								positions.add(target);
								positions.add(position);
							}
						}
						else if (redstoneInterface instanceof TileEntityAdvancedRedstoneInterface)
						{
							TileEntityAdvancedRedstoneInterface advancedRedstoneInterface = (TileEntityAdvancedRedstoneInterface) redstoneInterface;
							BlockPos position = advancedRedstoneInterface.getPos();
							Set<BlockPos> targets = advancedRedstoneInterface.getTargets();

							for (BlockPos target : targets)
							{
								positions.add(target);
								positions.add(position);
							}
						}

						for (int i = 0; i < positions.size(); i += 2)
						{
							BlockPos target = positions.get(i);
							BlockPos position = positions.get(i + 1);

							if (position.distanceSq(player.getPosition()) < 225)
							{
								worldRenderer.pos(target.getX() + 0.5 - playerX, target.getY() + 0.5 - playerY, target.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
								worldRenderer.pos(position.getX() + 0.5 - playerX, position.getY() + 0.5 - playerY, position.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
							}
						}
					}
				}
			}

			for (TileEntityRedstoneObserver redstoneObserver : TileEntityRedstoneObserver.loadedObservers)
			{
				if (!redstoneObserver.isInvalid())
				{
					BlockPos target = redstoneObserver.getTarget();
					BlockPos position = redstoneObserver.getPos();

					if (target != null)
					{
						if (target.distanceSq(player.getPosition()) < 225)
						{
							worldRenderer.pos(target.getX() + 0.5 - playerX, target.getY() + 0.5 - playerY, target.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
							worldRenderer.pos(position.getX() + 0.5 - playerX, position.getY() + 0.5 - playerY, position.getZ() + 0.5 - playerZ).color(255, 0, 0, 255).endVertex();
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
