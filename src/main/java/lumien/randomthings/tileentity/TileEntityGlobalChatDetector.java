package lumien.randomthings.tileentity;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import lumien.randomthings.block.BlockGlobalChatDetector;
import lumien.randomthings.block.ModBlocks;
import lumien.randomthings.item.ItemIDCard;
import lumien.randomthings.lib.IOpable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TileEntityGlobalChatDetector extends TileEntityBase implements ITickable, IOpable
{
	public static Set<TileEntityGlobalChatDetector> detectors = Collections.newSetFromMap(new WeakHashMap<TileEntityGlobalChatDetector, Boolean>());

	UUID playerUUID;
	String chatMessage;
	boolean consume;

	boolean pulsing;
	int pulsingCounter;

	public TileEntityGlobalChatDetector()
	{
		chatMessage = "";

		if (FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			detectors.add(this);
		}

		this.setItemHandler(9);
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
					this.world.setBlockState(pos, ModBlocks.globalChatDetector.getDefaultState().withProperty(BlockGlobalChatDetector.POWERED, pulsing));
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

		compound.setBoolean("pulsing", pulsing);
		compound.setInteger("pulsingCounter", pulsingCounter);
	}

	@Override
	public void readDataFromNBT(NBTTagCompound compound, boolean sync)
	{
		consume = compound.getBoolean("consume");
		chatMessage = compound.getString("chatMessage");

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

		this.world.setBlockState(pos, ModBlocks.globalChatDetector.getDefaultState().withProperty(BlockGlobalChatDetector.POWERED, pulsing));
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
			if (sendUUID != null && this.chatMessage.equals(sendMessage))
			{
				pulse();

				if (consume)
				{
					boolean validConsume = false;
					for (int slot = 0; slot < this.getItemHandler().getSlots(); slot++)
					{
						ItemStack stack = this.getItemHandler().getStackInSlot(slot);

						if (!stack.isEmpty())
						{
							if (stack.getItem() instanceof ItemIDCard)
							{
								UUID cardOwner = ItemIDCard.getUUID(stack);

								if (cardOwner.equals(sendUUID))
								{
									validConsume = true;
									break;
								}
							}
						}
					}
					return validConsume || isOp();
				}
				else
				{
					return false;
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
