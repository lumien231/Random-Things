package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import lumien.randomthings.tileentity.TileEntityImbuingStation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockImbuingStation extends BlockContainerBase
{
	public BlockImbuingStation()
	{
		super("imbuingStation", Material.rock);

		this.setHardness(1.25F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityImbuingStation();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.IMBUING_STATION, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
