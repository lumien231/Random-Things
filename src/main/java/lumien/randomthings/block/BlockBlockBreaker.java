package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityBlockBreaker;
import lumien.randomthings.tileentity.TileEntityEnderBridge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlockBreaker extends BlockContainerBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockBlockBreaker()
	{
		super("blockBreaker", Material.rock);
		
		this.setHardness(3.5F);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		((TileEntityBlockBreaker) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityBlockBreaker();
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		((TileEntityBlockBreaker) worldIn.getTileEntity(pos)).onNeighborBlockChange(worldIn, pos, state, neighborBlock);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(worldIn, pos, state);
		this.setDefaultDirection(worldIn, pos, state);
		
		((TileEntityBlockBreaker) worldIn.getTileEntity(pos)).onNeighborBlockChange(worldIn, pos, state, null);
	}

	private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			EnumFacing enumfacing = state.getValue(FACING);
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
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, BlockDispenser.getFacing(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | state.getValue(FACING).getIndex();

		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { FACING });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}
}
