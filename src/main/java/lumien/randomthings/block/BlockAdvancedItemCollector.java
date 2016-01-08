package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityAdvancedItemCollector;
import lumien.randomthings.tileentity.TileEntityItemCollector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAdvancedItemCollector extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	protected BlockAdvancedItemCollector()
	{
		super("advancedItemCollector", Material.rock);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
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
		return new TileEntityAdvancedItemCollector();
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	{
		return func_181088_a(worldIn, pos, side.getOpposite());
	}

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
		return p_181088_2_ == EnumFacing.DOWN && isBlockInventory(p_181088_0_, p_181088_1_.down()) ? true : isBlockInventory(p_181088_0_,p_181088_1_.offset(p_181088_2_));
	}

	private static boolean isBlockInventory(World worldObj, BlockPos pos)
	{
		TileEntity te = worldObj.getTileEntity(pos);

		if (te == null)
		{
			return false;
		}
		
		return te instanceof IInventory;
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to
	 * allow for adjustments to the IBlockstate
	 */
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return func_181088_a(worldIn, pos, facing.getOpposite()) ? this.getDefaultState().withProperty(FACING, facing) : this.getDefaultState().withProperty(FACING, EnumFacing.DOWN);
	}

	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (this.checkForDrop(worldIn, pos, state) && !func_181088_a(worldIn, pos, ((EnumFacing) state.getValue(FACING)).getOpposite()))
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

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		this.updateBlockBounds(worldIn.getBlockState(pos));
	}

	private void updateBlockBounds(IBlockState state)
	{
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		float f = 0.25F;
		float f1 = 0.375F;
		float f2 = 5 / 16.0F;
		float f3 = 0.125F;
		float f4 = 0.1875F;

		switch (enumfacing)
		{
			case EAST:
				this.setBlockBounds(0.0F, 0.375F, 0.375F, f2, 0.625F, 0.625F);
				break;
			case WEST:
				this.setBlockBounds(1.0F - f2, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
				break;
			case SOUTH:
				this.setBlockBounds(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, f2);
				break;
			case NORTH:
				this.setBlockBounds(0.375F, 0.375F, 1.0F - f2, 0.625F, 0.625F, 1.0F);
				break;
			case UP:
				this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.0F + f2, 0.625F);
				break;
			case DOWN:
				this.setBlockBounds(0.375F, 1.0F - f2, 0.375F, 0.625F, 1.0F, 0.625F);
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ADVANCED_ITEM_COLLECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}
}
