package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityCreativePlayerInterface;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

public class BlockCreativePlayerInterface extends BlockContainerBase
{
	protected BlockCreativePlayerInterface()
	{
		super("creativePlayerInterface", Material.ROCK);

		this.setSoundType(SoundType.STONE);
		this.setHardness(4.0F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityCreativePlayerInterface();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
}
