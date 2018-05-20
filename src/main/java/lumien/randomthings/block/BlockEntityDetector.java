package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEntityDetector extends BlockContainerBase
{

	public BlockEntityDetector()
	{
		super("entityDetector", Material.ROCK);

		this.setHardness(1.5F);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityEntityDetector();
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntityEntityDetector te = (TileEntityEntityDetector) blockAccess.getTileEntity(pos);

		return te.isPowered() ? 15 : 0;
	}

	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		TileEntityEntityDetector te = (TileEntityEntityDetector) blockAccess.getTileEntity(pos);

		if (te.isPowered() && te.strongOutput())
		{
			return 15;
		}
		else
		{
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}
	}

	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.ENTITY_DETECTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityEntityDetector tileentity = (TileEntityEntityDetector) worldIn.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(worldIn, pos, tileentity.getInventory());

		if (tileentity.strongOutput())
		{

			for (EnumFacing facing : EnumFacing.VALUES)
			{
				worldIn.notifyNeighborsOfStateChange(pos.offset(facing), ModBlocks.entityDetector, false);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}
}
