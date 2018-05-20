package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityInventoryTester;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockInventoryTester extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.375F, 0.375F, 1.0F - 1 / 16.0F, 0.625F, 0.625F, 1.0F);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1 / 16.0F);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(1.0F - 1 / 16.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1 / 16.0F, 0.625F, 0.625F);
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 0.0F + 1 / 16.0F, 0.625F);
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.375F, 1.0F - 1 / 16.0F, 0.375F, 0.625F, 1.0F, 0.625F);

	protected BlockInventoryTester()
	{
		super("inventoryTester", Material.ROCK);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		this.setHardness(0.3F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.INVENTORY_TESTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntityInventoryTester te = (TileEntityInventoryTester) blockAccess.getTileEntity(pos);

		return te.isPowered() ? 15 : 0;
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
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
		return new TileEntityInventoryTester();
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
		return canPlaceAgainst(worldIn, pos, side.getOpposite());
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		for (EnumFacing enumfacing : EnumFacing.values())
		{
			if (canPlaceAgainst(worldIn, pos, enumfacing))
			{
				return true;
			}
		}

		return false;
	}

	protected static boolean canPlaceAgainst(World p_181088_0_, BlockPos p_181088_1_, EnumFacing p_181088_2_)
	{
		return p_181088_2_ == EnumFacing.DOWN && isBlockInventory(p_181088_0_, p_181088_1_.down(), p_181088_2_) ? true : isBlockInventory(p_181088_0_, p_181088_1_.offset(p_181088_2_), p_181088_2_);
	}

	private static boolean isBlockInventory(World worldObj, BlockPos pos, EnumFacing facing)
	{
		TileEntity te = worldObj.getTileEntity(pos);

		if (te == null)
		{
			return false;
		}

		return te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return canPlaceAgainst(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty(FACING, facing) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
	}

	/**
	 * Called when a neighboring block changes.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (this.checkForDrop(worldIn, pos, state) && !canPlaceAgainst(worldIn, pos, state.getValue(FACING).getOpposite()))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
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
}
