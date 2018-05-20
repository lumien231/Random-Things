package lumien.randomthings.tileentity;

import java.util.UUID;

import lumien.randomthings.lib.IRedstoneSensitive;
import lumien.randomthings.network.PacketHandler;
import lumien.randomthings.network.messages.MessageNotification;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityNotificationInterface extends TileEntityBase implements IRedstoneSensitive
{
	UUID owner;

	String title = "Title";
	String description = "Description";

	public TileEntityNotificationInterface()
	{
		this.setItemHandler(1);
	}

	public void setData(String title, String description)
	{
		this.title = title;
		this.description = description;

		this.markDirty();
		this.syncTE();
	}

	public String getTitle()
	{
		return title;
	}

	public String getDescription()
	{
		return description;
	}

	private void notifyGo()
	{
		if (this.owner != null)
		{
			EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(owner);

			if (player != null)
			{
				MessageNotification message = new MessageNotification(title, description, getItemHandler().getStackInSlot(0));

				PacketHandler.INSTANCE.sendTo(message, player);
			}
		}
	}

	@Override
	public void redstoneChange(boolean oldState, boolean newState)
	{
		if (!oldState && newState)
		{
			notifyGo();
		}
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setString("title", title);
		compound.setString("description", description);

		if (this.owner != null)
		{
			compound.setString("owner", owner.toString());
		}
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		this.title = compound.getString("title");
		this.description = compound.getString("description");

		if (compound.hasKey("owner"))
		{
			this.owner = UUID.fromString(compound.getString("owner"));
		}
	}

	public void setPlayerUUID(UUID id)
	{
		this.owner = id;
	}

	public UUID getOwner()
	{
		return owner;
	}

}
