package lumien.randomthings.handler;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.tree.MethodNode;

import baubles.api.BaublesApi;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockLifeAnchor;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.entitys.EntitySoul;
import lumien.randomthings.item.ItemObsidianSkullRing;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.Colors;
import lumien.randomthings.lib.PlayerAbilitiesProperty;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import lumien.randomthings.tileentity.TileEntityRedstoneInterface;
import lumien.randomthings.util.DyeUtil;
import lumien.randomthings.util.EntityUtil;
import lumien.randomthings.util.InventoryUtil;
import lumien.randomthings.util.client.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RTEventHandler
{
	static Random rng = new Random();

	@SubscribeEvent
	public void chatEvent(ServerChatEvent event)
	{
		Iterator<TileEntityChatDetector> iterator = TileEntityChatDetector.detectors.iterator();

		while (iterator.hasNext())
		{
			TileEntityChatDetector chatDetector = iterator.next();
			if (chatDetector.isInvalid())
			{
				iterator.remove();
			}
			else
			{
				if (chatDetector.checkMessage(event.username, event.message))
				{
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderGameOverlay(RenderGameOverlayEvent event)
	{
		if (event.type != null)
		{
			if (event.type == RenderGameOverlayEvent.ElementType.HEALTH)
			{
				renderLavaCharm(event);
			}
			else if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			{
				renderRedstoneTool(event);
			}
		}
	}

	private void renderRedstoneTool(RenderGameOverlayEvent event)
	{
		ItemStack equippedItem;

		Minecraft minecraft = Minecraft.getMinecraft();

		if ((equippedItem = minecraft.thePlayer.getCurrentEquippedItem()) != null)
		{
			if (equippedItem.getItem() == ModItems.redstoneTool)
			{
				MovingObjectPosition objectMouseOver = minecraft.objectMouseOver;

				if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
				{
					IBlockState hitState = minecraft.theWorld.getBlockState(objectMouseOver.getBlockPos());
					Block hitBlock = hitState.getBlock();

					if (hitBlock instanceof BlockRedstoneWire)
					{
						int width = event.resolution.getScaledWidth();
						int height = event.resolution.getScaledHeight();

						int power = (Integer) hitState.getValue(BlockRedstoneWire.POWER);

						Minecraft.getMinecraft().fontRendererObj.drawString(power + "", width/2 + 5, height/2 + 5, Colors.RED_INT);
						GlStateManager.color(1, 1, 1, 1);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void anvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack left = event.left;
		ItemStack right = event.right;

		if (left != null && right != null)
		{
			Item leftItem = left.getItem();
			Item rightItem = right.getItem();

			if (Loader.isModLoaded("Baubles"))
			{
				if (arePairs(leftItem, rightItem, ModItems.obsidianSkull, Items.fire_charge))
				{
					event.output = new ItemStack(ModItems.obsidianSkullRing);
					event.cost = 3;
				}
			}

			if (arePairs(leftItem, rightItem, ModItems.waterWalkingBoots, ModItems.obsidianSkull) || arePairs(leftItem, rightItem, ModItems.waterWalkingBoots, ModItems.obsidianSkullRing))
			{
				event.output = new ItemStack(ModItems.obsidianWaterWalkingBoots);
				event.cost = 10;
			}

			if (arePairs(leftItem, rightItem, ModItems.obsidianWaterWalkingBoots, ModItems.lavaCharm))
			{
				event.output = new ItemStack(ModItems.lavaWader);
				event.cost = 15;
			}
		}
	}

	private boolean arePairs(Object o1, Object o2, Object o3, Object o4)
	{
		return ((o1 == o3 && o2 == o4) || (o1 == o4) && (o2 == o3));
	}

	@SideOnly(Side.CLIENT)
	private void renderLavaCharm(RenderGameOverlayEvent event)
	{
		ItemStack lavaProtector = null;
		ItemStack lavaCharm = null;

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		lavaCharm = InventoryUtil.getBauble(ModItems.lavaCharm, player);

		if (lavaCharm == null)
		{
			lavaCharm = InventoryUtil.getPlayerInventoryItem(ModItems.lavaCharm, player);
		}

		if (lavaCharm != null)
		{
			lavaProtector = lavaCharm;
		}

		ItemStack boots = player.getEquipmentInSlot(1);
		if (boots != null)
		{
			if (boots.getItem() == ModItems.lavaWader)
			{
				lavaProtector = boots;
			}
		}

		if (lavaProtector != null)
		{
			NBTTagCompound compound = lavaProtector.getTagCompound();
			if (compound != null)
			{
				float charge = compound.getInteger("charge");
				Minecraft mc = Minecraft.getMinecraft();
				mc.renderEngine.bindTexture(new ResourceLocation("randomthings:textures/gui/lavaCharmBar.png"));
				GuiIngame ingameGui = mc.ingameGUI;

				int width = event.resolution.getScaledWidth();
				int height = event.resolution.getScaledHeight();

				int count = (int) Math.floor(charge / 2F / 10F);

				int left = 0;

				int top = height - 40 - 10;

				if (ForgeHooks.getTotalArmorValue(mc.thePlayer) != 0)
				{
					top -= 10;
				}

				GlStateManager.enableBlend();
				for (int i = 0; i < count + 1; i++)
				{
					if (i == count + 1 - 1)
					{
						float countFloat = charge / 2F / 10F + 10f;
						GlStateManager.color(1, 1, 1, (countFloat) % ((int) (countFloat)));
					}

					ingameGui.drawTexturedModalRect(width / 2 - 92 + left, top, 0, 0, 10, 10);
					left += 8;
					GlStateManager.color(1, 1, 1, 1);
				}
				mc.renderEngine.bindTexture(Gui.icons);
				GlStateManager.disableBlend();
			}
		}
	}

	private void handleLavaProtection(LivingAttackEvent event)
	{
		ItemStack lavaProtector = null;
		ItemStack lavaCharm = null;

		lavaCharm = InventoryUtil.getBauble(ModItems.lavaCharm, (EntityPlayer) event.entityLiving);

		if (lavaCharm == null)
		{
			lavaCharm = InventoryUtil.getPlayerInventoryItem(ModItems.lavaCharm, (EntityPlayer) event.entityLiving);
		}

		if (lavaCharm != null)
		{
			lavaProtector = lavaCharm;
		}

		ItemStack boots = event.entityLiving.getEquipmentInSlot(1);
		if (boots != null)
		{
			if (boots.getItem() == ModItems.lavaWader)
			{
				lavaProtector = boots;
			}
		}

		if (lavaProtector != null)
		{
			NBTTagCompound compound = lavaProtector.getTagCompound();
			if (compound != null)
			{
				int charge = compound.getInteger("charge");
				if (charge > 0)
				{
					compound.setInteger("charge", charge - 1);
					compound.setInteger("chargeCooldown", 40);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingAttacked(LivingAttackEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote)
		{
			if (event.ammount > 0 && event.entityLiving instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) event.entityLiving;

				if (event.source == DamageSource.lava)
				{
					handleLavaProtection(event);
				}

				if (event.source.isFireDamage() && event.source != DamageSource.lava)
				{
					handleFireProtection(event);
				}

				if (!event.isCanceled() && !event.source.canHarmInCreative())
				{
					handleLinkingOrb(event, player);
				}
			}

			if (!event.isCanceled() && event.source instanceof EntityDamageSource && !(event.source instanceof EntityDamageSourceIndirect))
			{
				EntityDamageSource damageSource = (EntityDamageSource) event.source;

				if (damageSource.getEntity() != null && damageSource.getEntity() instanceof EntityLivingBase)
				{
					EntityLivingBase livingEntity = (EntityLivingBase) damageSource.getEntity();

					if (livingEntity.isPotionActive(ModPotions.imbueFire))
					{
						event.entityLiving.setFire(10);
					}
					else if (livingEntity.isPotionActive(ModPotions.imbueWither))
					{
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.wither.id, 5 * 20, 1));
					}
					else if (livingEntity.isPotionActive(ModPotions.imbuePoison))
					{
						event.entityLiving.addPotionEffect(new PotionEffect(Potion.poison.id, 10 * 20, 1));
					}
				}
			}
		}
	}

	private void handleLinkingOrb(LivingAttackEvent event, EntityPlayer player)
	{
		int slot = InventoryUtil.getInventorySlotContainItem(player.inventory, ModItems.linkingOrb);
		if (slot != -1)
		{
			ItemStack orb = player.inventory.getStackInSlot(slot);
			NBTTagCompound compound;
			if ((compound = orb.getTagCompound()) != null)
			{
				if (compound.getBoolean("active") && compound.hasKey("targetX"))
				{
					BlockPos anchor = new BlockPos(compound.getInteger("targetX"), compound.getInteger("targetY"), compound.getInteger("targetZ"));
					World worldObj = DimensionManager.getWorld(compound.getInteger("dimension"));
					if (worldObj != null && worldObj.isBlockLoaded(anchor) && worldObj.getBlockState(anchor).getBlock() instanceof BlockLifeAnchor)
					{
						AxisAlignedBB boundingBox = AxisAlignedBB.fromBounds(anchor.getX(), anchor.getY(), anchor.getZ(), anchor.getX(), anchor.getY(), anchor.getZ()).expand(5, 5, 5);

						float damageLeft = event.ammount;
						List<EntityLivingBase> entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);
						entityList.remove(player);

						if (entityList.size() > 0)
						{
							for (EntityLivingBase entity : entityList)
							{
								if (entity.isEntityAlive() && entity.getHealth() > 0)
								{
									if (damageLeft > 0)
									{
										float health = entity.getHealth();
										if (health >= damageLeft)
										{
											entity.attackEntityFrom(event.source, damageLeft);
											damageLeft = 0;
										}
										else
										{
											damageLeft -= entity.getHealth();
											entity.setHealth(0);
										}
									}
								}
							}

							if (damageLeft != event.ammount)
							{
								event.setCanceled(true);
								if (damageLeft > 0)
								{
									event.setCanceled(true);
									player.attackEntityFrom(event.source, damageLeft);
								}
							}
						}
					}
				}
			}
		}
	}

	private void handleFireProtection(LivingAttackEvent event)
	{
		EntityPlayer player = (EntityPlayer) event.entityLiving;
		ItemStack baubleSkull = InventoryUtil.getBauble(ModItems.obsidianSkullRing, player);
		ItemStack inventorySkull = InventoryUtil.getPlayerInventoryItem(ModItems.obsidianSkull, player);
		ItemStack obsidianBoots = player.getEquipmentInSlot(1);

		ItemStack skull = null;

		skull = baubleSkull;

		if (skull == null)
		{
			skull = inventorySkull;
		}

		if (skull == null)
		{
			skull = obsidianBoots;
		}

		if (skull != null)
		{
			float amount = event.ammount;
			float rngFloat = rng.nextFloat();

			float chance = amount / 100;
			chance *= amount * amount;

			if (rngFloat > chance)
			{
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void useHoe(UseHoeEvent event)
	{
		if (event.world.getBlockState(event.pos).getBlock() == ModBlocks.fertilizedDirt)
		{
			event.setResult(Result.ALLOW);
			event.world.setBlockState(event.pos, ModBlocks.fertilizedDirtTilled.getDefaultState());
			event.world.playSoundEffect(event.pos.getX() + 0.5F, event.pos.getY() + 0.5F, event.pos.getZ() + 0.5F, ModBlocks.fertilizedDirtTilled.stepSound.getStepSound(), (ModBlocks.fertilizedDirtTilled.stepSound.getVolume() + 1.0F) / 2.0F, ModBlocks.fertilizedDirtTilled.stepSound.getFrequency() * 0.8F);
		}
	}

	@SubscribeEvent
	public void livingUpdate(LivingUpdateEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote)
		{
			if (event.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.entityLiving;
				PlayerAbilitiesProperty abilities = (PlayerAbilitiesProperty) player.getExtendedProperties(PlayerAbilitiesProperty.KEY);
				if (abilities.isImmortal())
				{
					player.heal(0.5F);
				}
			}
		}
		else
		{
			if (event.entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.entityLiving;
				if (!player.isSneaking())
				{
					ItemStack boots = player.inventory.armorItemInSlot(0);
					if (boots != null && ((boots.getItem() == ModItems.waterWalkingBoots || boots.getItem() == ModItems.obsidianWaterWalkingBoots) || boots.getItem() == ModItems.lavaWader))
					{
						BlockPos liquid = new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
						BlockPos air = new BlockPos((int) player.posX, (int) (player.posY + player.height), (int) player.posZ);
						Block liquidBlock = player.worldObj.getBlockState(liquid).getBlock();
						Material liquidMaterial = liquidBlock.getMaterial();

						if ((liquidMaterial == Material.water || (boots.getItem() == ModItems.lavaWader && liquidMaterial == Material.lava)) && player.worldObj.getBlockState(air).getBlock().isAir(player.worldObj, air) && EntityUtil.isJumping(player))
						{
							player.moveEntity(0, 0.22, 0);
						}
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote)
		{
			if (event.entityLiving instanceof EntityPlayer)
			{
				if (!(event.entityLiving instanceof FakePlayer))
				{
					EntityPlayer player = (EntityPlayer) event.entityLiving;
					if (!event.source.canHarmInCreative())
					{
						PlayerAbilitiesProperty abilities = (PlayerAbilitiesProperty) player.getExtendedProperties(PlayerAbilitiesProperty.KEY);
						if (abilities.isImmortal())
						{
							player.setHealth(0.1f);
							event.setCanceled(true);
						}
					}

					if (!event.isCanceled())
					{
						player.worldObj.spawnEntityInWorld(new EntitySoul(player.worldObj, player.posX, player.posY, player.posZ, player.getGameProfile().getName()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void entityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			event.entity.registerExtendedProperties(PlayerAbilitiesProperty.KEY, new PlayerAbilitiesProperty());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderWorldPost(RenderWorldLastEvent event)
	{
		RandomThings.proxy.renderRedstoneInterfaceStuff(event.partialTicks);

		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		if (player != null)
		{
			ItemStack itemStack = player.getCurrentEquippedItem();

			if (itemStack != null)
			{
				Item item = itemStack.getItem();
				if (item == ModItems.positionFilter && itemStack.getTagCompound() != null)
				{
					NBTTagCompound compound = itemStack.getTagCompound();
					int dimension = compound.getInteger("dimension");
					int filterX = compound.getInteger("filterX");
					int filterY = compound.getInteger("filterY");
					int filterZ = compound.getInteger("filterZ");

					if (player.dimension == dimension)
					{
						double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks;
						double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks;
						double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks;

						GlStateManager.enableBlend();
						GlStateManager.pushMatrix();
						{
							GlStateManager.translate(-playerX, -playerY, -playerZ);
							RenderUtils.drawCube(filterX - 0.01F, filterY - 0.01F, filterZ - 0.01F, 1.02f, 0.4f, 0, 1, 0.2f);
						}
						GlStateManager.popMatrix();
						GlStateManager.disableBlend();
					}
				}
			}
		}
	}
}
