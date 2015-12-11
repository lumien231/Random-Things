package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEntityDetector extends BlockContainerBase
{

	public BlockEntityDetector()
	{
		super("entityDetector", Material.rock);
		
		this.setHardness(1.5F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityEntityDetector();
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
	{
		TileEntityEntityDetector te = (TileEntityEntityDetector) worldIn.getTileEntity(pos);

		return te.isPowered() ? 15 : 0;
	}
	
	@Override
	public boolean isNormalCube()
	{
		return true;
	}
	
	public boolean shouldCheckWeakPower(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
		return false;
    }
	
	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ENTITY_DETECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
