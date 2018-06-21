package lumien.randomthings.block;

import com.mojang.authlib.GameProfile;

import lumien.randomthings.tileentity.TileEntityRainShield;
import lumien.randomthings.tileentity.TileEntitySpectreLens;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpectreLens extends BlockContainerBase
{
	AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1/16D, 1);

	protected BlockSpectreLens()
	{
		super("spectreLens", Material.GLASS);
		
		this.setHardness(0.3F);
		this.setSoundType(SoundType.GLASS);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canPlaceOn(worldIn, pos.down());
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos changedPos)
	{
		super.neighborChanged(state, worldIn, pos, neighborBlock, changedPos);
		
		checkForDrop(worldIn, pos, state);
	}

	private boolean canPlaceOn(World worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos).getBlock() == Blocks.BEACON;
	}

	protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this && this.canPlaceOn(worldIn, pos.down()))
		{
			return true;
		}
		else
		{
			if (worldIn.getBlockState(pos).getBlock() == this)
			{
				TileEntity te = worldIn.getTileEntity(pos);
				
				if (te instanceof TileEntitySpectreLens)
				{
					((TileEntitySpectreLens)te).breakBlock(worldIn, pos, state);
				}
				
				this.dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}

			return false;
		}
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
				TileEntitySpectreLens lens = (TileEntitySpectreLens) worldIn.getTileEntity(pos);
				
				lens.setOwner(profile.getId());
			}
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySpectreLens();
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
