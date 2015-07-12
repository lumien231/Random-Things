package lumien.randomthings.block;

import java.util.HashSet;

import lumien.randomthings.RandomThings;
import lumien.randomthings.item.ModItems;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityRedstoneInterface;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockRedstoneInterface extends BlockContainerBase
{

	protected BlockRedstoneInterface()
	{
		super("redstoneInterface", Material.rock);

		this.setHardness(2);
	}

	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityRedstoneInterface();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	static HashSet<BlockPos> notifiedPositions = new HashSet<BlockPos>();

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		if (notifiedPositions.contains(pos))
		{
			return;
		}
		notifiedPositions.add(pos);
		TileEntityRedstoneInterface te = (TileEntityRedstoneInterface) worldIn.getTileEntity(pos);
		te.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

		notifiedPositions.remove(pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		((TileEntityRedstoneInterface) worldIn.getTileEntity(pos)).broken();
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			ItemStack equipped = playerIn.getCurrentEquippedItem();
			if (equipped != null && equipped.getItem() == ModItems.redstoneTool)
			{
				return false;
			}

			playerIn.openGui(RandomThings.instance, GuiIds.REDSTONE_INTERFACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return true;
	}
}
