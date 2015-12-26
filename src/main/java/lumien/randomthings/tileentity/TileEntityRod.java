package lumien.randomthings.tileentity;

import java.util.UUID;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public abstract class TileEntityRod extends TileEntityBase implements ITickable
{
	boolean creative;
	UUID placedBy;

	@Override
	public void writeDataToNBT(NBTTagCompound compound)
	{
		if (placedBy != null)
		{
			compound.setString("placedBy", placedBy.toString());
		}

		compound.setBoolean("creative", creative);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("placedBy"))
		{
			this.placedBy = UUID.fromString(compound.getString("placedBy"));
		}

		this.creative = compound.getBoolean("creative");
	}

	protected boolean consumeExperience(int amount)
	{
		if (creative)
		{
			return true;
		}

		if (placedBy == null)
		{
			return false;
		}

		EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(placedBy);

		if (player != null)
		{
			if (player.experienceTotal < amount)
			{
				return false;
			}
			else
			{
				player.experienceTotal -= amount;
				return true;
			}
		}
		else
		{
			return false;
		}
	}

	public void setOwner(EntityPlayer placer)
	{
		this.placedBy = placer.getGameProfile().getId();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}
}
