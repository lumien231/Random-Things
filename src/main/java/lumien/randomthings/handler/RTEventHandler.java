package lumien.randomthings.handler;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockContactButton;
import lumien.randomthings.block.BlockContactLever;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.client.models.blocks.ModelCustomWorkbench;
import lumien.randomthings.client.models.blocks.ModelFluidDisplay;
import lumien.randomthings.config.Numbers;
import lumien.randomthings.entitys.EntitySoul;
import lumien.randomthings.entitys.EntitySpirit;
import lumien.randomthings.handler.magicavoxel.ClientModelLibrary;
import lumien.randomthings.handler.magicavoxel.ServerModelLibrary;
import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import lumien.randomthings.handler.spectre.SpectreHandler;
import lumien.randomthings.item.ItemEntityFilter;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.AtlasSprite;
import lumien.randomthings.lib.Colors;
import lumien.randomthings.lib.IExplosionImmune;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.recipes.anvil.AnvilRecipe;
import lumien.randomthings.recipes.anvil.AnvilRecipeHandler;
import lumien.randomthings.tileentity.TileEntityChatDetector;
import lumien.randomthings.tileentity.TileEntityRainShield;
import lumien.randomthings.tileentity.TileEntityRedstoneObserver;
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
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RTEventHandler
{
	static Random rng = new Random();
	
	public static int clientAnimationCounter;

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientConnectingToServer(ClientConnectedToServerEvent event)
	{
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				ClientModelLibrary.getInstance().reset();
			}
		});

	}

	@SubscribeEvent
	public void loadLootTable(LootTableLoadEvent event)
	{
		LootTable table = event.getTable();
		if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON))
		{
			addSingleItemWithChance("lavaCharm", table, ModItems.lavaCharm, 0.02f);
			addSingleItemWithChance("summoningPendulum", table, ModItems.summoningPendulum, 0.02f);
			addSingleItemWithChance("magicHood", table, ModItems.magicHood, 0.03f);
		}
		else if (event.getName().equals(LootTableList.CHESTS_NETHER_BRIDGE))
		{
			addSingleItemWithChance("lavaCharm", table, ModItems.lavaCharm, 0.2f);
		}
		else if (event.getName().equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH))
		{
			addSingleItemWithChance("magicHood", table, ModItems.magicHood, 0.02f);
		}
		else if (event.getName().equals(LootTableList.CHESTS_STRONGHOLD_CORRIDOR))
		{
			addSingleItemWithChance("summoningPendulum", table, ModItems.summoningPendulum, 0.5f);
		}

		if (event.getName().toString().startsWith("minecraft:chests/"))
		{
			LootEntry crystalEntry = new LootEntryItem(ModItems.biomeCrystal, 1, 0, new LootFunction[] { new LootFunction(new LootCondition[] {})
			{
				@Override
				public ItemStack apply(ItemStack stack, Random rand, LootContext context)
				{
					Object[] locationArray =  Biome.REGISTRY.getKeys().toArray();
					ResourceLocation randomLocation = (ResourceLocation) locationArray[rand.nextInt(locationArray.length)];

					stack.setTagCompound(new NBTTagCompound());
					stack.getTagCompound().setString("biomeName", randomLocation.toString());

					return stack;
				}
			} }, new LootCondition[] {}, "randomthings:biomeCrystal");

			LootPool crystalPool = new LootPool(new LootEntry[] { crystalEntry }, new LootCondition[] { new RandomChance(0.2f) }, new RandomValueRange(1, 1), new RandomValueRange(0, 0), "randomthings:biomeCrystal");
			table.addPool(crystalPool);
		}
	}

	private void addSingleItemWithChance(String name, LootTable table, Item item, float chance)
	{
		table.addPool(new LootPool(new LootEntry[] { new LootEntryItem(item, 1, 0, new LootFunction[] {}, new LootCondition[] {}, "randomthings:" + name) }, new LootCondition[] { new RandomChance(chance) }, new RandomValueRange(1, 1), new RandomValueRange(0, 0), "randomthings:" + name));

	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event)
	{
		if (event.isWasDeath() && !event.getEntityPlayer().worldObj.getGameRules().getBoolean("keepInventory"))
		{
			EntityPlayer oldPlayer = event.getOriginal();
			EntityPlayer newPlayer = event.getEntityPlayer();

			for (int i = 0; i < oldPlayer.inventory.getSizeInventory(); i++)
			{
				ItemStack is = oldPlayer.inventory.getStackInSlot(i);

				if (is != null && is.hasTagCompound() && is.getTagCompound().hasKey("spectreAnchor"))
				{
					ItemStack newIs = newPlayer.inventory.getStackInSlot(i);

					if (newIs == null)
					{
						newPlayer.inventory.setInventorySlotContents(i, is.copy());
					}
				}
			}
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
			WorldGenSakanade.instance.generate(event.getRand(), event.getPos().getX() >> 4, event.getPos().getZ() >> 4, event.getWorld(), null, null);
		}
	}

	@SubscribeEvent
	public void explosionDetonate(ExplosionEvent.Detonate event)
	{
		Iterator<BlockPos> iterator = event.getAffectedBlocks().iterator();

		while (iterator.hasNext())
		{
			BlockPos pos = iterator.next();

			if (event.getWorld().getBlockState(pos).getBlock() instanceof IExplosionImmune)
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
		
		if ((tickEvent.type == TickEvent.Type.CLIENT))
		{
			clientAnimationCounter++;
		}

		if (tickEvent instanceof ServerTickEvent)
		{
			ServerModelLibrary.getInstance().tick();
		}

		if (tickEvent instanceof WorldTickEvent)
		{
			WorldTickEvent worldTickEvent = (WorldTickEvent) tickEvent;

			if (worldTickEvent.phase == Phase.END && !worldTickEvent.world.isRemote && worldTickEvent.world.provider.getDimension() == 0)
			{
				RedstoneSignalHandler.getHandler().tick();
			}
		}
	}

	@SubscribeEvent
	public void notifyNeighbors(NeighborNotifyEvent event)
	{
		TileEntityRedstoneObserver.notifyNeighbor(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void cameraSetup(CameraSetup event)
	{
		if (event.getEntity() instanceof EntityLivingBase)
		{
			if (((EntityLivingBase) event.getEntity()).isPotionActive(ModPotions.collapse))
			{
				event.setRoll(180);
			}
		}

	}

	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event)
	{
		if (event instanceof RightClickBlock)
		{
			ItemStack equipped = event.getEntityPlayer().getHeldItem(event.getHand());

			if (equipped != null && equipped.getItem() instanceof ItemSpade)
			{
				RightClickBlock rcEvent = (RightClickBlock) event;
				IBlockState targetState = event.getWorld().getBlockState(event.getPos());

				if (targetState.getBlock() == Blocks.SLIME_BLOCK)
				{
					event.getEntityPlayer().swingArm(event.getHand());
					if (!event.getWorld().isRemote)
					{
						if (!event.getWorld().isRemote)
						{
							event.getWorld().setBlockState(event.getPos(), ModBlocks.compressedSlimeBlock.getDefaultState());
							event.getWorld().playSound(null, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), Blocks.SLIME_BLOCK.getSoundType().getPlaceSound(), SoundCategory.PLAYERS, 1, 1);
							equipped.damageItem(1, event.getEntityPlayer());
						}
					}
				}
			}

			if (!event.getWorld().isRemote && event.getHand() == EnumHand.MAIN_HAND)
			{
				for (EnumFacing facing : EnumFacing.values())
				{
					BlockPos pos = event.getPos().offset(facing);
					IBlockState state = event.getWorld().getBlockState(pos);
					if (state.getBlock() == ModBlocks.contactButton)
					{
						if (state.getValue(BlockContactButton.FACING).getOpposite() == facing)
						{
							((BlockContactButton) state.getBlock()).activate(event.getWorld(), event.getPos().offset(facing), facing.getOpposite());
							break;
						}
					}
					else if (state.getBlock() == ModBlocks.contactLever)
					{
						if (state.getValue(BlockContactLever.FACING).getOpposite() == facing)
						{
							((BlockContactLever) state.getBlock()).activate(event.getWorld(), event.getPos().offset(facing), facing.getOpposite());
							break;
						}
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
		event.getModelRegistry().putObject(new ModelResourceLocation("randomthings:fluidDisplay", "normal"), modelFluidDisplay);
		event.getModelRegistry().putObject(new ModelResourceLocation("randomthings:fluidDisplay", "inventory"), modelFluidDisplay);

		ModelCustomWorkbench modelCustomWorkbench = new ModelCustomWorkbench();
		event.getModelRegistry().putObject(new ModelResourceLocation("randomthings:customWorkbench", "normal"), modelCustomWorkbench);
		event.getModelRegistry().putObject(new ModelResourceLocation("randomthings:customWorkbench", "inventory"), modelCustomWorkbench);
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

				f.set(null, event.getMap().registerSprite(rl));
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
				if (chatDetector.checkMessage(event.getUsername(), event.getMessage()))
				{
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void itemTooltip(ItemTooltipEvent event)
	{
		if (event.getItemStack().hasTagCompound() && event.getItemStack().getTagCompound().hasKey("spectreAnchor"))
		{
			event.getToolTip().add(1, TextFormatting.DARK_AQUA.toString() + I18n.format("tooltip.spectreAnchor.item") + TextFormatting.RESET.toString());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderGameOverlay(RenderGameOverlayEvent event)
	{
		if (event.getType() != null && event instanceof RenderGameOverlayEvent.Post)
		{
			if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR)
			{
				renderLavaCharm(event);
			}
			else if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			{
				renderRedstoneTool(event);
			}
		}
	}

	private void renderRedstoneTool(RenderGameOverlayEvent event)
	{
		ItemStack equippedItem;

		Minecraft minecraft = Minecraft.getMinecraft();

		if ((equippedItem = minecraft.thePlayer.getHeldItemMainhand()) != null)
		{
			if (equippedItem.getItem() == ModItems.redstoneTool)
			{
				RayTraceResult objectMouseOver = minecraft.objectMouseOver;

				if (objectMouseOver != null && objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
				{
					IBlockState hitState = minecraft.theWorld.getBlockState(objectMouseOver.getBlockPos());
					Block hitBlock = hitState.getBlock();

					if (hitBlock instanceof BlockRedstoneWire)
					{
						int width = event.getResolution().getScaledWidth();
						int height = event.getResolution().getScaledHeight();

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
		if (event.getLeft() != null && event.getRight() != null)
		{
			AnvilRecipe recipe = AnvilRecipeHandler.getRecipe(event.getLeft(), event.getRight());

			if (recipe != null)
			{
				event.setOutput(recipe.getOutput());
				event.setCost(recipe.getCost());
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

		ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
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

				int width = event.getResolution().getScaledWidth();
				int height = event.getResolution().getScaledHeight();

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
				mc.renderEngine.bindTexture(Gui.ICONS);
				GlStateManager.disableBlend();
			}
		}
	}

	private void handleLavaProtection(LivingAttackEvent event)
	{
		ItemStack lavaProtector = null;
		ItemStack lavaCharm = null;

		lavaCharm = InventoryUtil.getBauble(ModItems.lavaCharm, (EntityPlayer) event.getEntityLiving());

		if (lavaCharm == null)
		{
			lavaCharm = InventoryUtil.getPlayerInventoryItem(ModItems.lavaCharm, (EntityPlayer) event.getEntityLiving());
		}

		if (lavaCharm != null)
		{
			lavaProtector = lavaCharm;
		}

		ItemStack boots = event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.FEET);
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
		if (!event.getEntityLiving().worldObj.isRemote)
		{
			if (!event.isCanceled() && event.getAmount() > 0 && event.getEntityLiving() instanceof EntityPlayerMP)
			{
				EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

				if (event.getSource() == DamageSource.lava)
				{
					handleLavaProtection(event);
				}

				if (event.getSource().isFireDamage() && event.getSource() != DamageSource.lava)
				{
					handleFireProtection(event);
				}
			}

			if (!event.isCanceled() && event.getSource() instanceof EntityDamageSource && !(event.getSource() instanceof EntityDamageSourceIndirect))
			{
				EntityDamageSource damageSource = (EntityDamageSource) event.getSource();

				if (damageSource.getEntity() != null && damageSource.getEntity() instanceof EntityLivingBase)
				{
					EntityLivingBase livingEntity = (EntityLivingBase) damageSource.getEntity();

					if (livingEntity.isPotionActive(ModPotions.imbueFire))
					{
						event.getEntityLiving().setFire(10);
					}
					else if (livingEntity.isPotionActive(ModPotions.imbueWither))
					{
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WITHER, 5 * 20, 1));
					}
					else if (livingEntity.isPotionActive(ModPotions.imbuePoison))
					{
						event.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON, 10 * 20, 1));
					}
					else if (livingEntity.isPotionActive(ModPotions.imbueCollapse))
					{
						if (Math.random() < 0.2f)
						{
							event.getEntityLiving().addPotionEffect(new PotionEffect(ModPotions.collapse, 10 * 20, 1));
						}
					}
				}
			}
		}
	}

	private void handleFireProtection(LivingAttackEvent event)
	{
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		ItemStack baubleSkull = InventoryUtil.getBauble(ModItems.obsidianSkullRing, player);
		ItemStack inventorySkull = InventoryUtil.getPlayerInventoryItem(ModItems.obsidianSkull, player);
		ItemStack obsidianBoots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

		if (obsidianBoots != null && !(obsidianBoots.getItem() == ModItems.obsidianWaterWalkingBoots || obsidianBoots.getItem() == ModItems.lavaWader))
		{
			obsidianBoots = null;
		}

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
			float amount = event.getAmount();
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
		if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.fertilizedDirt)
		{
			event.setResult(Result.ALLOW);
			event.getWorld().setBlockState(event.getPos(), ModBlocks.fertilizedDirtTilled.getDefaultState());
			event.getWorld().playSound(null, event.getPos().add(0.5, 0.5, 0.5), ModBlocks.fertilizedDirtTilled.getSoundType().getStepSound(), SoundCategory.BLOCKS, (ModBlocks.fertilizedDirtTilled.getSoundType().getVolume() + 1.0F) / 2.0F, ModBlocks.fertilizedDirtTilled.getSoundType().getPitch() * 0.8F);
		}
	}

	@SubscribeEvent
	public void livingUpdate(LivingUpdateEvent event)
	{
		if (!event.getEntityLiving().worldObj.isRemote)
		{
			if (event.getEntityLiving() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
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

			if (event.getEntityLiving() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				if (!player.isSneaking())
				{
					ItemStack boots = player.inventory.armorInventory[0];
					if (boots != null && ((boots.getItem() == ModItems.waterWalkingBoots || boots.getItem() == ModItems.obsidianWaterWalkingBoots) || boots.getItem() == ModItems.lavaWader))
					{
						BlockPos liquid = new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
						BlockPos air = new BlockPos((int) player.posX, (int) (player.posY + player.height), (int) player.posZ);
						Block liquidBlock = player.worldObj.getBlockState(liquid).getBlock();
						Material liquidMaterial = liquidBlock.getMaterial(player.worldObj.getBlockState(liquid));

						if ((liquidMaterial == Material.WATER || (boots.getItem() == ModItems.lavaWader && liquidMaterial == Material.LAVA)) && player.worldObj.getBlockState(air).getBlock().isAir(player.worldObj.getBlockState(air), player.worldObj, air) && EntityUtil.isJumping(player))
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

		ItemStack equipped = thePlayer.getHeldItemMainhand();

		if (equipped != null && equipped.getItem() == ModItems.entityFilter)
		{
			if (ItemEntityFilter.filterAppliesTo(equipped, event.getEntityLiving()))
			{
				for (int i = 0; i < 1; ++i)
				{
					Particle particle = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.PORTAL.ordinal(), event.getEntityLiving().posX + (RTEventHandler.rng.nextDouble() - 0.5D) * event.getEntityLiving().width, event.getEntityLiving().posY + RTEventHandler.rng.nextDouble() * event.getEntityLiving().height - 0.25D, event.getEntityLiving().posZ + (RTEventHandler.rng.nextDouble() - 0.5D) * event.getEntityLiving().width, (RTEventHandler.rng.nextDouble() - 0.5D) * 2.0D, -RTEventHandler.rng.nextDouble(), (RTEventHandler.rng.nextDouble() - 0.5D) * 2.0D);
					particle.setRBGColorF(0.2F, 0.2F, 1);
				}
			}
		}
	}

	@SubscribeEvent
	public void livingDeath(LivingDeathEvent event)
	{
		if (!event.getEntityLiving().worldObj.isRemote)
		{
			if (event.getEntityLiving() instanceof EntityDragon)
			{
				RTWorldInformation rtInfo = RTWorldInformation.getInstance();
				if (rtInfo != null)
				{
					rtInfo.setEnderDragonDefeated(true);
				}
			}

			if (event.getSource().getEntity() != null && !(event.getSource().getEntity() instanceof FakePlayer) && event.getSource().getEntity() instanceof EntityPlayer && !(event.getEntity() instanceof EntitySpirit))
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

				if (event.getEntityLiving().worldObj.canBlockSeeSky(event.getEntityLiving().getPosition()) && !event.getEntityLiving().worldObj.isDaytime())
				{
					chance += event.getEntityLiving().worldObj.getCurrentMoonPhaseFactor() / 100f * Numbers.SPIRIT_CHANCE_MOON_MULT;
				}

				if (Math.random() < chance)
				{
					event.getEntityLiving().worldObj.spawnEntityInWorld(new EntitySpirit(event.getEntityLiving().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ));
				}
			}

			if (event.getEntityLiving() instanceof EntityPlayer)
			{
				if (!(event.getEntityLiving() instanceof FakePlayer))
				{
					EntityPlayer player = (EntityPlayer) event.getEntityLiving();

					if (!event.isCanceled())
					{
						player.worldObj.spawnEntityInWorld(new EntitySoul(player.worldObj, player.posX, player.posY, player.posZ, player.getGameProfile().getName()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderWorldPost(RenderWorldLastEvent event)
	{
		RandomThings.proxy.renderRedstoneInterfaceStuff(event.getPartialTicks());

		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		if (player != null)
		{
			ItemStack itemStack = player.getHeldItemMainhand();

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
						double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
						double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
						double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();

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
