package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityRedstoneObserver;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneObserver extends BlockContainerBase
{

	protected BlockRedstoneObserver()
	{
		super("redstoneObserver", Material.ROCK);
		
		this.setHardness(2);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityRedstoneObserver();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		((TileEntityRedstoneObserver) worldIn.getTileEntity(pos)).broken();
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return ((TileEntityRedstoneObserver) blockAccess.getTileEntity(pos)).getWeakPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return ((TileEntityRedstoneObserver) blockAccess.getTileEntity(pos)).getStrongPower(blockState, blockAccess, pos, side);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			ItemStack equipped = playerIn.getHeldItemMainhand();
			if (equipped != null && equipped.getItem() == ModItems.redstoneTool)
			{
				return false;
			}

			playerIn.openGui(RandomThings.instance, GuiIds.REDSTONE_OBSERVER, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return true;
	}
}
