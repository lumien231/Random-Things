package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityAnalogEmitter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAnalogEmitter extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	protected BlockAnalogEmitter()
	{
		super("analogEmitter", Material.rock);

		this.setHardness(3.0F).setResistance(5.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityAnalogEmitter();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, BlockDispenser.getFacing(meta));
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		TileEntityAnalogEmitter te = (TileEntityAnalogEmitter) worldIn.getTileEntity(pos);

		te.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((EnumFacing) state.getValue(FACING)).getIndex();

		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultDirection(worldIn, pos, state);
	}

	private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			boolean flag = worldIn.getBlockState(pos.north()).getBlock().isFullBlock();
			boolean flag1 = worldIn.getBlockState(pos.south()).getBlock().isFullBlock();

			if (enumfacing == EnumFacing.NORTH && flag && !flag1)
			{
				enumfacing = EnumFacing.SOUTH;
			}
			else if (enumfacing == EnumFacing.SOUTH && flag1 && !flag)
			{
				enumfacing = EnumFacing.NORTH;
			}
			else
			{
				boolean flag2 = worldIn.getBlockState(pos.west()).getBlock().isFullBlock();
				boolean flag3 = worldIn.getBlockState(pos.east()).getBlock().isFullBlock();

				if (enumfacing == EnumFacing.WEST && flag2 && !flag3)
				{
					enumfacing = EnumFacing.EAST;
				}
				else if (enumfacing == EnumFacing.EAST && flag3 && !flag2)
				{
					enumfacing = EnumFacing.WEST;
				}
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			if (playerIn.isSneaking())
			{
				EnumFacing facing = (EnumFacing) state.getValue(FACING);

				if (facing != side)
				{
					worldIn.setBlockState(pos, state.withProperty(FACING, side));

					TileEntityAnalogEmitter analogEmitter = (TileEntityAnalogEmitter) worldIn.getTileEntity(pos);

					analogEmitter.setLevel(analogEmitter.emitLevel);
				}
			}
			else
			{
				playerIn.openGui(RandomThings.instance, GuiIds.ANALOG_EMITTER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer));
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)), 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		TileEntityAnalogEmitter te = (TileEntityAnalogEmitter) worldIn.getTileEntity(pos);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

		if (enumfacing.getOpposite() != side)
		{
			return te.getOutput();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		return isProvidingStrongPower(worldIn, pos, state, side);
	}
}
