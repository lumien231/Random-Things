package lumien.randomthings.handler;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;

import lumien.randomthings.RandomThings;
import lumien.randomthings.asm.MCPNames;
import lumien.randomthings.block.BlockSpecialChest;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.config.Features;
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
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces.MonumentCoreRoom;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AsmHandler
{
	static HashMap<ModContainer, Long> loadingTimes = new HashMap<ModContainer, Long>();

	static Random rng = new Random();

	static Field fluidRenderer;
	static
	{
		if (FMLCommonHandler.instance().getSide().isClient())
		{
			getFields();
		}
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
		if (is != null)
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
	public static int renderBlock(BlockRendererDispatcher dispatcher, IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn)
	{
		long time = System.currentTimeMillis();
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
					if (blockAccess.getWorldType() != WorldType.DEBUG_WORLD)
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

		return 2;
	}

	static HashSet<BlockPos> posSet = new HashSet<BlockPos>();

	public static BlockPos getSwitchedPosition(IBlockAccess access, BlockPos pos)
	{
		if (pos != null && access != null)
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
						if (oppositeState != null && !oppositeState.getBlock().isAir(oppositeState, access, opPos))
						{
							return getSwitchedPosition(access, opPos);
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

			if (state.getBlock() instanceof BlockLiquid && collidingEntity.posY > pos.getY() + 0.9 && !(worldIn.getBlockState(pos.up()).getBlock().getMaterial(worldIn.getBlockState(pos.up())) == Material.lava || worldIn.getBlockState(pos.up()).getBlock().getMaterial(worldIn.getBlockState(pos.up())) == Material.water))
			{
				if (!player.isSneaking())
				{
					ItemStack boots = player.inventory.armorItemInSlot(0);
					if (boots != null && ((((boots.getItem() == ModItems.waterWalkingBoots || boots.getItem() == ModItems.obsidianWaterWalkingBoots) || boots.getItem() == ModItems.lavaWader) && state.getBlock().getMaterial(state) == Material.water) || (boots.getItem() == ModItems.lavaWader && state.getBlock().getMaterial(state) == Material.lava)))
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
}
