package lumien.randomthings.item;

import lumien.randomthings.block.BlockEnderCore;
import lumien.randomthings.tileentity.cores.TileEntityEnderCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemInertLinkingOrb extends ItemBase
{
	public ItemInertLinkingOrb()
	{
		super("inertLinkingOrb");

		this.setMaxStackSize(1);
	}
}
