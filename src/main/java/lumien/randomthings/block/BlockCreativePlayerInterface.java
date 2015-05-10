package lumien.randomthings.block;

import lumien.randomthings.tileentity.TileEntityCreativePlayerInterface;
import lumien.randomthings.tileentity.TileEntityPlayerInterface;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockCreativePlayerInterface extends BlockContainerBase
{
	protected BlockCreativePlayerInterface()
	{
		super("creativePlayerInterface", Material.rock);

		this.setStepSound(soundTypeStone);
		this.setHardness(4.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityCreativePlayerInterface();
	}
	
	@Override
    public int getRenderType()
    {
        return 3;
    }
}
