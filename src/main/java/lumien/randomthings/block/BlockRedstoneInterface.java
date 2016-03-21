package lumien.randomthings.block;

import java.util.HashSet;

import lumien.randomthings.tileentity.redstoneinterface.TileEntityRedstoneInterface;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockRedstoneInterface extends BlockContainerBase
{
	protected BlockRedstoneInterface(String name, Material materialIn)
	{
		super(name, materialIn);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	static HashSet<BlockPos> notifiedPositions = new HashSet<BlockPos>();
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (notifiedPositions.contains(pos))
		{
			return;
		}
		notifiedPositions.add(pos);
		TileEntityRedstoneInterface te = (TileEntityRedstoneInterface) worldIn.getTileEntity(pos);
		te.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

		notifiedPositions.remove(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		((TileEntityRedstoneInterface) worldIn.getTileEntity(pos)).broken();
		super.breakBlock(worldIn, pos, state);
	}
}
