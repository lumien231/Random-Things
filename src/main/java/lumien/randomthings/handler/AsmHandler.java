package lumien.randomthings.handler;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.block.BlockSpecialChest;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Features;
import lumien.randomthings.handler.compability.chisel.ChiselModelWrapper;
import lumien.randomthings.handler.redstonesignal.RedstoneSignalHandler;
import lumien.randomthings.item.ItemRedstoneTool;
import lumien.randomthings.item.ItemSpectreKey;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.tileentity.TileEntityRainShield;
import lumien.randomthings.tileentity.TileEntitySpecialChest;
import lumien.randomthings.tileentity.redstoneinterface.TileEntityRedstoneInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces.MonumentCoreRoom;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AsmHandler
{
	static HashMap<ModContainer, Long> loadingTimes = new HashMap<ModContainer, Long>();

	static Random rng = new Random();

	@SideOnly(Side.CLIENT)
	public static int getColorFromItemStack(ItemStack is, int renderPass)
	{
		NBTTagCompound compound;
		if ((compound = is.getTagCompound()) != null)
		{
			if (compound.hasKey("rtDye"))
			{
				return compound.getInteger("rtDye");
			}
		}
		return 16777215;
	}

	public static boolean shouldRain(World worldObj, BlockPos pos)
	{
		return TileEntityRainShield.shouldRain(worldObj, pos.add(0, -pos.getY(), 0));
	}

	public static int getColorFromArmorStack(ItemStack is)
	{
		NBTTagCompound compound;
		if ((compound = is.getTagCompound()) != null)
		{
			if (compound.hasKey("rtDye"))
			{
				return compound.getInteger("rtDye");
			}
		}
		return -1;
	}

	public static boolean shouldRenderPotionParticles(EntityLivingBase entity)
	{
		if (entity != null && entity instanceof EntityPlayer)
		{
			ItemStack helmet = ((EntityPlayer) entity).getEquipmentInSlot(4);
			if (helmet != null && helmet.getItem() == ModItems.magicHood)
			{
				return false;
			}
		}
		return true;
	}

	// False returns false, true runs vanilla behaviour
	@SideOnly(Side.CLIENT)
	public static boolean canRenderName(Entity e)
	{
		if (e != null && e instanceof EntityPlayer)
		{
			ItemStack helmet = ((EntityPlayer) e).getEquipmentInSlot(4);
			if (helmet != null && helmet.getItem() == ModItems.magicHood)
			{
				return false;
			}
		}
		return true;
	}

	static HashSet<BlockPos> posSet = new HashSet<BlockPos>();

	@SideOnly(Side.CLIENT)
	public static IBakedModel getModelFromBlockState(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		IBakedModel model = getModelFromBlockStateRec(state, access, pos);
		posSet.clear();
		return model;
	}

	@SideOnly(Side.CLIENT)
	public static IBakedModel getModelFromBlockStateRec(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		access = Minecraft.getMinecraft().theWorld;
		if (pos != null && access != null && state != null)
		{
			for (EnumFacing facing : EnumFacing.VALUES)
			{
				BlockPos offset = pos.offset(facing);

				if (!posSet.contains(offset))
				{
					posSet.add(offset);
					IBlockState faceState = access.getBlockState(offset);
					if (faceState != null && faceState.getBlock() == ModBlocks.lightRedirector)
					{
						BlockPos opPos = pos.offset(facing, 2);
						IBlockState oppositeState = access.getBlockState(opPos);
						if (oppositeState != null && !oppositeState.getBlock().isAir(access, opPos))
						{
							IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelFromBlockState(oppositeState, access, opPos);
							if (model instanceof ISmartBlockModel)
							{
								IBlockState extendedState = oppositeState.getBlock().getExtendedState(oppositeState, access, opPos);
								model = ((ISmartBlockModel) model).handleBlockState(extendedState);
							}

							if (model != null && model instanceof ISmartBlockModel)
							{
								model = new ChiselModelWrapper((ISmartBlockModel) model);
							}

							return model;
						}
					}
				}
			}
		}
		return null;
	}

	public static int getRedstonePower(World worldObj, BlockPos pos, EnumFacing facing)
	{
		return Math.max(TileEntityRedstoneInterface.getRedstonePower(worldObj, pos, facing),RedstoneSignalHandler.getHandler().getStrongPower(worldObj, pos, facing));
	}

	public static int getStrongPower(World worldObj, BlockPos pos, EnumFacing facing)
	{
		return Math.max(TileEntityRedstoneInterface.getStrongPower(worldObj, pos, facing), RedstoneSignalHandler.getHandler().getStrongPower(worldObj, pos, facing));
	}

	// Returns whether to cancel normal behaviour
	public static boolean addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		if (collidingEntity != null && collidingEntity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) collidingEntity;

			if (state.getBlock() instanceof BlockLiquid && collidingEntity.posY > pos.getY() + 0.9 && !(worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.lava || worldIn.getBlockState(pos.up()).getBlock().getMaterial() == Material.water))
			{
				if (!player.isSneaking())
				{
					ItemStack boots = player.inventory.armorItemInSlot(0);
					if (boots != null && ((((boots.getItem() == ModItems.waterWalkingBoots || boots.getItem() == ModItems.obsidianWaterWalkingBoots) || boots.getItem() == ModItems.lavaWader) && state.getBlock().getMaterial() == Material.water) || (boots.getItem() == ModItems.lavaWader && state.getBlock().getMaterial() == Material.lava)))
					{
						AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), (double) pos.getX() + 1, (double) pos.getY() + 1, (double) pos.getZ() + 1);
						if (mask.intersectsWith(bb))
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

			currentlyRendering = null;
		}

		return -8372020;
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

	public static int shouldLiquidSideBeRendered(BlockLiquid liquid, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();

		if (block == ModBlocks.specialChest || (Features.removeAirBubble && !block.isOpaqueCube()))
		{
			for (EnumFacing facing : EnumFacing.VALUES)
			{
				if (worldIn.isAirBlock(pos.offset(facing)))
				{
					return -1;
				}
			}
			return 0;
		}

		return -1;
	}

	static HashMap<MonumentCoreRoom, Integer> generationMap = new HashMap<MonumentCoreRoom, Integer>();

	public static void handleOceanCoreRoomGeneration(MonumentCoreRoom structure, World worldObj, StructureBoundingBox structureBoundingBox, StructureBoundingBox idkWhatThisIsBoundingBox, EnumFacing coordBaseMode)
	{
		if (!generationMap.containsKey(structure))
		{
			generationMap.put(structure, 1);
		}
		else
		{
			generationMap.put(structure, generationMap.get(structure) + 1);
		}

		int count = generationMap.get(structure);

		if (count == 4)
		{
			generationMap.remove(structure);
			BlockPos pos1 = new BlockPos(getXWithOffset(coordBaseMode, structureBoundingBox, 6, 6), getYWithOffset(coordBaseMode, structureBoundingBox, 1), getZWithOffset(coordBaseMode, structureBoundingBox, 6, 6));
			BlockPos pos2 = new BlockPos(getXWithOffset(coordBaseMode, structureBoundingBox, 9, 6), getYWithOffset(coordBaseMode, structureBoundingBox, 1), getZWithOffset(coordBaseMode, structureBoundingBox, 9, 6));
			BlockPos pos3 = new BlockPos(getXWithOffset(coordBaseMode, structureBoundingBox, 9, 9), getYWithOffset(coordBaseMode, structureBoundingBox, 1), getZWithOffset(coordBaseMode, structureBoundingBox, 9, 9));
			BlockPos pos4 = new BlockPos(getXWithOffset(coordBaseMode, structureBoundingBox, 6, 9), getYWithOffset(coordBaseMode, structureBoundingBox, 1), getZWithOffset(coordBaseMode, structureBoundingBox, 6, 9));

			if (!worldObj.isRemote && (worldObj.getBlockState(pos1).getBlock() != ModBlocks.specialChest && worldObj.getBlockState(pos2).getBlock() != ModBlocks.specialChest && worldObj.getBlockState(pos3).getBlock() != ModBlocks.specialChest && worldObj.getBlockState(pos4).getBlock() != ModBlocks.specialChest))
			{
				int random = rng.nextInt(4);

				BlockPos target = null;
				EnumFacing facing = null;

				switch (random)
				{
					case 0:
						target = pos1;
						facing = EnumFacing.EAST;
						break;
					case 1:
						target = pos2;
						facing = EnumFacing.SOUTH;
						break;
					case 2:
						target = pos3;
						facing = EnumFacing.WEST;
						break;
					case 3:
						target = pos4;
						facing = EnumFacing.EAST;
						break;
				}

				if (worldObj.getBlockState(target).getBlock() != ModBlocks.specialChest)
				{
					worldObj.setBlockState(target, ModBlocks.specialChest.getDefaultState().withProperty(BlockSpecialChest.FACING, facing));
					TileEntitySpecialChest te = (TileEntitySpecialChest) worldObj.getTileEntity(target);
					te.setChestType(1);

					ItemStack rareLoot = null;
					if (rng.nextBoolean())
					{
						rareLoot = new ItemStack(ModItems.bottleOfAir);
					}
					else
					{
						rareLoot = new ItemStack(ModItems.waterWalkingBoots);
					}

					WeightedRandomChestContent.generateChestContents(rng, ChestGenHooks.getItems(ChestGenHooks.PYRAMID_JUNGLE_CHEST, rng), te, ChestGenHooks.getCount(ChestGenHooks.PYRAMID_JUNGLE_CHEST, rng));
					te.setInventorySlotContents(0, rareLoot);

					RandomThings.instance.logger.log(Level.DEBUG, "Spawned Water Chest at " + target.toString());
				}
			}
		}
	}

	protected static int getXWithOffset(EnumFacing coordBaseMode, StructureBoundingBox boundingBox, int x, int z)
	{
		if (coordBaseMode == null)
		{
			return x;
		}
		else
		{
			switch (SwitchEnumFacing.field_176100_a[coordBaseMode.ordinal()])
			{
				case 1:
				case 2:
					return boundingBox.minX + x;
				case 3:
					return boundingBox.maxX - z;
				case 4:
					return boundingBox.minX + z;
				default:
					return x;
			}
		}
	}

	protected static int getYWithOffset(EnumFacing coordBaseMode, StructureBoundingBox boundingBox, int y)
	{
		return coordBaseMode == null ? y : y + boundingBox.minY;
	}

	protected static int getZWithOffset(EnumFacing coordBaseMode, StructureBoundingBox boundingBox, int x, int z)
	{
		if (coordBaseMode == null)
		{
			return z;
		}
		else
		{
			switch (SwitchEnumFacing.field_176100_a[coordBaseMode.ordinal()])
			{
				case 1:
					return boundingBox.maxZ - z;
				case 2:
					return boundingBox.minZ + z;
				case 3:
				case 4:
					return boundingBox.minZ + x;
				default:
					return z;
			}
		}
	}

	static final class SwitchEnumFacing
	{
		static final int[] field_176100_a = new int[EnumFacing.values().length];
		private static final String __OBFID = "CL_00001969";

		static
		{
			try
			{
				field_176100_a[EnumFacing.NORTH.ordinal()] = 1;
			}
			catch (NoSuchFieldError var4)
			{
				;
			}

			try
			{
				field_176100_a[EnumFacing.SOUTH.ordinal()] = 2;
			}
			catch (NoSuchFieldError var3)
			{
				;
			}

			try
			{
				field_176100_a[EnumFacing.WEST.ordinal()] = 3;
			}
			catch (NoSuchFieldError var2)
			{
				;
			}

			try
			{
				field_176100_a[EnumFacing.EAST.ordinal()] = 4;
			}
			catch (NoSuchFieldError var1)
			{
				;
			}
		}
	}
}
