package lumien.randomthings.block;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageLightRedirector;
import lumien.randomthings.tileentity.TileEntityLightRedirector;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLightRedirector extends BlockContainerBase
{
	public BlockLightRedirector()
	{
		super("lightRedirector", Material.ROCK);

		this.setSoundType(SoundType.WOOD);
		this.setHardness(2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state,IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		((TileEntityLightRedirector) worldIn.getTileEntity(pos)).broken();
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock)
	{
		if (!worldIn.isRemote)
		{
			MessageLightRedirector message = new MessageLightRedirector(worldIn.provider.getDimension(), pos);
			
			MessageUtil.sendToAllWatchingPos(worldIn, pos, message);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityLightRedirector();
	}
}
