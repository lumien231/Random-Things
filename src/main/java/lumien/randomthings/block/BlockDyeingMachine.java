package lumien.randomthings.block;

import lumien.randomthings.RandomThings;
import lumien.randomthings.lib.GuiIds;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDyeingMachine extends BlockBase
{
	public BlockDyeingMachine()
	{
		super("dyeingMachine", Material.wood);

		this.setStepSound(soundTypeWood);
		this.setHardness(0.7F);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(RandomThings.instance, GuiIds.DYEING_MACHINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
