package lumien.randomthings.container;

import lumien.randomthings.tileentity.TileEntityEntityDetector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerEntityDetector extends Container
{
	World worldObj;
	BlockPos pos;
	TileEntityEntityDetector entityDetector;
	
	public ContainerEntityDetector(EntityPlayer player, World world, int x, int y, int z)
	{
		this.worldObj = world;
		this.pos = new BlockPos(x,y,z);
		this.entityDetector = (TileEntityEntityDetector) world.getTileEntity(pos);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.worldObj.getTileEntity(this.pos) != entityDetector ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
}
