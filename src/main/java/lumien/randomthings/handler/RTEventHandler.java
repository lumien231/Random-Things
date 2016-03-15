package lumien.randomthings.handler;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockContactButton;
import lumien.randomthings.block.BlockContactLever;
import lumien.randomthings.block.BlockLifeAnchor;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.models.blocks.ModelCustomWorkbench;
import lumien.randomthings.client.models.blocks.ModelFluidDisplay;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.entitys.EntitySoul;
import lumien.randomthings.entitys.EntitySpirit;
import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import lumien.randomthings.handler.spectre.SpectreHandler;
import lumien.randomthings.item.ItemEntityFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.AtlasSprite;
import lumien.randomthings.lib.Colors;
import lumien.randomthings.lib.IExplosionImmune;
import lumien.randomthings.lib.PlayerAbilitiesProperty;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageSoundRecorder;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.recipes.anvil.AnvilRecipe;
import lumien.randomthings.recipes.anvil.AnvilRecipeHandler;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import lumien.randomthings.tileentity.TileEntityRainShield;
import lumien.randomthings.util.EntityUtil;
import lumien.randomthings.util.InventoryUtil;
import lumien.randomthings.util.client.RenderUtils;
import lumien.randomthings.worldgen.WorldGenSakanade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RTEventHandler
{
	static Random rng = new Random();

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void soundSourceEvent(SoundSourceEvent event)
	{
		ItemStack currentlyEquipped;

		if (event.name != null && ModItems.soundRecorder!=null &&  Minecraft.getMinecraft().thePlayer != null && (currentlyEquipped = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem()) != null && currentlyEquipped.getItem() == ModItems.soundRecorder)
		{
			MessageSoundRecorder message = new MessageSoundRecorder(event.name);

			PacketHandler.INSTANCE.sendToServer(message);
		}
	}

	@SubscribeEvent
	public void biomeDecoration(DecorateBiomeEvent event)
	{
		if (event instanceof DecorateBiomeEvent.Pre)
		{

		}
		else if (event instanceof DecorateBiomeEvent.Post)
		{
			WorldGenSakanade.instance.generate(event.rand, event.pos.getX() >> 4, event.pos.getZ() >> 4, event.world, null, null);
		}
	}

	@SubscribeEvent
	public void explosionDetonate(ExplosionEvent.Detonate event)
	{
		Iterator<BlockPos> iterator = event.getAffectedBlocks().iterator();

		while (iterator.hasNext())
		{
			BlockPos pos = iterator.next();

			if (event.world.getBlockState(pos).getBlock() instanceof IExplosionImmune)
			{
				iterator.remove();
			}
		}
	}

	@SubscribeEvent
	public void tick(TickEvent tickEvent)
	{
		if ((tickEvent.type == TickEvent.Type.CLIENT || tickEvent.type == TickEvent.Type.SERVER) && tickEvent.phase == TickEvent.Phase.END)
		{
			TileEntityRainShield.rainCache.clear();
		}

		if (tickEvent instanceof WorldTickEvent)
		{
			WorldTickEvent worldTickEvent = (WorldTickEvent) tickEvent;

			if (worldTickEvent.phase == Phase.END && !worldTickEvent.world.isRemote && worldTickEvent.world.provider.getDimensionId() == 0)
			{
				RedstoneSignalHandler.getHandler().tick();
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderLiving(RenderLivingEvent.Pre<EntityLivingBase> event)
	{
		final EntityLivingBase entity = event.entity;

		if (event.entity.isPotionActive(ModPotions.boss))
		{
			IBossDisplayData displayData = new IBossDisplayData()
			{
				@Override
				public float getMaxHealth()
				{
					return entity.getMaxHealth();
				}

				@Override
				public float getHealth()
				{
					return entity.getHealth();
				}

				@Override
				public IChatComponent getDisplayName()
				{
					return entity.getDisplayName();
				}

			};
			BossStatus.setBossStatus(displayData, false);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void cameraSetup(CameraSetup event)
	{
		if (event.entity instanceof EntityLivingBase)
		{
			if (((EntityLivingBase) event.entity).isPotionActive(ModPotions.collapse))
			{
				event.roll = 180;
			}
		}
	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event)
	{
		if (!event.world.isRemote && event.action == Action.RIGHT_CLICK_BLOCK)
		{
			for (EnumFacing facing : EnumFacing.values())
			{
				BlockPos pos = event.pos.offset(facing);
				IBlockState state = event.world.getBlockState(pos);
				if (state.getBlock() == ModBlocks.contactButton)
				{
					if (state.getValue(BlockContactButton.FACING).getOpposite() == facing)
					{
						((BlockContactButton) state.getBlock()).activate(event.world, event.pos.offset(facing), facing.getOpposite());
						break;
					}
				}
				else if (state.getBlock() == ModBlocks.contactLever)
				{
					if (state.getValue(BlockContactLever.FACING).getOpposite() == facing)
					{
						((BlockContactLever) state.getBlock()).activate(event.world, event.pos.offset(facing), facing.getOpposite());
						break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void livingExperience(LivingExperienceDropEvent event)
	{
		if (event.getAttackingPlayer() != null && event.getAttackingPlayer().isPotionActive(ModPotions.imbueExperience))
		{
			event.setDroppedExperience(event.getDroppedExperience() + event.getOriginalExperience());
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void modelBake(ModelBakeEvent event)
	{
		ModelFluidDisplay modelFluidDisplay = new ModelFluidDisplay();
		event.modelRegistry.putObject(new ModelResourceLocation("randomthings:fluidDisplay", "normal"), modelFluidDisplay);
		event.modelRegistry.putObject(new ModelResourceLocation("randomthings:fluidDisplay", "inventory"), modelFluidDisplay);

		ModelCustomWorkbench modelCustomWorkbench = new ModelCustomWorkbench();
		event.modelRegistry.putObject(new ModelResourceLocation("randomthings:customWorkbench", "normal"), modelCustomWorkbench);
		event.modelRegistry.putObject(new ModelResourceLocation("randomthings:customWorkbench", "inventory"), modelCustomWorkbench);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void textureStitch(TextureStitchEvent.Pre event)
	{
		try
		{
			ASMDataTable asmData = RandomThings.instance.getASMData();

			Set<ASMData> atlasSet = asmData.getAll(AtlasSprite.class.getName());

			for (ASMData data : atlasSet)
			{
				Class clazz = Class.forName(data.getClassName());
				Field f = clazz.getDeclaredField(data.getObjectName());
				f.setAccessible(true);
				ResourceLocation rl = new ResourceLocation((String) data.getAnnotationInfo().get("resource"));

				f.set(null, event.map.registerSprite(rl));
			}
		}
		catch (Exception e)
		{
			RandomThings.instance.logger.log(Level.ERROR, "Error stitching extra textures");
			e.printStackTrace();
		}
	}

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
		if (event.type != null && event instanceof RenderGameOverlayEvent.Post)
		{
			if (event.type == RenderGameOverlayEvent.ElementType.ARMOR)
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

						int power = hitState.getValue(BlockRedstoneWire.POWER);

						Minecraft.getMinecraft().fontRendererObj.drawString(power + "", width / 2 + 5, height / 2 + 5, Colors.RED_INT);
						GlStateManager.color(1, 1, 1, 1);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void anvilUpdate(AnvilUpdateEvent event)
	{
		if (event.left != null && event.right != null)
		{
			AnvilRecipe recipe = AnvilRecipeHandler.getRecipe(event.left, event.right);

			if (recipe != null)
			{
				event.output = recipe.getOutput();
				event.cost = recipe.getCost();
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

				int top = height - GuiIngameForge.left_height - 1;
				GuiIngameForge.left_height += 10;

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
			if (!event.isCanceled() && event.ammount > 0 && event.entityLiving instanceof EntityPlayerMP)
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
					else if (livingEntity.isPotionActive(ModPotions.imbueCollapse))
					{
						if (Math.random() < 0.2f)
						{
							event.entityLiving.addPotionEffect(new PotionEffect(ModPotions.collapse.id, 10 * 20, 1));
						}
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

				if (player.dimension == ModDimensions.SPECTRE_ID)
				{
					SpectreHandler spectreHandler;

					if ((spectreHandler = SpectreHandler.getInstance()) != null)
					{
						spectreHandler.checkPosition((EntityPlayerMP) player);
					}
				}
			}
		}
		else
		{
			spawnEntityFilterParticles(event);

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

	@SideOnly(Side.CLIENT)
	private void spawnEntityFilterParticles(LivingUpdateEvent event)
	{
		EntityPlayer thePlayer = Minecraft.getMinecraft().thePlayer;

		ItemStack equipped = thePlayer.getCurrentEquippedItem();

		if (equipped != null && equipped.getItem() == ModItems.entityFilter)
		{
			if (ItemEntityFilter.filterAppliesTo(equipped, event.entityLiving))
			{
				for (int i = 0; i < 1; ++i)
				{
					EntityFX fx = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.PORTAL.ordinal(), event.entityLiving.posX + (RTEventHandler.rng.nextDouble() - 0.5D) * event.entityLiving.width, event.entityLiving.posY + RTEventHandler.rng.nextDouble() * event.entityLiving.height - 0.25D, event.entityLiving.posZ + (RTEventHandler.rng.nextDouble() - 0.5D) * event.entityLiving.width, (RTEventHandler.rng.nextDouble() - 0.5D) * 2.0D, -RTEventHandler.rng.nextDouble(), (RTEventHandler.rng.nextDouble() - 0.5D) * 2.0D);
					fx.setRBGColorF(0.2F, 0.2F, 1);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent event)
	{
		if (!event.entityLiving.worldObj.isRemote)
		{
			if (event.entityLiving instanceof EntityDragon)
			{
				RTWorldInformation rtInfo = RTWorldInformation.getInstance();
				if (rtInfo != null)
				{
					rtInfo.setEnderDragonDefeated(true);
				}
			}

			if (event.source.getEntity() != null && !(event.source.getEntity() instanceof FakePlayer) && event.source.getEntity() instanceof EntityPlayer && !(event.entity instanceof EntitySpirit))
			{
				double chance = Numbers.SPIRIT_CHANCE_NORMAL;

				RTWorldInformation rtInfo = RTWorldInformation.getInstance();

				if (rtInfo != null)
				{
					if (rtInfo.isDragonDefeated())
					{
						chance += Numbers.SPIRIT_CHANCE_END_INCREASE;
					}
				}

				if (event.entityLiving.worldObj.canBlockSeeSky(event.entityLiving.getPosition()) && !event.entityLiving.worldObj.isDaytime())
				{
					chance += event.entityLiving.worldObj.getCurrentMoonPhaseFactor() / 100f * Numbers.SPIRIT_CHANCE_MOON_MULT;
				}

				if (Math.random() < chance)
				{
					event.entityLiving.worldObj.spawnEntityInWorld(new EntitySpirit(event.entityLiving.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
				}
			}

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
							RenderUtils.drawCube(filterX - 0.01F, filterY - 0.01F, filterZ - 0.01F, 1.02f, 102, 0, 255, 51);
						}
						GlStateManager.popMatrix();
						GlStateManager.disableBlend();
					}
				}
			}
		}
	}
}
