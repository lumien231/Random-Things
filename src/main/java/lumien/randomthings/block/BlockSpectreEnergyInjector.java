package lumien.randomthings.block;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.tileentity.TileEntitySpectreEnergyInjector;
import lumien.randomthings.tileentity.TileEntitySpectreLens;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSpectreEnergyInjector extends BlockContainerBase
{

	protected BlockSpectreEnergyInjector()
	{
		super("spectreEnergyInjector", Material.ROCK);
		
		this.setHardness(3.0F);
		this.setSoundType(SoundType.GLASS);
	}
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		return layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpectreEnergyInjector();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (!worldIn.isRemote && placer instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) placer;
			
			GameProfile profile = player.getGameProfile();
			
			if (profile != null)
			{
				TileEntitySpectreEnergyInjector injector = (TileEntitySpectreEnergyInjector) worldIn.getTileEntity(pos);
				
				injector.setOwner(profile.getId());
			}
		}
	}
}
