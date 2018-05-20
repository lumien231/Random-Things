package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityEnderAnchor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEnderAnchor extends BlockContainerBase
{
	protected BlockEnderAnchor()
	{
		super("enderAnchor", Material.ROCK);

		this.setHardness(1.5F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityEnderAnchor();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof TileEntityEnderAnchor)
		{
			((TileEntityEnderAnchor) te).discardTicket();
		}

		super.breakBlock(worldIn, pos, state);
	}
}
