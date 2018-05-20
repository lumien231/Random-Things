package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import lumien.randomthings.block.BlockChatDetector;
import lumien.randomthings.block.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityChatDetector extends TileEntityBase implements ITickable
{
	public static Set<TileEntityChatDetector> detectors = Collections.newSetFromMap(new WeakHashMap());

	UUID playerUUID;
	String chatMessage;
	boolean consume;

	boolean pulsing;
	int pulsingCounter;

	public TileEntityChatDetector()
	{
		chatMessage = "";

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			detectors.add(this);
		}
	}

	@Override
	public void onChunkUnload()
	{
		this.invalidate();
	}

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			if (pulsing)
			{
				pulsingCounter--;

				if (pulsingCounter <= 0)
				{
					pulsing = false;
					this.world.setBlockState(pos, ModBlocks.chatDetector.getDefaultState().withProperty(BlockChatDetector.POWERED, pulsing));
				}
			}
		}
	}

	public boolean isPulsing()
	{
		return pulsing;
	}

	@Override
	public void writeDataToNBT(NBTTagCompound compound, boolean sync)
	{
		compound.setBoolean("consume", consume);
		compound.setString("chatMessage", chatMessage);
		if (playerUUID != null)
		{
			compound.setString("playerUUID", playerUUID.toString());
		}

		compound.setBoolean("pulsing", pulsing);
		compound.setInteger("pulsingCounter", pulsingCounter);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		consume = compound.getBoolean("consume");
		chatMessage = compound.getString("chatMessage");
		if (compound.hasKey("playerUUID"))
		{
			playerUUID = UUID.fromString(compound.getString("playerUUID"));
		}

		pulsing = compound.getBoolean("pulsing");
		pulsingCounter = compound.getInteger("pulsingCounter");
	}

	public String getChatMessage()
	{
		return chatMessage;
	}

	public void setChatMessage(String chatMessage)
	{
		this.chatMessage = chatMessage;
		this.syncTE();
	}

	public void setPlayerUUID(UUID playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	private void pulse()
	{
		pulsing = true;
		pulsingCounter = 20;

		this.world.setBlockState(pos, ModBlocks.chatDetector.getDefaultState().withProperty(BlockChatDetector.POWERED, pulsing));
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return (oldState.getBlock() != newState.getBlock());
	}

	public boolean checkMessage(EntityPlayerMP entityPlayerMP, String sendMessage)
	{
		if (!this.world.isRemote)
		{
			UUID sendUUID = entityPlayerMP.getGameProfile().getId();
			if (sendUUID != null && sendUUID.equals(this.playerUUID))
			{
				if (this.chatMessage.equals(sendMessage))
				{
					pulse();

					if (consume)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}
		return false;
	}

	public void setConsume(boolean consume)
	{
		this.consume = consume;
	}

	public boolean consume()
	{
		return consume;
	}
}
