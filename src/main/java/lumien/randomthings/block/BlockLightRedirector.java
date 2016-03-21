package lumien.randomthings.block;

import lumien.randomthings.network.MessageUtil;
import lumien.randomthings.network.messages.MessageLightRedirector;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLightRedirector extends BlockBase
{
	public BlockLightRedirector()
	{
		super("lightRedirector", Material.rock);

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
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (!worldIn.isRemote)
		{
			MessageLightRedirector message = new MessageLightRedirector(worldIn.provider.getDimension(), pos);
			
			MessageUtil.sendToAllWatchingPos(worldIn, pos, message);
		}
	}
}
