package lumien.randomthings.block;

import java.util.Random;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.lib.INoItem;
import lumien.randomthings.tileentity.TileEntityAdvancedRedstoneRepeater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAdvancedRedstoneRepeater extends BlockContainerBase implements INoItem
{
	public static final PropertyBool LOCKED = PropertyBool.create("locked");
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	protected static final AxisAlignedBB REDSTONE_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
	/** Tells whether the repeater is powered or not */
	protected final boolean isRepeaterPowered;

	protected BlockAdvancedRedstoneRepeater(boolean powered)
	{
		super("advancedRedstoneRepeater" + (powered ? "_powered" : ""), Material.CIRCUITS);

		this.isRepeaterPowered = powered;
	}

	@Override
	public boolean hasNoItem()
	{
		return this.getUnlocalizedName().endsWith("_powered");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ADVANCED_REDSTONE_REPEATER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		return enumfacing == side || enumfacing.getOpposite() == side;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityAdvancedRedstoneRepeater();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return REDSTONE_DIODE_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	/**
	 * Checks if this block can be placed exactly at the given position.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).isTopSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).isTopSolid();
	}

	/**
	 * Called randomly when setTickRandomly is set to true (used by e.g. crops to
	 * grow, etc.)
	 */
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!this.isLocked(worldIn, pos, state))
		{
			boolean flag = this.shouldBePowered(worldIn, pos, state);

			if (this.isRepeaterPowered && !flag)
			{
				worldIn.setBlockState(pos, this.getUnpoweredState(state), 2);
			}
			else if (!this.isRepeaterPowered)
			{
				worldIn.setBlockState(pos, this.getPoweredState(state), 2);

				if (!flag)
				{
					worldIn.updateBlockTick(pos, this.getPoweredState(state).getBlock(), getDelay(worldIn, pos, true, false), -1);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return side.getAxis() != EnumFacing.Axis.Y;
	}

	protected boolean isPowered(IBlockState state)
	{
		return this.isRepeaterPowered;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return blockState.getWeakPower(blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		if (!this.isPowered(blockState))
		{
			return 0;
		}
		else
		{
			return blockState.getValue(FACING) == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
		}
	}

	/**
	 * Called when a neighboring block was changed and marks that this state should
	 * perform any checks during a neighbor change. Cases may include when redstone
	 * power is updated, cactus blocks popping off due to a neighboring solid block,
	 * etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (this.canBlockStay(worldIn, pos))
		{
			this.updateState(worldIn, pos, state);
		}
		else
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);

			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}
	}

	protected void updateState(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.isLocked(worldIn, pos, state))
		{
			boolean flag = this.shouldBePowered(worldIn, pos, state);

			if (this.isRepeaterPowered != flag && !worldIn.isBlockTickPending(pos, this))
			{
				int i = -1;

				if (this.isFacingTowardsRepeater(worldIn, pos, state))
				{
					i = -3;
				}
				else if (this.isRepeaterPowered)
				{
					i = -2;
				}

				worldIn.updateBlockTick(pos, this, getDelay(worldIn, pos, this.isRepeaterPowered, flag), i);
			}
		}
	}

	protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
	{
		return this.calculateInputStrength(worldIn, pos, state) > 0;
	}

	protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		BlockPos blockpos = pos.offset(enumfacing);
		int i = worldIn.getRedstonePower(blockpos, enumfacing);

		if (i >= 15)
		{
			return i;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(blockpos);
			return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? iblockstate.getValue(BlockRedstoneWire.POWER).intValue() : 0);
		}
	}

	protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		EnumFacing enumfacing1 = enumfacing.rotateY();
		EnumFacing enumfacing2 = enumfacing.rotateYCCW();
		return Math.max(this.getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), this.getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
	}

	protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if (this.isAlternateInput(iblockstate))
		{
			if (block == Blocks.REDSTONE_BLOCK)
			{
				return 15;
			}
			else
			{
				return block == Blocks.REDSTONE_WIRE ? iblockstate.getValue(BlockRedstoneWire.POWER).intValue() : worldIn.getStrongPower(pos, side);
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change
	 * based on its state.
	 */
	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (this.shouldBePowered(worldIn, pos, state))
		{
			worldIn.scheduleUpdate(pos, this, 1);
		}
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile Entity
	 * is set
	 */
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.notifyNeighbors(worldIn, pos, state);
	}

	protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING);
		BlockPos blockpos = pos.offset(enumfacing.getOpposite());
		if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.of(enumfacing.getOpposite()), false).isCanceled())
			return;
		worldIn.neighborChanged(blockpos, this, pos);
		worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
	}

	/**
	 * Called after a player destroys this Block - the posiiton pos may no longer
	 * hold the state indicated.
	 */
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
	{
		if (this.isRepeaterPowered)
		{
			for (EnumFacing enumfacing : EnumFacing.values())
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
			}
		}

		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
	{
		return 15;
	}

	public static boolean isDiode(IBlockState state)
	{
		return ModBlocks.unpoweredAdvancedRedstoneRepeater.isSameDiode(state) || ModBlocks.unpoweredAdvancedRedstoneRepeater.isSameDiode(state);
	}

	public boolean isSameDiode(IBlockState state)
	{
		Block block = state.getBlock();
		return block == this.getPoweredState(this.getDefaultState()).getBlock() || block == this.getUnpoweredState(this.getDefaultState()).getBlock();
	}

	public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state)
	{
		EnumFacing enumfacing = state.getValue(FACING).getOpposite();
		BlockPos blockpos = pos.offset(enumfacing);

		if (isDiode(worldIn.getBlockState(blockpos)))
		{
			return worldIn.getBlockState(blockpos).getValue(FACING) != enumfacing;
		}
		else
		{
			return false;
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return state.withProperty(LOCKED, Boolean.valueOf(this.isLocked(worldIn, pos, state)));
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If
	 * inapplicable, returns the passed blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	protected int getDelay(IBlockAccess world, BlockPos pos, boolean oldState, boolean newState)
	{
		TileEntityAdvancedRedstoneRepeater te = (TileEntityAdvancedRedstoneRepeater) world.getTileEntity(pos);

		if (oldState && !newState)
		{
			return te.getTurnOffDelay();
		}
		else
		{
			return te.getTurnOnDelay();
		}
	}

	protected IBlockState getPoweredState(IBlockState unpoweredState)
	{
		Boolean obool = unpoweredState.getValue(LOCKED);
		EnumFacing enumfacing = unpoweredState.getValue(FACING);
		return ModBlocks.poweredAdvancedRedstoneRepeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(LOCKED, obool);
	}

	protected IBlockState getUnpoweredState(IBlockState poweredState)
	{
		Boolean obool = poweredState.getValue(LOCKED);
		EnumFacing enumfacing = poweredState.getValue(FACING);
		return ModBlocks.unpoweredAdvancedRedstoneRepeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(LOCKED, obool);
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.unpoweredAdvancedRedstoneRepeater);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(ModBlocks.unpoweredAdvancedRedstoneRepeater);
	}

	public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
	{
		return this.getPowerOnSides(worldIn, pos, state) > 0;
	}

	protected boolean isAlternateInput(IBlockState state)
	{
		return isDiode(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (this.isRepeaterPowered)
		{
			EnumFacing enumfacing = stateIn.getValue(FACING);
			double d0 = pos.getX() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
			double d1 = pos.getY() + 0.4F + (rand.nextFloat() - 0.5F) * 0.2D;
			double d2 = pos.getZ() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
			float f = -5.0F;

			f = f / 16.0F;
			double d3 = f * enumfacing.getFrontOffsetX();
			double d4 = f * enumfacing.getFrontOffsetZ();
			worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
		}
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but
	 * before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		IBlockState newState = worldIn.getBlockState(pos);

		if (newState.getBlock() == ModBlocks.poweredAdvancedRedstoneRepeater || newState.getBlock() == ModBlocks.unpoweredAdvancedRedstoneRepeater)
		{
			return;
		}
		else
		{
			super.breakBlock(worldIn, pos, state);
			this.notifyNeighbors(worldIn, pos, state);
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(LOCKED, Boolean.valueOf(false));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i = i | state.getValue(FACING).getHorizontalIndex();
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, LOCKED });
	}

	@Override
	public boolean isAssociatedBlock(Block other)
	{
		return this.isSameDiode(other.getDefaultState());
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks,
	 * CUTOUT or CUTOUT_MIPPED for on-off transparency (glass, reeds), TRANSLUCENT
	 * for fully blended transparency (stained glass)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	/*
	 * ======================================== FORGE START
	 * =====================================
	 */
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		if (super.rotateBlock(world, pos, axis))
		{
			IBlockState state = world.getBlockState(pos);
			state = getUnpoweredState(state);
			world.setBlockState(pos, state);

			if (shouldBePowered(world, pos, state))
			{
				world.scheduleUpdate(pos, this, 1);
			}
			return true;
		}
		return false;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}
}
