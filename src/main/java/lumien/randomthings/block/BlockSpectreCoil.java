package lumien.randomthings.block;

import java.awt.Color;
import java.util.List;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.config.Numbers;
import lumien.randomthings.item.block.ItemBlockColored;
import lumien.randomthings.item.block.ItemBlockSpectreCoil;
import lumien.randomthings.lib.ILuminousBlock;
import lumien.randomthings.lib.IRTBlockColor;
import lumien.randomthings.tileentity.TileEntitySpectreCoil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectreCoil extends BlockContainerBase implements ILuminousBlock, IRTBlockColor
{
	public enum CoilType
	{
		NORMAL("normal", Color.CYAN.getRGB()), REDSTONE("redstone", Color.RED.getRGB()), ENDER("ender", new Color(200, 0, 210).getRGB()), NUMBER("number", Color.GREEN.getRGB()), GENESIS("genesis", Color.ORANGE.getRGB());

		int color;
		String name;

		private CoilType(String name, int color)
		{
			this.name = name;
			this.color = color;
		}
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	// Height: 0.09375
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.3125F, 0.3125F, 1.0F - 0.09375, 0.6875F, 0.6875F, 1.0F);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.3125F, 0.3125F, 0.0F, 0.6875F, 0.6875F, 0.09375);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(1.0F - 0.09375, 0.3125F, 0.3125F, 1.0F, 0.6875F, 0.6875F);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0F, 0.3125F, 0.3125F, 0.09375, 0.6875F, 0.6875F);
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.09375, 0.6875F);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.3125F, 1.0F - 0.09375, 0.3125F, 0.6875F, 1.0F, 0.6875F);

	CoilType coilType;

	protected BlockSpectreCoil(CoilType type)
	{
		super("spectreCoil_" + type.name, Material.ROCK, ItemBlockSpectreCoil.class);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		this.setHardness(0.3F);
		
		this.coilType = type;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		String display;
		
		switch (this.coilType)
		{
			case NORMAL:
				display = I18n.format("tile.spectrecoil.transfer", "1024");
				break;
			case REDSTONE:
				display = I18n.format("tile.spectrecoil.transfer", "4096");
				break;
			case ENDER:
				display = I18n.format("tile.spectrecoil.transfer", "20480");
				break;
			case GENESIS:
				display = I18n.format("tile.spectrecoil.generate", "Infinite");
				break;
			case NUMBER:
				display = I18n.format("tile.spectrecoil.generate", Numbers.NUMBERED_SPECTRECOIL_ENERGY + "");
				break;
			default:
				display = I18n.format("tile.spectrecoil.transfer", "???");
				break;
		}
		
		tooltip.add(display);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).ordinal();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpectreCoil(coilType);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return func_181088_a(worldIn, pos, side.getOpposite());
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			if (func_181088_a(worldIn, pos, enumfacing))
			{
				return true;
			}
		}

		return false;
	}

	protected static boolean func_181088_a(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_)
	{
		return p_181088_2_ == EnumFacing.DOWN && isBlockEnergyStorage(p_181088_0_, p_181088_1_.down(), p_181088_2_) ? true : isBlockEnergyStorage(p_181088_0_, p_181088_1_.offset(p_181088_2_), p_181088_2_);
	}

	private static boolean isBlockEnergyStorage(World worldObj, BlockPos pos, EnumFacing facing)
	{
		TileEntity te = worldObj.getTileEntity(pos);

		if (te == null)
		{
			return false;
		}

		return te.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()) || te.hasCapability(CapabilityEnergy.ENERGY, null);
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return func_181088_a(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty(FACING, facing) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
	}

	/**
	 * Called when a neighboring block changes.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (this.checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, state.getValue(FACING).getOpposite()))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) placer;

			GameProfile profile = player.getGameProfile();

			if (profile != null)
			{
				TileEntitySpectreCoil coil = (TileEntitySpectreCoil) worldIn.getTileEntity(pos);

				coil.setOwner(profile.getId());
			}
		}
	}

	private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.canPlaceBlockAt(worldIn, pos))
		{
			return true;
		}
		else
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
			return false;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		switch (enumfacing)
		{
			case EAST:
				return EAST_AABB;
			case WEST:
				return WEST_AABB;
			case SOUTH:
				return SOUTH_AABB;
			case NORTH:
				return NORTH_AABB;
			case UP:
				return UP_AABB;
			case DOWN:
				return DOWN_AABB;
		}

		return UP_AABB;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	@Override
	public boolean shouldGlow(IBlockState state, int tintIndex)
	{
		return true;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess p_186720_2_, BlockPos pos, int tintIndex)
	{
		return this.coilType.color;
	}
}
