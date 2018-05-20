package lumien.randomthings.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import lumien.randomthings.tileentity.TileEntityInventoryRerouter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockInventoryRerouter extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public static final OverrideDataProperty OVERRIDE_DATA = new OverrideDataProperty();

	protected BlockInventoryRerouter()
	{
		super("inventoryRerouter", Material.ROCK);

		this.setHardness(1.5F);
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		EnumFacing myFacing = state.getValue(FACING);

		if (facing != myFacing)
		{
			if (te instanceof TileEntityInventoryRerouter)
			{
				((TileEntityInventoryRerouter) te).rotateFacing(facing);
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityInventoryRerouter();
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultFacing(worldIn, pos, state);
	}

	HashSet<BlockPos> circleSet = new HashSet<>();

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		if (circleSet.contains(pos))
		{
			return;
		}

		EnumFacing facing = state.getValue(FACING);
		BlockPos offset = pos.offset(facing);

		if (offset.equals(changedPos))
		{
			circleSet.add(pos);
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
			circleSet.remove(pos);
		}

		super.neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos.north());
			IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
			{
				enumfacing = EnumFacing.NORTH;
			}
			else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
			{
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | state.getValue(FACING).getIndex();

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] { FACING }, new IUnlistedProperty[] { OVERRIDE_DATA });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		IExtendedBlockState extendedState = (IExtendedBlockState) state;

		if (te instanceof TileEntityInventoryRerouter)
		{
			TileEntityInventoryRerouter rerouter = (TileEntityInventoryRerouter) te;

			Map<EnumFacing, EnumFacing> facingMap = rerouter.getFacingMap();

			return extendedState.withProperty(OVERRIDE_DATA, new HashMap<>(facingMap));
		}

		return super.getActualState(state, worldIn, pos);
	}

	private static class OverrideDataProperty implements IUnlistedProperty<HashMap<EnumFacing, EnumFacing>>
	{
		@Override
		public String getName()
		{
			return "overridedata";
		}

		@Override
		public boolean isValid(HashMap<EnumFacing, EnumFacing> value)
		{
			return true;
		}

		@Override
		public Class getType()
		{
			return HashMap.class;
		}

		@Override
		public String valueToString(HashMap<EnumFacing, EnumFacing> value)
		{
			return value.toString();
		}

	}
}
