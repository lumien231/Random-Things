package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockContainerBase extends BlockBase
{
	protected BlockContainerBase(String name, Material materialIn)
	{
		super(name, materialIn);
	}

	protected BlockContainerBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
	{
		super(name, materialIn, itemBlock);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityBase)
		{
			((TileEntityBase) te).breakBlock(worldIn, pos, state);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityBase)
		{
			((TileEntityBase) te).neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		}
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
}
