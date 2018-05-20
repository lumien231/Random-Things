package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityPlayerInterface;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPlayerInterface extends BlockContainerBase
{
	protected BlockPlayerInterface()
	{
		super("playerInterface", Material.ROCK);

		this.setSoundType(SoundType.STONE);
		this.setHardness(4.0F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityPlayerInterface();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote && placer != null && placer instanceof EntityPlayerMP && worldIn.getTileEntity(pos) != null)
		{
			EntityPlayerMP player = (EntityPlayerMP) placer;
			((TileEntityPlayerInterface) worldIn.getTileEntity(pos)).setPlayerUUID(player.getGameProfile().getId());
		}
		else
		{
			if (!worldIn.isRemote)
			{
				worldIn.setBlockToAir(pos);
			}
		}
	}
}
