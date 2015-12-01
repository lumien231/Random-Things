package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.block.BlockEnderMailbox;
import lumien.randomthings.handler.EnderLetterHandler;
import lumien.randomthings.util.InventoryUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntityEnderMailbox extends TileEntityBase implements ITickable
{
	UUID owner;

	int tickCounter;

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote && this.owner != null)
		{
			tickCounter++;

			if (tickCounter == 20 * 10)
			{
				tickCounter = 0;
				IBlockState state = worldObj.getBlockState(this.pos);
				boolean active = (Boolean) state.getValue(BlockEnderMailbox.ACTIVE);

				EnderLetterHandler enderLetterHandler = EnderLetterHandler.get(worldObj);
				boolean post = enderLetterHandler.hasInventoryFor(this.owner);

				if (post)
				{
					post = !InventoryUtil.isInventoryEmpty(enderLetterHandler.getOrCreateInventoryForPlayer(this.owner));
				}

				if (post != active)
				{
					worldObj.setBlockState(this.pos, state.withProperty(BlockEnderMailbox.ACTIVE, post));
				}
			}
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		if (this.owner != null)
		{
			compound.setString("owner", owner.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("owner"))
		{
			this.owner = UUID.fromString(compound.getString("owner"));
		}
	}

	@Override
	public boolean renderAfterData()
	{
		return true;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}

	public void setOwner(UUID owner)
	{
		this.owner = owner;
	}

	public UUID getOwner()
	{
		return owner;
	}
}
