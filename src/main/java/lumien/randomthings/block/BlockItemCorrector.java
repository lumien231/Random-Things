package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityItemCorrector;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockItemCorrector extends BlockContainerBase
{
	protected BlockItemCorrector()
	{
		super("itemCorrector", Material.rock);
		
		this.setHardness(2.0F);
		this.slipperiness = 1F / 0.98F;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityItemCorrector();
	}

}
