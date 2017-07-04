package lumien.randomthings.handler;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.enchantment.ModEnchantments;
import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import lumien.randomthings.item.ItemRedstoneTool;
import lumien.randomthings.item.ItemSpectreKey;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.item.spectretools.ItemSpectreSword;
import lumien.randomthings.lib.ISuperLubricent;
import lumien.randomthings.potion.ModPotions;
import lumien.randomthings.tileentity.TileEntityLightRedirector;
import lumien.randomthings.tileentity.TileEntityPeaceCandle;
import lumien.randomthings.tileentity.TileEntityRainShield;
import lumien.randomthings.tileentity.TileEntitySlimeCube;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityRedstoneInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class AsmHandler
{
	static Random rng = new Random();

	static Field fluidRenderer;
	static
	{
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			getFields();
		}
	}

	public static void modifyValidSpawningChunks(EnumCreatureType creatureType, List<ChunkPos> positions)
	{
		if (creatureType == EnumCreatureType.MONSTER)
		{
			Set<ChunkPos> toRemove = new HashSet<ChunkPos>();

			synchronized (TileEntityPeaceCandle.candles)
			{
				for (TileEntityPeaceCandle pc : TileEntityPeaceCandle.candles)
				{
					if (!pc.isInvalid())
					{
						int cX = pc.getPos().getX() >> 4;
						int cZ = pc.getPos().getZ() >> 4;

						for (int mX = -3; mX < 4; mX++)
						{
							for (int mZ = -3; mZ < 4; mZ++)
							{
								toRemove.add(new ChunkPos(cX + mX, cZ + mZ));
							}
						}
					}
				}
			}

			if (!toRemove.isEmpty())
			{
				Iterator<ChunkPos> iterator = positions.iterator();

				while (iterator.hasNext())
				{
					ChunkPos pos = iterator.next();

					if (toRemove.remove(pos))
					{
						iterator.remove();
					}
				}
			}
		}
	}

	// -1 = vanilla, 0 = NO, 1 = YES
	public static int overrideSlimeChunk(World worldObj, Chunk chunk)
	{
		synchronized (TileEntitySlimeCube.cubes)
		{
			for (TileEntitySlimeCube core : TileEntitySlimeCube.cubes)
			{
				if (!core.isInvalid() && core.getWorld() == worldObj)
				{
					BlockPos pos = core.getPos();
					int chunkX = pos.getX() >> 4;
					int chunkZ = pos.getZ() >> 4;

					if (chunk.x == chunkX && chunk.z == chunkZ)
					{
						return core.isRedstonePowered() ? 0 : 1;
					}
				}
			}
		}
		return -1;
	}

	public static double getPlayerRange(double original, EntityPlayer player)
	{
		return getPlayerRange((float) original, player);
	}

	public static float getPlayerRange(float original, EntityPlayer player)
	{
		if (player != null)
		{
			ItemStack holdingMain = player.getHeldItemMainhand();
			ItemStack holdingOff = player.getHeldItemOffhand();

			if (holdingMain.getItem() == ModItems.spectrePickaxe || holdingMain.getItem() == ModItems.spectreAxe || holdingMain.getItem() == ModItems.spectreShovel)
			{
				return original + 3;
			}


		}

		return original;
	}

	public static void updateColor(float[] normal, float[] color, float x, float y, float z, float tint, int multiplier)
	{
		if (tint != -1)
		{
			color[0] *= (float) (multiplier >> 0x10 & 0xFF) / 0xFF;
			color[1] *= (float) (multiplier >> 0x8 & 0xFF) / 0xFF;
			color[2] *= (float) (multiplier & 0xFF) / 0xFF;
		}
	}

	// Called when a tree tries to set the block below it to dirt, returning
	// true prevents that from happening
	public static boolean protectGround(Block b)
	{
		return b == ModBlocks.fertilizedDirt || b == ModBlocks.fertilizedDirtTilled;
	}

	static PlayerInteractionManager interactionManager;

	public static void preHarvest(PlayerInteractionManager manager)
	{
		ItemStack tool = manager.player.getHeldItemMainhand();

		if (tool != null && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.magnetic, tool) > 0)
		{
			ItemCatcher.startCatching();
			interactionManager = manager;
		}
	}

	public static void postHarvest()
	{
		if (ItemCatcher.isCatching() && interactionManager != null)
		{
			for (ItemStack is : ItemCatcher.stopCatching())
			{
				ItemHandlerHelper.giveItemToPlayer(interactionManager.player, is.copy());
			}

			interactionManager = null;
		}
	}

	public static boolean shouldPlayerDrop(InventoryPlayer inventory, int slot, ItemStack item)
	{
		return !(item.hasTagCompound() && item.getTagCompound().hasKey("spectreAnchor"));
	}

	@SideOnly(Side.CLIENT)
	private static void getFields()
	{
		try
		{
			fluidRenderer = BlockRendererDispatcher.class.getDeclaredField(MCPNames.field("field_175025_e"));
			fluidRenderer.setAccessible(true);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	public static int getColorFromItemStack(ItemStack is, int originalColor)
	{
		if (!is.isEmpty())
		{
			NBTTagCompound compound;
			if ((compound = is.getTagCompound()) != null)
			{
				if (compound.hasKey("rtDye"))
				{
					return compound.getInteger("rtDye") | -16777216;
				}
			}
		}
		return originalColor;
	}

	public static boolean shouldRain(World worldObj, BlockPos pos)
	{
		return TileEntityRainShield.shouldRain(worldObj, pos.add(0, -pos.getY(), 0));
	}

	public static boolean shouldRenderPotionParticles(EntityLivingBase entity)
	{
		if (entity != null && entity instanceof EntityPlayer)
		{
			ItemStack helmet = ((EntityPlayer) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if (helmet != null && helmet.getItem() == ModItems.magicHood)
			{
				return false;
			}
		}
		return true;
	}

	// False returns false, true runs vanilla behaviour
	@SideOnly(Side.CLIENT)
	public static boolean canRenderName(EntityLivingBase e)
	{
		if (e != null && e instanceof EntityPlayer)
		{
			ItemStack helmet = ((EntityPlayer) e).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if (helmet != null && helmet.getItem() == ModItems.magicHood)
			{
				return false;
			}
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public static int renderBlock(BlockRendererDispatcher dispatcher, IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder worldRendererIn)
	{
		synchronized (TileEntityLightRedirector.redirectorSet)
		{
			if (!TileEntityLightRedirector.redirectorSet.isEmpty())
			{
				blockAccess = Minecraft.getMinecraft().world;

				BlockPos changedPos = getSwitchedPosition(blockAccess, pos);

				posSet.clear();

				if (!changedPos.equals(pos))
				{
					state = blockAccess.getBlockState(changedPos);

					try
					{
						EnumBlockRenderType enumblockrendertype = state.getRenderType();

						if (enumblockrendertype == EnumBlockRenderType.INVISIBLE)
						{

						}
						else
						{
							if (blockAccess.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
							{
								try
								{
									state = state.getActualState(blockAccess, changedPos);
								}
								catch (Exception var8)
								{
									;
								}
							}

							switch (enumblockrendertype)
							{
								case MODEL:
									IBakedModel model = dispatcher.getModelForState(state);
									state = state.getBlock().getExtendedState(state, blockAccess, changedPos);
									return dispatcher.getBlockModelRenderer().renderModel(blockAccess, model, state, pos, worldRendererIn, true) ? 1 : 0;
								case ENTITYBLOCK_ANIMATED:
									return 0;
								case LIQUID:
									return 2;
								default:
									return 0;
							}
						}
					}
					catch (Throwable throwable)
					{
						CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
						CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
						CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
						throw new ReportedException(crashreport);
					}

					return 0;
				}
			}

			return 2;
		}
	}

	static HashSet<BlockPos> posSet = new HashSet<>();

	public static BlockPos getSwitchedPosition(IBlockAccess access, BlockPos pos)
	{
		if (pos != null && access != null)
		{
			Iterator<TileEntityLightRedirector> iterator = TileEntityLightRedirector.redirectorSet.iterator();
			while (iterator.hasNext())
			{
				TileEntityLightRedirector redirector = iterator.next();
				if (redirector.isInvalid())
				{
					iterator.remove();
				}
				else
				{
					if (redirector.established && !posSet.contains(redirector.getPos()))
					{
						posSet.add(redirector.getPos());

						if (redirector.targets.isEmpty())
						{
							for (EnumFacing facing : EnumFacing.values())
							{
								redirector.targets.put(redirector.getPos().offset(facing), redirector.getPos().offset(facing.getOpposite()));
							}
						}

						if (redirector.targets.containsKey(pos))
						{
							BlockPos switched = redirector.targets.get(pos);

							if (!access.isAirBlock(switched))
							{
								return getSwitchedPosition(access, switched);
							}
						}
					}
				}
			}
		}

		return pos;
	}

	public static int getRedstonePower(World worldObj, BlockPos pos, EnumFacing facing)
	{
		return Math.max(TileEntityRedstoneInterface.getRedstonePower(worldObj, pos, facing), worldObj.isRemote ? 0 : RedstoneSignalHandler.getHandler().getStrongPower(worldObj, pos, facing));
	}

	public static int getStrongPower(World worldObj, BlockPos pos, EnumFacing facing)
	{
		return Math.max(TileEntityRedstoneInterface.getStrongPower(worldObj, pos, facing), worldObj.isRemote ? 0 : RedstoneSignalHandler.getHandler().getStrongPower(worldObj, pos, facing));
	}

	// Returns whether to cancel normal behaviour
	public static boolean addCollisionBoxesToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		if (collidingEntity != null && collidingEntity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) collidingEntity;

			if (state.getBlock() instanceof BlockLiquid && collidingEntity.posY > pos.getY() + 0.9 && !(worldIn.getBlockState(pos.up()).getBlock().getMaterial(worldIn.getBlockState(pos.up())) == Material.LAVA || worldIn.getBlockState(pos.up()).getBlock().getMaterial(worldIn.getBlockState(pos.up())) == Material.WATER))
			{
				if (!player.isSneaking())
				{
					ItemStack boots = player.inventory.armorInventory.get(0);
					if (boots != null && ((((boots.getItem() == ModItems.waterWalkingBoots || boots.getItem() == ModItems.obsidianWaterWalkingBoots) || boots.getItem() == ModItems.lavaWader) && state.getBlock().getMaterial(state) == Material.WATER) || (boots.getItem() == ModItems.lavaWader && state.getBlock().getMaterial(state) == Material.LAVA)))
					{
						AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 1, (double) pos.getZ() + 1);
						if (mask.intersects(bb))
						{
							list.add(bb);
						}
						return true;
					}
				}
			}
		}

		return false;
	}

	static float enchantmentLightMapX;
	static float enchantmentLightMapY;

	public static void preEnchantment()
	{
		if (currentlyRendering != null && currentlyRendering.hasTagCompound() && currentlyRendering.getTagCompound().hasKey("luminousEnchantment"))
		{
			enchantmentLightMapX = OpenGlHelper.lastBrightnessX;
			enchantmentLightMapY = OpenGlHelper.lastBrightnessY;

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		}
	}

	public static void postEnchantment()
	{
		if (currentlyRendering != null && currentlyRendering.hasTagCompound() && currentlyRendering.getTagCompound().hasKey("luminousEnchantment"))
		{
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, enchantmentLightMapX, enchantmentLightMapY);
		}
	}

	public static ItemStack currentlyRendering = null;

	public static int enchantmentColorHook()
	{
		if (currentlyRendering != null)
		{
			if (currentlyRendering.getItem() instanceof ItemRedstoneTool)
			{
				return Color.RED.darker().getRGB() | -16777216;
			}

			if (currentlyRendering.getItem() instanceof ItemSpectreKey)
			{
				return Color.CYAN.darker().getRGB() | -16777216;
			}

			NBTTagCompound compound;
			if ((compound = currentlyRendering.getTagCompound()) != null)
			{
				if (compound.hasKey("enchantmentColor"))
				{
					return compound.getInteger("enchantmentColor") | -16777216;
				}
			}

			if (currentlyRendering.getItem() instanceof ItemSpectreSword)
			{
				return Color.WHITE.darker().darker().getRGB() | -16777216;
			}
		}

		return -8372020;
	}

	public static void armorColorHook(ItemStack stack)
	{
		NBTTagCompound compound;
		if ((compound = stack.getTagCompound()) != null)
		{
			if (compound.hasKey("rtDye"))
			{
				Color c = new Color(compound.getInteger("rtDye"));

				GlStateManager.color(1F / 255F * c.getRed(), 1F / 255F * c.getGreen(), 1F / 255F * c.getBlue());
			}
		}
	}

	public static void armorEnchantmentHook()
	{
		int color = enchantmentColorHook();

		if (color != -8372020)
		{
			Color c = new Color(color);
			c = c.darker();
			GlStateManager.color(1F / 255F * c.getRed(), 1F / 255F * c.getGreen(), 1F / 255F * c.getBlue());
		}
	}

	static Block changed;

	public static void preSlipFix(Block b)
	{
		if (b instanceof ISuperLubricent)
		{
			b.slipperiness = 1F / 0.91F;
			changed = b;
		}
	}

	public static void postSlipFix()
	{
		if (changed != null)
		{
			changed.slipperiness = 1F / 0.98F;
			changed = null;
		}
	}

	public static void modifyInput(MovementInputFromOptions input)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		PotionEffect effect;
		if (player != null && (effect = player.getActivePotionEffect(ModPotions.collapse)) != null && effect.getAmplifier() == 0)
		{
			boolean left = input.leftKeyDown;
			boolean right = input.rightKeyDown;

			boolean forward = input.forwardKeyDown;
			boolean backwards = input.backKeyDown;

			if (left)
			{
				input.moveStrafe -= 2;
				input.leftKeyDown = false;
				input.rightKeyDown = true;
			}

			if (right)
			{
				input.moveStrafe += 2;
				input.rightKeyDown = false;
				input.leftKeyDown = true;
			}

			if (forward)
			{
				input.moveForward -= 2;
				input.forwardKeyDown = false;
				input.backKeyDown = true;
			}

			if (backwards)
			{
				input.moveForward += 2;
				input.backKeyDown = false;
				input.forwardKeyDown = true;
			}
		}
	}
}
