package lumien.randomthings.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
	{
		super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
	}
}
